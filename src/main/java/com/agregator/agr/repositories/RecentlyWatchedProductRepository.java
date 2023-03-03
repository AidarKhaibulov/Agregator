package com.agregator.agr.repositories;

import com.agregator.agr.models.Cart;
import com.agregator.agr.models.RecentlyWatchedProduct;
import com.agregator.agr.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentlyWatchedProductRepository extends JpaRepository<RecentlyWatchedProduct,Long> {
    RecentlyWatchedProduct findByUser(UserEntity user);

}
