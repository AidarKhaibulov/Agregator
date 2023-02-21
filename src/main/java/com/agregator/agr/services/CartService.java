package com.agregator.agr.services;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Cart;
import com.agregator.agr.models.Product;
import com.agregator.agr.models.UserEntity;

import java.util.List;

public interface CartService {

    Cart saveCart(Cart cart);

    Cart findCartById(long cartId);

    Cart findCartByUser(UserEntity user);
}
