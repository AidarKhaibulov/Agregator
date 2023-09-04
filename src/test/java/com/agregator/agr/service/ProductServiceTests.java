package com.agregator.agr.service;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Product;
import com.agregator.agr.repositories.ProductRepository;
import com.agregator.agr.services.impl.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;
    @Test
    public void ProductService_CreateProduct(){
        Product product1 = Product.builder()
                .title("Backpack 1")
                .build();
        Product product2 = Product.builder()
                .title("Backpack 2")
                .build();
        when(productRepository.searchProducts(Mockito.any(String.class))).thenReturn(List.of(product1,product2));

        List<ProductDto> products = productService.searchProducts("Backpack");

        Assertions.assertThat(products.size()).isEqualTo(2);
    }
}
