package com.agregator.agr.repositories;

import com.agregator.agr.models.Product;
import com.agregator.agr.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p  where p.title like concat('%',:query,'%')")
    List<Product> searchProducts(String query);

    @Query("select p from Product p join Category c on c.id = p.category.id where c.name like concat('%',:query,'%')")
    List<Product> searchProductsByCategory(String query);

    List<Product> findAllByCreatedByEquals(UserEntity user);

    Product findProductByPhotoUrl(String photoUrl);

    Product findProductByPlatform(String platform);

    // finding top 8 most popular products
    @Query(value = """
            select *
            from products
            where id in
            (SELECT product_id as id
             FROM cart_products
            GROUP BY  product_id
             ORDER BY COUNT(product_id) DESC
             LIMIT 8);
            """,nativeQuery = true)
    List<Product> findPopularProducts();
}
