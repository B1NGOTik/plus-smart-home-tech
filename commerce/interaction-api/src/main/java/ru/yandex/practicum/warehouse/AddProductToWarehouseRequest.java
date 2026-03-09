package ru.yandex.practicum.warehouse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddProductToWarehouseRequest {
    UUID productId;
    Integer quantity;
}
