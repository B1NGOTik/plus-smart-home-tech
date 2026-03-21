package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.OrderRestController;
import ru.yandex.practicum.order.CreateNewOrderRequest;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderRestController {
    private final OrderService orderService;

    @Override
    public List<OrderDto> findAllOrders(String username) {
        log.info("GET /api/v1/order - Получить заказы пользователя: username={}",
                username);
        List<OrderDto> response = orderService.findAllOrders(username);
        log.info("Возвращаем список заказов размером: {}", response.size());
        log.info("Возвращаем заказы: {}", response);
        return response;
    }

    @Override
    public OrderDto addNewOrder(CreateNewOrderRequest request) {
        log.info("PUT /api/v1/order - Создать новый заказ в системе: request={}", request);
        OrderDto response = orderService.addNewOrder(request);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto orderCalculateDelivery(UUID orderId) {
        log.info("POST /api/v1/order/calculate/delivery - Расчёт стоимости доставки заказа: orderId={}", orderId);
        OrderDto response = orderService.orderCalculateDelivery(orderId);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto orderCalculateTotal(UUID orderId) {
        log.info("POST /api/v1/order/calculate/total - Расчёт стоимости заказа: orderId={}", orderId);
        OrderDto response = orderService.orderCalculateTotal(orderId);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto orderPayment(UUID orderId) {
        log.info("POST /api/v1/order/payment - Оплата заказа: orderId={}", orderId);
        OrderDto response = orderService.orderPayment(orderId);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto orderFailedPayment(UUID orderId) {
        log.info("POST /api/v1/order/payment/failed - Оплата заказа произошла с ошибкой: orderId={}", orderId);
        OrderDto response = orderService.orderFailedPayment(orderId);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto orderAssembly(UUID orderId) {
        log.info("POST /api/v1/order/assembly - Сборка заказа: orderId={}", orderId);
        OrderDto response = orderService.orderAssembly(orderId);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto orderFailedAssembly(UUID orderId) {
        log.info("POST /api/v1/order/assembly/failed - Сборка заказа произошла с ошибкой: orderId={}", orderId);
        OrderDto response = orderService.orderFailedAssembly(orderId);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto orderDelivery(UUID orderId) {
        log.info("POST /api/v1/order/delivery - Доставка заказа: orderId={}", orderId);
        OrderDto response = orderService.orderDelivery(orderId);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto orderFailedDelivery(UUID orderId) {
        log.info("POST /api/v1/order/delivery/failed - Доставка заказа произошла с ошибкой: orderId={}", orderId);
        OrderDto response = orderService.orderFailedDelivery(orderId);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto orderComplete(UUID orderId) {
        log.info("POST /api/v1/order/completed - Завершение заказа: orderId={}", orderId);
        OrderDto response = orderService.orderComplete(orderId);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        log.info("POST /api/v1/order/return - Возврат заказа: request={}", request);
        OrderDto response = orderService.returnOrder(request);
        log.info("Возвращаем заказ: {}", response);
        return response;
    }
}