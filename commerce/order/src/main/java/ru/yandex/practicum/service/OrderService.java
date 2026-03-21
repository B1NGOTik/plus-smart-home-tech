package ru.yandex.practicum.service;

import ru.yandex.practicum.order.CreateNewOrderRequest;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    public List<OrderDto> findAllOrders(String username);

    public OrderDto addNewOrder(CreateNewOrderRequest request);

    public OrderDto returnOrder(ProductReturnRequest request);

    public OrderDto orderPayment(UUID orderId);

    public OrderDto orderFailedPayment(UUID orderId);

    public OrderDto orderDelivery(UUID orderId);

    public OrderDto orderFailedDelivery(UUID orderId);

    public OrderDto orderComplete(UUID orderId);

    public OrderDto orderCalculateTotal(UUID orderId);

    public OrderDto orderCalculateDelivery(UUID orderId);

    public OrderDto orderAssembly(UUID orderId);

    public OrderDto orderFailedAssembly(UUID orderId);
}
