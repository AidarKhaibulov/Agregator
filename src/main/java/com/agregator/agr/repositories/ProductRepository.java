package com.agregator.agr.repositories;

import com.agregator.agr.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from Product p  where p.title like concat('%',:query,'%')")
    List<Product> searchProducts(String query);

    //TODO: rewrite sql query in order to find by categories
@Query("select p from Product p join Category c on c.id = p.category.id where c.name like concat('%',:query,'%')")
    List<Product> searchProductsByCategory(String query);
}
