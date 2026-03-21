package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.api.OrderRestController;
import ru.yandex.practicum.api.WarehouseRestController;
import ru.yandex.practicum.delivery.DeliveryDto;
import ru.yandex.practicum.delivery.DeliveryState;
import ru.yandex.practicum.delivery.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.warehouse.ShippedToDeliveryRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final WarehouseRestController warehouseClient;
    private final OrderRestController  orderClient;

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(5.0);
    private static final BigDecimal WAREHOUSE_1_ADDRESS_MULTIPLIER = BigDecimal.valueOf(1);
    private static final BigDecimal WAREHOUSE_2_ADDRESS_MULTIPLIER = BigDecimal.valueOf(2);
    private static final BigDecimal FRAGILE_MULTIPLIER = BigDecimal.valueOf(0.2);
    private static final BigDecimal WEIGHT_MULTIPLIER = BigDecimal.valueOf(0.3);
    private static final BigDecimal VOLUME_MULTIPLIER = BigDecimal.valueOf(0.2);
    private static final BigDecimal STREET_MULTIPLIER = BigDecimal.valueOf(0.2);

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        log.info("Создаем новую доставку в БД: deliveryDto={}", deliveryDto);
        Delivery delivery = DeliveryMapper.toEntity(deliveryDto);
        delivery = deliveryRepository.save(delivery);
        log.info("Возвращаем доставку с присвоенным идентификатором: {}", delivery);
        return DeliveryMapper.toDto(delivery);
    }

    @Override
    public BigDecimal deliveryCost(OrderDto orderDto) {
        log.info("Рассчитываем стоимость доставки");
        Delivery delivery = deliveryRepository.findById(orderDto.getDeliveryId())
                .orElseThrow(() -> new NoDeliveryFoundException
                        ("Такой доставки не найдено: deliveryId = " + orderDto.getDeliveryId()));
        Address warehouseAddress = delivery.getFromAddress();
        Address destinationAddress = delivery.getToAddress();
        BigDecimal totalCost = BASE_RATE;
        totalCost = warehouseAddress.getCity().equals("ADDRESS_1") ?
                totalCost.add(totalCost.multiply(WAREHOUSE_1_ADDRESS_MULTIPLIER)) :
                totalCost.add(totalCost.multiply(WAREHOUSE_2_ADDRESS_MULTIPLIER));
        totalCost = orderDto.isFragile() ? totalCost.add(totalCost.multiply(FRAGILE_MULTIPLIER)) : totalCost;
        totalCost = totalCost.add(BigDecimal.valueOf(orderDto.getDeliveryWeight()).multiply(WEIGHT_MULTIPLIER));
        totalCost = totalCost.add(BigDecimal.valueOf(orderDto.getDeliveryVolume()).multiply(VOLUME_MULTIPLIER));
        totalCost = warehouseAddress.getStreet().equals(destinationAddress.getStreet()) ?
                totalCost : totalCost.add(totalCost.multiply(STREET_MULTIPLIER));
        log.info("Возвращаем стоимость доставки: {}", totalCost);
        return totalCost;
    }

    @Override
    public void deliveryPicked(UUID orderId) {
        log.info("Передаем товар в доставку: orderId={}", orderId);
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException
                        ("Доставки для такого заказа не найдено: orderId = " + orderId));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        delivery = deliveryRepository.save(delivery);
        orderClient.orderAssembly(orderId);
        warehouseClient.shippedToDelivery(new ShippedToDeliveryRequest(orderId, delivery.getDeliveryId()));
        log.info("Товар передан в доставку: orderId={}", orderId);
    }

    @Override
    public void deliverySuccessful(UUID orderId) {
        log.info("Проставить признак успешной доставки товара: orderId={}", orderId);
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException
                        ("Доставки для такого заказа не найдено: orderId = " + orderId));
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.orderDelivery(orderId);
        log.info("Успешная доставка товара: orderId={}", orderId);
    }

    @Override
    public void deliveryFailed(UUID orderId) {
        log.info("Проставить признак неудачной доставки товара: orderId={}", orderId);
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException
                        ("Доставки для такого заказа не найдено: orderId = " + orderId));
        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        orderClient.orderFailedDelivery(orderId);
        log.info("Неудачная доставка товара: orderId={}", orderId);
    }
}