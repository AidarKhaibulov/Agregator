package com.agregator.agr.repositories;

import com.agregator.agr.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from Product p  where p.title like concat('%',:query,'%')")
    List<Product> searchProducts(String query);
}
