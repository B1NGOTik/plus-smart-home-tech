package ru.yandex.practicum.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageProductDto {
    Long totalElements;
    Integer totalPages;
    Boolean first;
    Boolean last;
    Integer size;
    List<ProductDto> content;
    Integer number;
    List<SortObject> sort;
    Integer numberOfElements;
    PageableObject pageable;
    boolean empty;
}
