package ru.yandex.practicum.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeProductQuantityRequest {
    UUID productId;
    Integer newQuantity;
}
