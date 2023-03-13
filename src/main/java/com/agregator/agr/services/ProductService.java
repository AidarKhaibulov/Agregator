package com.agregator.agr.services;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAllProducts(Pageable pageable);

    Product findProductByPhotoUrl(String photoUrl);

    void saveProduct(ProductDto product);

    ProductDto findProductById(long productId);

    void updateProduct(ProductDto product);

    void delete(Long productId);

    List<ProductDto> searchProducts(String query);

    List<ProductDto> searchProductsByCategory(String query);

    Product mapToProduct(ProductDto product);

    ProductDto mapToProductDto(Product product);


    Product findProductByPlatform(String platform);
}
