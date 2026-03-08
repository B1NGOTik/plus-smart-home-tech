package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
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
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @GetMapping
    public ShoppingCartDto findCartByUsername(@RequestParam String username) {
        return cartService.findCartByUsername(username);
    }

    @PutMapping
    public ShoppingCartDto addProductInCart(@RequestParam String username,
                                            @RequestBody Map<UUID, Integer> newProducts) {
        return cartService.addProductInCart(username, newProducts);
    }

    @DeleteMapping
    public void deactivateCart(@RequestParam String username) {
        cartService.deactivateCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductFromCart(@RequestParam String username,
                                                 @RequestBody List<UUID> removedProducts) {
        return cartService.removeProductFromCart(username, removedProducts);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam String username,
                                                 @RequestBody ChangeProductQuantityRequest request) {
        return cartService.changeProductQuantity(username, request);
    }
}
