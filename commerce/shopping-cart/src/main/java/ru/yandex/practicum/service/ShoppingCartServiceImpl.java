package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.cart.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService{
    private final ShoppingCartRepository cartRepository;

    @Override
    public ShoppingCartDto findCartByUsername(String username) {
        ShoppingCart cart = findOrCreateCart(username);
        return ShoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto addProductInCart(String username, Map<UUID, Integer> newProducts) {
        ShoppingCart userCart = findOrCreateCart(username);
        if(!userCart.getActive()) {
            throw new RuntimeException("Корзина деактивирована");
        }
        Map<UUID, Integer> oldProducts = userCart.getProducts();
        oldProducts.putAll(newProducts);
        userCart.setProducts(oldProducts);
        return ShoppingCartMapper.toDto(userCart);
    }

    @Override
    public void deactivateCart(String username) {
        ShoppingCart userCart = findOrCreateCart(username);
        if(!userCart.getActive()) {
            throw new RuntimeException("Корзина уже деактивирована");
        }
        userCart.setActive(false);
        cartRepository.save(userCart);
    }

    @Override
    public ShoppingCartDto removeProductFromCart(String username, List<UUID> removedProducts) {
        ShoppingCart userCart = findOrCreateCart(username);
        if(!userCart.getActive()) {
            throw new RuntimeException("Корзина деактивирована");
        }
        Map<UUID, Integer> products = userCart.getProducts();
        for (UUID product : removedProducts) {
            if(products.containsKey(product)) {
                products.remove(product);
            } else {
                throw new NoProductsInShoppingCartException("Продукта с id " + product + " нет в корзине");
            }
        }
        userCart.setProducts(products);
        return ShoppingCartMapper.toDto(cartRepository.save(userCart));
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart userCart = findOrCreateCart(username);
        if(!userCart.getActive()) {
            throw new RuntimeException("Корзина деактивирована");
        }
        Map<UUID, Integer> products = userCart.getProducts();
        if (!products.containsKey(request.getProductId())) {
            throw new NoProductsInShoppingCartException("Продукта с id " + request.getProductId() + " нет в корзине");
        }
        products.put(request.getProductId(), request.getNewQuantity());
        userCart.setProducts(products);
        return ShoppingCartMapper.toDto(cartRepository.save(userCart));
    }

    private ShoppingCart findOrCreateCart(String username) {
        return cartRepository.findByUsername(username)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUsername(username);
                    return cartRepository.save(newCart);
                });
    }
}
