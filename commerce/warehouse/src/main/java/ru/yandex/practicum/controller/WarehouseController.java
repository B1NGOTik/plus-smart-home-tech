package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.WarehouseRestController;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.service.ProductService;
import ru.yandex.practicum.warehouse.*;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
@RestController
public class WarehouseController implements WarehouseRestController {
    private final ProductService productService;

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequest request) {
        productService.addNewProductToWarehouse(request);
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cartDto) {
        return productService.checkCart(cartDto);
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        productService.increaseNumberOfProduct(request);
    }

    @Override
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request) {
        return null;
    }

    @Override
    public void acceptReturn(Map<UUID, Integer> productsToReturn) {

    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {

    }

    @Override
    public AddressDto getWarehouseAddress() {
        return productService.giveAddress();
    }
}
