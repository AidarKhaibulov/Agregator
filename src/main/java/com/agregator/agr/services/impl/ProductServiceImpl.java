package com.agregator.agr.services.impl;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Product;
import com.agregator.agr.repositories.ProductRepository;
import com.agregator.agr.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository=productRepository;
    }
    @Override
    public List<ProductDto> findAllProducts() {
        List<Product> products=productRepository.findAll();
        return products.stream().map((product) -> mapToProductDto(product)).collect(Collectors.toList());
    }

    private ProductDto mapToProductDto(Product product) {
        ProductDto productDto=ProductDto.builder()
                .title(product.getTitle())
                .build();
        return productDto;
    }
}
