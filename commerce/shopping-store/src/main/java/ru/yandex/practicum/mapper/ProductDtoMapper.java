package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.store.ProductDto;

@UtilityClass
public class ProductDtoMapper {
    public static Product toModel(ProductDto product) {
        return Product.builder()
                .id(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .quantityState(product.getQuantityState())
                .price(product.getPrice())
                .build();
    }

    public static ProductDto toDto(Product product) {
        return ProductDto.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .productCategory(product.getProductCategory())
                .productState(product.getProductState())
                .quantityState(product.getQuantityState())
                .price(product.getPrice())
                .build();
    }
}
