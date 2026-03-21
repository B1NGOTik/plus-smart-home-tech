package ru.yandex.practicum.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.store.PageProductDto;
import ru.yandex.practicum.store.ProductCategory;
import ru.yandex.practicum.store.ProductDto;
import ru.yandex.practicum.store.QuantityState;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreRestController {

    @GetMapping
    PageProductDto findProducts(@RequestParam(name = "category", required = false) ProductCategory category,
                                Pageable pageable);

    @PutMapping
    ProductDto addProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("quantityState")
    boolean setProductQuantityState(@RequestParam UUID productId,
                                    @RequestParam QuantityState quantityState);

    @GetMapping("{productId}")
    ProductDto findProductById(@PathVariable UUID productId);
}
