package com.agregator.agr.services.impl;

import com.agregator.agr.models.Cart;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.repositories.CartRepository;
import com.agregator.agr.services.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart findCartById(long cartId) {
        return cartRepository.findById(cartId).get();
    }

    @Override
    public Cart findCartByUser(UserEntity user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public void updateCart(Cart currentCart) {
        cartRepository.save(currentCart);
    }
}
