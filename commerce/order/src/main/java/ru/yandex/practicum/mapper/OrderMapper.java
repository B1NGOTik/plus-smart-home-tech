package ru.yandex.practicum.mapper;

import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.order.CreateNewOrderRequest;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.warehouse.BookedProductsDto;

public class OrderMapper {
    public static OrderDto toDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .deliveryId(order.getDeliveryId())
                .paymentId(order.getPaymentId())
                .products(order.getProducts())
                .deliveryPrice(order.getDeliveryPrice())
                .deliveryVolume(order.getDeliveryVolume())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .fragile(order.isFragile())
                .productPrice(order.getProductPrice())
                .shoppingCartId(order.getShoppingCartId())
                .totalPrice(order.getTotalPrice())
                .build();
    }

    public static Order toEntity(CreateNewOrderRequest request, BookedProductsDto products, String username) {
        return Order.builder()
                .products(request.getShoppingCart().getProducts())
                .deliveryVolume(products.getDeliveryVolume())
                .deliveryWeight(products.getDeliveryWeight())
                .fragile(products.getFragile())
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .build();
    }
}
