package com.example.onlinebookstore.mapper;

import com.example.onlinebookstore.config.MapperConfig;
import com.example.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCart toModel(CreateCartItemRequestDto requestDto);

    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    default Book map(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }

}
