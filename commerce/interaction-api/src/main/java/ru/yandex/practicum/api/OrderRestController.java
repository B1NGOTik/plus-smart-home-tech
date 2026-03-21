package ru.yandex.practicum.api;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.order.CreateNewOrderRequest;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderRestController {
    @GetMapping
    List<OrderDto> findAllOrders(@RequestParam String username);

    @PutMapping
    OrderDto addNewOrder(@RequestBody CreateNewOrderRequest request);

    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto orderPayment(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto orderFailedPayment(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto orderDelivery(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto orderFailedDelivery(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto orderComplete(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto orderCalculateTotal(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto orderCalculateDelivery(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto orderAssembly(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto orderFailedAssembly(@RequestBody UUID orderId);
}
