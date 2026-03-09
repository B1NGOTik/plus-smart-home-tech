package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.warehouse.AddressDto;
import ru.yandex.practicum.warehouse.BookedProductsDto;
import ru.yandex.practicum.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final AddressDto warehouseAddress = initAddress();

    @Override
    public void addNewProductToWarehouse(NewProductInWarehouseRequest product) {
        if(productRepository.existsById(product.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException
                    ("Продукт с id " + product.getProductId() + " уже есть на складе");
        }
        log.info("Добавляем продукт dto: {}", product.getProductId());
        Product newProduct = ProductMapper.toEntity(product);
        productRepository.save(newProduct);
        log.info("Добавляем продукт: {}", newProduct.getProductId());
    }

    @Override
    public BookedProductsDto checkCart(ShoppingCartDto cart) {
        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean fragile = false;
        Map<UUID, Integer> productsInCart = cart.getProducts();
        for (UUID productId : productsInCart.keySet()) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new NoSpecifiedProductInWarehouseException("Продукта с id " + productId + "нет на складе")
                    );
            Integer neededQuantity = productsInCart.get(productId);
            if(product.getQuantity() < neededQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException("Недостаточно продуктов на складе");
            }
            double productVolume = product.getDepth() * product.getHeight() * product.getWidth();
            totalWeight += product.getWeight() * neededQuantity;
            totalVolume += productVolume * neededQuantity;
            if(product.getFragile()) {
                fragile = true;
            }
        }
        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(fragile)
                .build();
    }

    @Override
    public void increaseNumberOfProduct(AddProductToWarehouseRequest request) {
        log.info("Добавляем {} единиц продукта {}", request.getProductId(), request.getQuantity());
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException
                        ("Продукта с id " + request.getProductId() + " нет на складе"));
        Long productQuantity = product.getQuantity();
        productQuantity += request.getQuantity();
        product.setQuantity(productQuantity);
        productRepository.save(product);
    }

    @Override
    public AddressDto giveAddress() {
        return warehouseAddress;
    }

    private AddressDto initAddress() {
        final String[] addresses = new String[]{"ADDRESS_1", "ADDRESS_2"};
        final String address = addresses[Random.from(new SecureRandom()).nextInt(0, 1)];
        return AddressDto.builder()
                .city(address)
                .street(address)
                .house(address)
                .country(address)
                .flat(address)
                .build();
    }
}
