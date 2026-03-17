package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartController implements ShoppingCartRestController {
    private final ShoppingCartService cartService;

    @Override
    public ShoppingCartDto findCartByUsername(String username) {
        return cartService.findCartByUsername(username);
    }

    @Override
    public ShoppingCartDto addProductInCart(String username, Map<UUID, Integer> newProducts) {
        log.info("Обращение к cart");
        return cartService.addProductInCart(username, newProducts);
    }

    @Override
    public void deactivateCart(String username) {
        cartService.deactivateCart(username);
    }

    @Override
    public ShoppingCartDto removeProductFromCart(String username, List<UUID> removedProducts) {
        return cartService.removeProductFromCart(username, removedProducts);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        return cartService.changeProductQuantity(username, request);
    }
}
