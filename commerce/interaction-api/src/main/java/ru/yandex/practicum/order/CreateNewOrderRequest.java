package ru.yandex.practicum.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.warehouse.AddressDto;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateNewOrderRequest {
    ShoppingCartDto shoppingCart;
    AddressDto deliveryAddress;
}
