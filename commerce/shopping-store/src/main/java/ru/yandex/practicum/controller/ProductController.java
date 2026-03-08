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
public class ProductController {
    private final ProductService productService;

    @PutMapping
    public ProductDto addProduct(@RequestBody ProductDto product) {
        log.info("Добавляем продукт: {}", product);
        return productService.addProduct(product);
    }

    @GetMapping("/{productId}")
    public ProductDto findProductById(@PathVariable UUID productId) {
        return productService.findProductById(productId);
    }

    @GetMapping
    public PageProductDto findProducts(@RequestParam(required = false) ProductCategory category,
                                       Pageable pageable) {
        log.info("pageable: {}", pageable);
        return productService.findProducts(category, pageable);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto product) {
        return productService.updateProduct(product);
    }

    @PostMapping("/removeProductFromStore")
    public Boolean removeProductFromStore(@RequestBody UUID productId) {
        return productService.removeProductFromStore(productId);
    }

    @PostMapping("/quantityState")
    public Boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState) {
        return productService.setProductQuantityState(productId, quantityState);
    }
}
