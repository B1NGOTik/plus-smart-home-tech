package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.product.PageProductDto;
import ru.yandex.practicum.product.ProductCategory;
import ru.yandex.practicum.product.ProductDto;
import ru.yandex.practicum.product.QuantityState;

import java.util.UUID;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto);
    ProductDto findProductById(UUID id);
    PageProductDto findProducts(ProductCategory category, Pageable pageable);
    ProductDto updateProduct(ProductDto productDto);
    Boolean removeProductFromStore(UUID productId);
    Boolean setProductQuantityState(UUID productId, QuantityState quantityState);
}
