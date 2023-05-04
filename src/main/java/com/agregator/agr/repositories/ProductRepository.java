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

    Product findProductByPhotoUrl(String photoUrl);

    Product findProductByPlatform(String platform);

    // finding top 5 most popular products
    @Query(value = "select *\n" +
            "    from products\n" +
            "        where id in\n" +
            "\n" +
            "(SELECT product_id as id\n" +
            " FROM cart_products\n" +
            " GROUP BY  product_id\n" +
            " ORDER BY COUNT(product_id) DESC\n" +
            " LIMIT 8);",nativeQuery = true)
    List<Product> findPopularProducts();
}
