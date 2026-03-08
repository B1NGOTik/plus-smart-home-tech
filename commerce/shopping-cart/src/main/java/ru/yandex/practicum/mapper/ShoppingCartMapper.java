package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;

@UtilityClass
public class ShoppingCartMapper {
    public static ShoppingCartDto toDto(ShoppingCart cart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getCartId())
                .products(cart.getProducts())
                .build();
    }
}
