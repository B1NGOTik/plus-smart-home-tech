package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.payment.PaymentDto;

import java.math.BigDecimal;

@UtilityClass
public class PaymentMapper {
    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .deliveryTotal(payment.getDeliveryTotal())
                .totalPayment(payment.getTotalPayment())
                .feeTotal(payment.getFeeTotal())
                .build();
    }

    public static Payment toEntity(OrderDto order, BigDecimal feeTotal) {
        return Payment.builder()
                .paymentId(order.getPaymentId())
                .feeTotal(feeTotal)
                .totalPayment(order.getTotalPrice())
                .deliveryTotal(order.getDeliveryPrice())
                .productTotal(order.getProductPrice())
                .orderId(order.getOrderId())
                .build();
    }
}
