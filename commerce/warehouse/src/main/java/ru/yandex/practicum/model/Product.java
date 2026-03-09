package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products", schema = "warehouse")
public class Product {
    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    UUID productId;

    @Column(name = "fragile")
    Boolean fragile;

    @Column(name = "width")
    Double width;

    @Column(name = "height")
    Double height;

    @Column(name = "depth")
    Double depth;

    @Column(name = "weight")
    Double weight;

    @Column(name = "quantity")
    Long quantity;
}
