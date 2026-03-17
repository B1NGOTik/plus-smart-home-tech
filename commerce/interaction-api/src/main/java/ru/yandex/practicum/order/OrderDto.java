package ru.yandex.practicum.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    UUID orderId;
    UUID shoppingCartId;
    Map<UUID, Integer> products;
    UUID paymentId;
    UUID deliveryId;
    OrderState state;
    Double deliveryWeight;
    Double deliveryVolume;
    boolean fragile;
    Double totalPrice;
    Double deliveryPrice;
    Double productPrice;
}
