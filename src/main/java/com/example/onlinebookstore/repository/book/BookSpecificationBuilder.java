package com.example.onlinebookstore.repository.book;

import com.example.onlinebookstore.dto.BookSearchParametersDto;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.repository.SpecificationBuilder;
import com.example.onlinebookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String TITLE_KEY = "title";
    private static final String AUTHOR_KEY = "author";
    private final SpecificationProviderManager<Book> providerManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        String[] authors = bookSearchParametersDto.authors();
        if (authors != null && authors.length > 0) {
            specification = specification.and(providerManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(authors));
        }
        String[] titles = bookSearchParametersDto.titles();
        if (titles != null && titles.length > 0) {
            specification = specification.and(providerManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(titles));
        }
        return specification;
    }
}
