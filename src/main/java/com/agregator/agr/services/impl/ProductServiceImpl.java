package com.agregator.agr.services.impl;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Product;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.repositories.ProductRepository;
import com.agregator.agr.repositories.UserRepository;
import com.agregator.agr.security.SecurityUtil;
import com.agregator.agr.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository){
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }
    @Override
    public List<ProductDto> findAllProducts(Pageable pageable) {
        Page<Product> products=productRepository.findAll(pageable);
        return products.stream().map(this::mapToProductDto).collect(Collectors.toList());
    }

    @Override
    public Product findProductByPhotoUrl(String photoUrl) {
        return productRepository.findProductByPhotoUrl(photoUrl);
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        Product product= mapToProduct(productDto);
        productRepository.save(product);
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
        return products.stream().map(this::mapToProductDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchProductsByCategory(String query) {
        List<Product> products=productRepository.searchProductsByCategory(query);
        return products.stream().map(this::mapToProductDto).collect(Collectors.toList());
    }
    @Override
    public Product mapToProduct(ProductDto product){
        return Product.builder()
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
    }
@Override
    public ProductDto mapToProductDto(Product product) {
    return ProductDto.builder()
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
    }

    @Override
    public Product findProductByPlatform(String platform) {
        return productRepository.findProductByPlatform(platform);
    }

}
