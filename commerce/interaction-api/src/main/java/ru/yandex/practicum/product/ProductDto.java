package ru.yandex.practicum.product;

import lombok.*;

import java.util.UUID;

@Data
@Builder
public class ProductDto {
    UUID productId;
    String productName;
    String description;
    String imageSrc;
    QuantityState quantityState;
    ProductState productState;
    ProductCategory productCategory;
    Double price;
}
