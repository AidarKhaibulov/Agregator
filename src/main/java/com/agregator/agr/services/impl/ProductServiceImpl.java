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

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public ProductDto findProductById(long productId) {
         Product product=productRepository.findById(productId).get();
         return mapToProductDto(product);
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        Product product=mapToProduct(productDto);
        productRepository.save(product);
    }

    private Product mapToProduct(ProductDto product){
        Product productDto=Product.builder()
                .id(product.getId())
                .title(product.getTitle())
                .photoUrl(product.getPhotoUrl())
                .platform(product.getPlatform())
                .price(product.getPrice())
                .size(product.getSize())
                .createdOn(product.getCreatedOn())
                .updatedOn(product.getUpdatedOn())
                .build();
        return productDto;
    }

    private ProductDto mapToProductDto(Product product) {
        ProductDto productDto=ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .build();
        return productDto;
    }
}
