package com.agregator.agr.repository;

import com.agregator.agr.models.Category;
import com.agregator.agr.models.Product;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.repositories.CategoryRepository;
import com.agregator.agr.repositories.ProductRepository;
import com.agregator.agr.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void ProductRepository_SearchProducts_ReturnDesiredProductsList(){
        Product product1 = Product.builder()
                .title("Backpack 90l")
                .build();
        Product product2 = Product.builder()
                .title("Backpack 100l")
                .build();
        Product product3 = Product.builder()
                .title("Backpack 20l")
                .build();
        Product product4 = Product.builder()
                .title("Boots")
                .build();
        productRepository.saveAll(List.of(product1,product2,product3,product4));

        List<Product> foundedProducts=productRepository.searchProducts("Backpack");

        Assertions.assertThat(foundedProducts.size()).isEqualTo(3);
        assertThat(foundedProducts,not(contains(product4)));
    }

    @Test
    public void ProductRepository_searchProductByCategory_ReturnListOfProductsByDesiredCategory(){
        Category backpacksCategory =  Category.builder()
                .name("Backpacks")
                .build();
        categoryRepository.save(backpacksCategory);
        Product product1 = Product.builder()
                .title("Backpack 90l")
                .category(backpacksCategory)
                .build();
        Product product2 = Product.builder()
                .title("Backpack 20l")
                .category(backpacksCategory)
                .build();
        Product product3 = Product.builder()
                .title("Boots")
                .build();
        productRepository.saveAll(List.of(product1,product2,product3));


        List<Product> foundedProducts=productRepository.searchProductsByCategory("Backpacks");

        Assertions.assertThat(foundedProducts.size()).isEqualTo(2);
        assertThat(foundedProducts,not(contains(product3)));
    }
    @Test
    public void ProductRepository_findAllByCreatedByEquals_ReturnListOfProducts(){
        UserEntity user= UserEntity.builder()
                .username("Ivan")
                .email("ivan@gmail.com")
                .password("pass123word")
                .build();
        userRepository.save(user);
        Product product1 = Product.builder()
                .title("Backpack 90l")
                .createdBy(user)
                .build();
        Product product2 = Product.builder()
                .title("Backpack 100l")
                .createdBy(user)
                .build();
        Product product3 = Product.builder()
                .title("Backpack 20l")
                .createdBy(user)
                .build();
        Product product4 = Product.builder()
                .title("Boots")
                .build();
        productRepository.saveAll(List.of(product1,product2,product3,product4));

        List<Product> foundedProducts= productRepository.findAllByCreatedByEquals(user);

        Assertions.assertThat(foundedProducts.size()).isEqualTo(3);
        assertThat(foundedProducts,not(contains(product4)));

    }
}
