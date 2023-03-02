package com.agregator.agr.repositories;

import com.agregator.agr.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p  where p.title like concat('%',:query,'%')")
    List<Product> searchProducts(String query);

    @Query("select p from Product p join Category c on c.id = p.category.id where c.name like concat('%',:query,'%')")
    List<Product> searchProductsByCategory(String query);

    @Query(value = "SELECT * FROM products p ORDER BY p.id LIMIT ?2 OFFSET ?1",nativeQuery = true)
    List<Product> findCertainNumberOfProductsStartFrom(int start, int end);

}
