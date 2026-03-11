package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.service.ProductService;
import ru.yandex.practicum.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.warehouse.AddressDto;
import ru.yandex.practicum.warehouse.BookedProductsDto;
import ru.yandex.practicum.warehouse.NewProductInWarehouseRequest;

@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
@RestController
public class WarehouseController implements WarehouseRestController{
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
    public AddressDto getWarehouseAddress() {
        return productService.giveAddress();
    }
}
