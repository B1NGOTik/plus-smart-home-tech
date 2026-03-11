package ru.yandex.practicum.warehouse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewProductInWarehouseRequest {
    UUID productId;
    Boolean fragile;
    DimensionDto dimension;
    Double weight;
}
