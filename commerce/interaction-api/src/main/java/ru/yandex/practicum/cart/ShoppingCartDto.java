package ru.yandex.practicum.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {
    UUID shoppingCartId;
    Map<UUID, Integer> products;
}
