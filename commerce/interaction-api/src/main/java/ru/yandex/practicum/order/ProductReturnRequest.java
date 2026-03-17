package ru.yandex.practicum.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReturnRequest {
    UUID orderId;
    Map<UUID, Integer> products;
}
