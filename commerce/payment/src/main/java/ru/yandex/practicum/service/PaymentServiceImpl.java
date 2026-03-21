package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.api.OrderRestController;
import ru.yandex.practicum.api.ShoppingStoreRestController;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.exception.NoOrderFoundException;
import ru.yandex.practicum.payment.PaymentState;
import ru.yandex.practicum.payment.exception.NoPaymentFoundException;
import ru.yandex.practicum.payment.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.repository.PaymentRepository;


import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRestController orderClient;
    private final ShoppingStoreRestController shoppingStoreClient;

    private static final BigDecimal FEE_MULTIPLIER = BigDecimal.valueOf(0.1);

    @Transactional(readOnly = true)
    @Override
    public BigDecimal productCost(OrderDto orderDto) {
        log.info("Рассчитываем стоимость товаров в заказе: orderDto={}", orderDto);
        Map<UUID, Integer> products = orderDto.getProducts();
        if (products.isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
        }
        BigDecimal productCost = BigDecimal.valueOf(0.0);
        Set<UUID> ids = products.keySet();
        for (UUID id : ids) {
            BigDecimal price;
            try {
                price = shoppingStoreClient.findProductById(id).getPrice();
            } catch (FeignException e) {
                if (e.status() == 404) {
                    throw new ru.yandex.practicum.store.exception.ProductNotFoundException(e.getMessage());
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
            Integer quantity = products.get(id);
            productCost = productCost.add(price.multiply(BigDecimal.valueOf(quantity)));
        }
        log.info("Рассчитали стоимость товаров в заказе: {}", productCost);
        return productCost;
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalCost(OrderDto orderDto) {
        log.info("Рассчитываем полную стоимость заказа: orderDto={}", orderDto);

        BigDecimal productCost = orderDto.getProductPrice();
        BigDecimal deliveryTotal = orderDto.getDeliveryPrice();
        if (productCost == null || deliveryTotal == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
        }
        BigDecimal feeTotal = productCost.multiply(FEE_MULTIPLIER);
        BigDecimal totalCost = productCost.add(feeTotal).add(deliveryTotal);
        log.info("Рассчитали полную стоимость заказа: {}", totalCost);
        return totalCost;
    }

    @Override
    public ru.yandex.practicum.payment.PaymentDto payment(OrderDto orderDto) {
        log.info("Формируем оплату для заказа: orderDto={}", orderDto);
        BigDecimal productCost = orderDto.getProductPrice();
        BigDecimal deliveryTotal = orderDto.getDeliveryPrice();
        BigDecimal totalCost = orderDto.getTotalPrice();
        if (productCost == null || deliveryTotal == null || totalCost == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
        }
        BigDecimal feeTotal = productCost.multiply(FEE_MULTIPLIER);
        Payment payment = PaymentMapper.toEntity(orderDto, feeTotal);
        payment = paymentRepository.save(payment);
        log.info("Сохранили новую оплату в БД: {}", payment);
        return PaymentMapper.toDto(payment);
    }

    @Override
    public void paymentSuccess(UUID paymentId) {
        log.info("Эмулируем успешную оплату платежного шлюза: paymentId={}", paymentId);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("Оплата не найдена"));
        payment.setPaymentState(PaymentState.SUCCESS);
        paymentRepository.save(payment);
        log.info("Сохранили новый статус оплаты в БД: {}", payment);
        try {
            orderClient.orderPayment(payment.getOrderId());
            log.info("вызвать изменение в сервисе заказов — статус оплачен: orderId{}", payment.getOrderId());
        } catch (FeignException e) {
            if (e instanceof FeignException.NotFound) {
                throw new ru.yandex.practicum.order.exception.NoOrderFoundException(e.getMessage());
            }
        }
        log.info("Успешная оплата заказа");
    }

    @Override
    public void paymentFailed(UUID paymentId) {
        log.info("Эмулируем отказ в оплате платежного шлюза: paymentId={}", paymentId);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("Оплата не найдена"));
        payment.setPaymentState(PaymentState.FAILED);
        paymentRepository.save(payment);
        log.info("Сохранили новый статус оплаты в БД: {}", payment);
        try {
            orderClient.orderFailedPayment(payment.getOrderId());
            log.info("вызвать изменение в сервисе заказов — статус оплачен: orderId{}", payment.getOrderId());
        } catch (FeignException e) {
            if (e instanceof FeignException.NotFound) {
                throw new NoOrderFoundException(e.getMessage());
            }
        }
        log.info("Отказ в оплате заказа");
    }
}