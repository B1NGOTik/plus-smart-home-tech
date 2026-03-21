package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.delivery.DeliveryDto;
import ru.yandex.practicum.model.Delivery;

@UtilityClass
public class DeliveryMapper {
    public static DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .deliveryState(delivery.getDeliveryState())
                .fromAddress(AddressMapper.toDto(delivery.getFromAddress()))
                .toAddress(AddressMapper.toDto(delivery.getToAddress()))
                .orderId(delivery.getOrderId())
                .build();
    }

    public static Delivery toEntity(DeliveryDto dto) {
        return Delivery.builder()
                .deliveryId(dto.getDeliveryId())
                .deliveryState(dto.getDeliveryState())
                .fromAddress(AddressMapper.toEntity(dto.getFromAddress()))
                .toAddress(AddressMapper.toEntity(dto.getToAddress()))
                .orderId(dto.getOrderId())
                .build();
    }
}
