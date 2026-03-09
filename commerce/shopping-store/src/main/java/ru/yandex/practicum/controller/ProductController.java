package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.store.PageProductDto;
import ru.yandex.practicum.store.ProductCategory;
import ru.yandex.practicum.store.ProductDto;
import ru.yandex.practicum.store.QuantityState;
import ru.yandex.practicum.service.ProductService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ProductController implements ShoppingStoreRestController {
    private final ProductService productService;

    @Override
    public ProductDto addProduct(ProductDto product) {
        log.info("Добавляем продукт: {}", product);
        return productService.addProduct(product);
    }

    @Override
    public ProductDto findProductById(UUID productId) {
        return productService.findProductById(productId);
    }

    @Override
    public PageProductDto findProducts(ProductCategory category, Pageable pageable) {
        log.info("pageable: {}", pageable);
        return productService.findProducts(category, pageable);
    }

    @Override
    public ProductDto updateProduct(ProductDto product) {
        return productService.updateProduct(product);
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        return productService.removeProductFromStore(productId);
    }

    @Override
    public boolean setProductQuantityState(UUID productId, QuantityState quantityState) {
        return productService.setProductQuantityState(productId, quantityState);
    }
}
