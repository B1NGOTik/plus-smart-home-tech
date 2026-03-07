package ru.yandex.practicum.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.product.PageProductDto;
import ru.yandex.practicum.product.PageableObject;
import ru.yandex.practicum.product.ProductDto;
import ru.yandex.practicum.product.SortObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageMapper {

    public static PageProductDto toPageProductDto(Page<ProductDto> page) {
        if (page == null) {
            return null;
        }

        return PageProductDto.builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .size(page.getSize())
                .content(page.getContent())
                .number(page.getNumber())
                .sort(mapSortToList(page.getSort()))
                .numberOfElements(page.getNumberOfElements())
                .pageable(mapPageable(page.getPageable()))
                .empty(page.isEmpty())
                .build();
    }

    private static List<SortObject> mapSortToList(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return new ArrayList<>();
        }

        return sort.stream()
                .map(order -> SortObject.builder()
                        .direction(order.getDirection().name())
                        .property(order.getProperty())
                        .ignoreCase(order.isIgnoreCase())
                        .ascending(order.isAscending())
                        .nullHandling(order.getNullHandling() != null ? order.getNullHandling().name() : null)
                        .build())
                .collect(Collectors.toList());
    }

    private static PageableObject mapPageable(org.springframework.data.domain.Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return PageableObject.builder()
                    .paged(false)
                    .unpaged(true)
                    .sort(new ArrayList<>())
                    .offset(pageable != null ? pageable.getOffset() : null)
                    .build();
        }

        return PageableObject.builder()
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .offset(pageable.getOffset())
                .sort(mapSortToList(pageable.getSort()))
                .paged(true)
                .unpaged(false)
                .build();
    }
}