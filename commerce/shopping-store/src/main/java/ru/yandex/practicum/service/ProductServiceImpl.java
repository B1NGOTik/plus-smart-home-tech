package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mapper.PageMapper;
import ru.yandex.practicum.mapper.ProductDtoMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.product.*;
import ru.yandex.practicum.product.exception.ProductNotFoundException;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        return ProductDtoMapper.toDto(productRepository.save(ProductDtoMapper.toModel(productDto)));
    }

    @Override
    public ProductDto findProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Продукт не найден"));
        return ProductDtoMapper.toDto(product);
    }

    @Override
    public PageProductDto findProducts(ProductCategory category, Pageable pageable) {
        Page<Product> entityPage;
        if(category!=null) {
            entityPage = productRepository.findByProductCategory(category, pageable);
        } else {
            entityPage = productRepository.findAll(pageable);
        }
        List<ProductDto> dtoList = entityPage.getContent().stream()
                .map(ProductDtoMapper::toDto)
                .toList();
        return PageMapper.toPageProductDto(new PageImpl<>(dtoList, pageable, entityPage.getTotalElements()));
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        if (!productRepository.existsById(productDto.getProductId())) {
            throw new ProductNotFoundException("Продукта с ID " + productDto.getProductId() + " не найден");
        }
        Product updatedProduct = ProductDtoMapper.toModel(productDto);
        productRepository.save(updatedProduct);
        return ProductDtoMapper.toDto(updatedProduct);
    }

    @Override
    public Boolean removeProductFromStore(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Продукта с ID " + productId + " не существует"));
        if (product.getProductState().equals(ProductState.ACTIVE)) {
            product.setProductState(ProductState.DEACTIVATE);
            productRepository.save(product);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean setProductQuantityState(UUID productId, QuantityState quantityState) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Продукта с ID " + productId + " не существует"));
        if (!product.getQuantityState().equals(quantityState)) {
            product.setQuantityState(quantityState);
            productRepository.save(product);
            return true;
        } else {
            return false;
        }
    }
}
