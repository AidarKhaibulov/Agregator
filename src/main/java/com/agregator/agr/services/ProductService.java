package com.agregator.agr.services;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAllProducts();
    Product saveProduct(ProductDto product);

    ProductDto findProductById(long productId);

    void updateProduct(ProductDto product);

    void delete(Long productId);

    List<ProductDto> searchProducts(String query);

    List<ProductDto> searchProductsByCategory(String query);

    Product mapToProduct(ProductDto product);

    ProductDto mapToProductDto(Product product);
}
