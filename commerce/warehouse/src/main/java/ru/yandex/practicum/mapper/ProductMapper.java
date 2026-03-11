package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.warehouse.NewProductInWarehouseRequest;

@UtilityClass
public class ProductMapper {
    public static Product toEntity(NewProductInWarehouseRequest product) {
        return Product.builder()
                .productId(product.getProductId())
                .fragile(product.getFragile())
                .width(product.getDimension().getWidth())
                .depth(product.getDimension().getDepth())
                .height(product.getDimension().getHeight())
                .weight(product.getWeight())
                .quantity(0L)
                .build();
    }
}
