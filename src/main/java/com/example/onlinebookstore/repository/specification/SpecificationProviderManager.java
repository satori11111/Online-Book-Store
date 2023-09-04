package com.example.onlinebookstore.repository.specification;

import com.example.onlinebookstore.repository.specification.SpecificationProvider;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String key);
}
