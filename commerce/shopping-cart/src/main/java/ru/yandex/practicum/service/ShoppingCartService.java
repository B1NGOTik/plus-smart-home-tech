package ru.yandex.practicum.service;

import ru.yandex.practicum.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto findCartByUsername(String username);

    ShoppingCartDto addProductInCart(String username, Map<UUID, Integer> newProducts);

    void deactivateCart(String username);

    ShoppingCartDto removeProductFromCart(String username, List<UUID> removedProducts);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);
}
