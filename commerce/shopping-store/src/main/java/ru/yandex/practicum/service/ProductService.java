package ru.yandex.practicum.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.store.PageProductDto;
import ru.yandex.practicum.store.ProductCategory;
import ru.yandex.practicum.store.ProductDto;
import ru.yandex.practicum.store.QuantityState;

import java.util.UUID;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto);
    ProductDto findProductById(UUID id);
    PageProductDto findProducts(ProductCategory category, Pageable pageable);
    ProductDto updateProduct(ProductDto productDto);
    Boolean removeProductFromStore(UUID productId);
    Boolean setProductQuantityState(UUID productId, QuantityState quantityState);
}
