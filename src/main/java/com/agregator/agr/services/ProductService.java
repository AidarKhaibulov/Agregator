package com.agregator.agr.services;

import com.agregator.agr.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAllProducts();
}
