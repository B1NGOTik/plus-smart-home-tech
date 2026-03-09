package ru.yandex.practicum.service;

import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.warehouse.AddressDto;
import ru.yandex.practicum.warehouse.BookedProductsDto;
import ru.yandex.practicum.warehouse.NewProductInWarehouseRequest;

public interface ProductService {
    void addNewProductToWarehouse(NewProductInWarehouseRequest product);
    BookedProductsDto checkCart(ShoppingCartDto cart);
    void increaseNumberOfProduct(AddProductToWarehouseRequest request);
    AddressDto giveAddress();
}
