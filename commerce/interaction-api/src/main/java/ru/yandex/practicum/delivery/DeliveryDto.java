package ru.yandex.practicum.delivery;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.warehouse.AddressDto;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DeliveryDto {
    UUID deliveryId;
    AddressDto fromAddress;
    AddressDto toAddress;
    UUID orderId;
    DeliveryState deliveryState;
}
