package ru.yandex.practicum.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartRestController {
    @GetMapping
    ShoppingCartDto findCartByUsername(@RequestParam(name = "username") String username);

    @PutMapping
    ShoppingCartDto addProductInCart(@RequestParam(name = "username") String username,
                                             @RequestBody Map<UUID, Integer> products);

    @DeleteMapping
    void deactivateCart(@RequestParam(name = "username") String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProductFromCart(@RequestParam(name = "username") String username,
                                           @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam(name = "username") String username,
                                          @RequestBody ChangeProductQuantityRequest request);

    @GetMapping("/name")
    String getUsernameById(@RequestParam UUID cartId);
}
