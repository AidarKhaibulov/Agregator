package com.agregator.agr.services.impl;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Product;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.repositories.ProductRepository;
import com.agregator.agr.repositories.UserRepository;
import com.agregator.agr.security.SecurityUtil;
import com.agregator.agr.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private UserRepository userRepository;
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository){
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }
    @Override
    public List<ProductDto> findAllProducts() {
        List<Product> products=productRepository.findAll();
        return products.stream().map((product) -> mapToProductDto(product)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findCertainNumberOfProductsStartFrom(int start, int end) {
        List<Product> products=productRepository.findCertainNumberOfProductsStartFrom(start, end);
        return products.stream().map((product) -> mapToProductDto(product)).collect(Collectors.toList());
    }

    @Override
    public Product saveProduct(ProductDto productDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user= userRepository.findByUsername(username);
        Product product= mapToProduct(productDto);
        //product.setCreatedBy(user);
        return productRepository.save(product);
    }

    @Override
    public ProductDto findProductById(long productId) {
         Product product=productRepository.findById(productId).get();
         return mapToProductDto(product);
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user= userRepository.findByUsername(username);
        Product product=mapToProduct(productDto);
        product.setCreatedBy(user);
        productRepository.save(product);
    }

    @Override
    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<ProductDto> searchProducts(String query) {
        List<Product> products=productRepository.searchProducts(query);
        return products.stream().map(product -> mapToProductDto(product)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchProductsByCategory(String query) {
        List<Product> products=productRepository.searchProductsByCategory(query);
        return products.stream().map(product -> mapToProductDto(product)).collect(Collectors.toList());
    }
    @Override
    public Product mapToProduct(ProductDto product){
        Product productDto=Product.builder()
                .id(product.getId())
                .title(product.getTitle())
                .photoUrl(product.getPhotoUrl())
                .platform(product.getPlatform())
                .price(product.getPrice())
                .size(product.getSize())
                .createdBy(product.getCreatedBy())
                .createdOn(product.getCreatedOn())
                .updatedOn(product.getUpdatedOn())
                .description(product.getDescription())
                .build();
        return productDto;
    }
@Override
    public ProductDto mapToProductDto(Product product) {
        ProductDto productDto=ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .photoUrl(product.getPhotoUrl())
                .platform(product.getPlatform())
                .price(product.getPrice())
                .size(product.getSize())
                .createdBy(product.getCreatedBy())
                .createdOn(product.getCreatedOn())
                .updatedOn(product.getUpdatedOn())
                .description(product.getDescription())
                .build();
        return productDto;
    }

}
