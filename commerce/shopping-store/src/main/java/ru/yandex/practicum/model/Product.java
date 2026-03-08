package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.store.ProductCategory;
import ru.yandex.practicum.store.ProductState;
import ru.yandex.practicum.store.QuantityState;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products", schema = "store")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "name")
    String productName;

    @Column(name = "description")
    String description;

    @Column(name = "image_src")
    String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state")
    QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    ProductCategory productCategory;

    @Column(name = "price")
    Double price;
}
