package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.api.DeliveryRestController;
import ru.yandex.practicum.api.PaymentRestController;
import ru.yandex.practicum.api.ShoppingCartRestController;
import ru.yandex.practicum.api.WarehouseRestController;
import ru.yandex.practicum.cart.exception.NoCartException;
import ru.yandex.practicum.cart.exception.NotAuthorizedUserException;
import ru.yandex.practicum.delivery.DeliveryDto;
import ru.yandex.practicum.delivery.DeliveryState;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.order.CreateNewOrderRequest;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.OrderState;
import ru.yandex.practicum.order.ProductReturnRequest;
import ru.yandex.practicum.order.exception.NoOrderFoundException;
import ru.yandex.practicum.payment.PaymentDto;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.warehouse.BookedProductsDto;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WarehouseRestController warehouseClient;
    private final ShoppingCartRestController shoppingCartClient;
    private final PaymentRestController paymentClient;
    private final DeliveryRestController deliveryClient;

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> findAllOrders(String username) {
        validateUsername(username);
        log.info("Запрашиваем список всех заказов пользователя {}", username);
        List<Order> orders = orderRepository.findAllByUsername(username);
        log.debug("Получили из DB список заказов размером {}", orders.size());
        return orders.stream().
                map(OrderMapper::toDto).
                toList();
    }

    @Override
    public OrderDto addNewOrder(CreateNewOrderRequest request) {
        log.info("Создаем новый заказ: shoppingCartId {}, products {}", request.getShoppingCart().getShoppingCartId(), request.getShoppingCart().getProducts());
        BookedProductsDto bookedProductsDto;
        try {
            bookedProductsDto = warehouseClient.checkProductQuantityEnoughForShoppingCart(request.getShoppingCart());
            log.info("Проверили наличие товаров на складе, параметры заказа: {}", bookedProductsDto);
        } catch (FeignException e) {
            if (e.status() == 400) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException(e.getMessage());
            } else if (e.status() == 404) {
                throw new NoSpecifiedProductInWarehouseException(e.getMessage());
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }

        String username;
        try {
            username = shoppingCartClient.getUsernameById(request.getShoppingCart().getShoppingCartId());
            log.info("Нашли имя пользователя {}", username);
        } catch (FeignException e) {
            if (e.status() == 400) {
                throw new NoCartException(e.getMessage());
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
        Order newOrder = OrderMapper.toEntity(request, bookedProductsDto, username);
        newOrder = orderRepository.save(newOrder);

        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setFromAddress(warehouseClient.getWarehouseAddress());
        deliveryDto.setToAddress(request.getDeliveryAddress());
        deliveryDto.setOrderId(newOrder.getOrderId());
        deliveryDto.setDeliveryState(DeliveryState.CREATED);

        DeliveryDto newDeliveryDto = deliveryClient.planDelivery(deliveryDto);

        newOrder.setDeliveryId(newDeliveryDto.getDeliveryId());
        newOrder = orderRepository.save(newOrder);
        log.info("Сохранили новый заказ в БД: {}", newOrder);
        return OrderMapper.toDto(newOrder);
    }

    @Override
    public OrderDto orderCalculateDelivery(UUID orderId) {
        log.info("Обрабатываем вычисление стоимости доставки заказа OrderId: {}", orderId);
        Order orderToCalculate = getOrderById(orderId);
        BigDecimal productCost = paymentClient.productCost(OrderMapper.toDto(orderToCalculate));
        orderToCalculate.setProductPrice(productCost);
        BigDecimal deliveryCost = deliveryClient.deliveryCost(OrderMapper.toDto(orderToCalculate));
        orderToCalculate.setDeliveryPrice(deliveryCost);

        orderToCalculate = orderRepository.save(orderToCalculate);
        log.info("Сохранили изменения в БД: {}", orderToCalculate);
        return OrderMapper.toDto(orderToCalculate);
    }

    @Override
    public OrderDto orderCalculateTotal(UUID orderId) {
        log.info("Обрабатываем вычисление общей стоимости заказа OrderId: {}", orderId);
        Order orderToCalculate = getOrderById(orderId);
        BigDecimal totalCost = paymentClient.getTotalCost(OrderMapper.toDto(orderToCalculate));
        orderToCalculate.setTotalPrice(totalCost);

        PaymentDto paymentDto =  paymentClient.payment(OrderMapper.toDto(orderToCalculate));
        orderToCalculate.setPaymentId(paymentDto.getPaymentId());

        orderToCalculate = orderRepository.save(orderToCalculate);
        log.info("Сохранили изменения в БД: {}", orderToCalculate);
        return OrderMapper.toDto(orderToCalculate);
    }

    @Override
    public OrderDto orderPayment(UUID orderId) {
        log.info("Обрабатываем успешный платеж по заказу OrderId: {}", orderId);

        Order orderToPay = getOrderById(orderId);
        orderToPay = changeOrderStateAndSave(orderToPay, OrderState.PAID);

        warehouseClient.assemblyProductsForOrder(new AssemblyProductsForOrderRequest(orderToPay.getProducts(), orderId));

        return OrderMapper.toDto(orderToPay);
    }

    @Override
    public OrderDto orderFailedPayment(UUID orderId) {
        log.info("Обрабатываем ошибку оплаты заказа OrderId: {}", orderId);
        Order orderToPay = getOrderById(orderId);
        orderToPay = changeOrderStateAndSave(orderToPay, OrderState.PAYMENT_FAILED);
        return OrderMapper.toDto(orderToPay);
    }

    @Override
    public OrderDto orderAssembly(UUID orderId) {
        log.info("Обрабатываем успешную сборку заказа OrderId: {}", orderId);
        Order orderToAssembly = getOrderById(orderId);
        orderToAssembly = changeOrderStateAndSave(orderToAssembly, OrderState.ASSEMBLED);
        return OrderMapper.toDto(orderToAssembly);
    }

    @Override
    public OrderDto orderFailedAssembly(UUID orderId) {
        log.info("Обрабатываем ошибку сборки по заказу OrderId: {}", orderId);
        Order orderToAssembly = getOrderById(orderId);
        orderToAssembly = changeOrderStateAndSave(orderToAssembly, OrderState.ASSEMBLY_FAILED);
        return OrderMapper.toDto(orderToAssembly);
    }

    @Override
    public OrderDto orderDelivery(UUID orderId) {
        log.info("Обрабатываем успешную доставку по заказу OrderId: {}", orderId);
        Order orderToDeliver = getOrderById(orderId);
        orderToDeliver = changeOrderStateAndSave(orderToDeliver, OrderState.DELIVERED);
        return OrderMapper.toDto(orderToDeliver);
    }

    @Override
    public OrderDto orderFailedDelivery(UUID orderId) {
        log.info("Обрабатываем ошибку доставки заказа OrderId: {}", orderId);
        Order orderToDeliver = getOrderById(orderId);
        orderToDeliver = changeOrderStateAndSave(orderToDeliver, OrderState.DELIVERY_FAILED);
        return OrderMapper.toDto(orderToDeliver);
    }

    @Override
    public OrderDto orderComplete(UUID orderId) {
        log.info("Обрабатываем завершение заказа OrderId: {}", orderId);
        Order orderToComplete = getOrderById(orderId);
        orderToComplete = changeOrderStateAndSave(orderToComplete, OrderState.COMPLETED);
        return OrderMapper.toDto(orderToComplete);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        log.info("Создан запрос на возврат заказа OrderId: {}, products: {}", request.getOrderId(), request.getProducts());
        Order orderToReturn = getOrderById(request.getOrderId());
        Map<UUID, Integer> productsToReturn = request.getProducts();
        Set<UUID> ids = productsToReturn.keySet();
        for (UUID id : ids) {
            AddProductToWarehouseRequest addProductToWarehouseRequest = new AddProductToWarehouseRequest(id, productsToReturn.get(id));
            warehouseClient.addProductToWarehouse(addProductToWarehouseRequest);
        }
        log.info("Вернули на склад товары из заказа: OrderId {}, products {}", request.getOrderId(), request.getProducts());
        orderToReturn = changeOrderStateAndSave(orderToReturn, OrderState.PRODUCT_RETURNED);
        return OrderMapper.toDto(orderToReturn);
    }

    private void validateUsername(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException(username);
        }
    }

    private Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Такого заказа нет в базе: " + orderId));
    }

    private Order changeOrderStateAndSave(Order order, OrderState orderState) {
        order.setState(orderState);
        log.info("Изменили статус заказа на {}", orderState);
        order = orderRepository.save(order);
        log.info("Сохранили изменения в БД: {}", order);
        return order;
    }
}