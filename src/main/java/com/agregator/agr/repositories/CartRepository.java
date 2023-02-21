package com.agregator.agr.repositories;

import com.agregator.agr.models.Cart;
import com.agregator.agr.models.Category;
import com.agregator.agr.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUser(UserEntity user);
}
