package com.agregator.agr.services.impl;

import com.agregator.agr.models.Cart;
import com.agregator.agr.models.RecentlyWatchedProduct;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.repositories.CartRepository;
import com.agregator.agr.repositories.RecentlyWatchedProductRepository;
import com.agregator.agr.services.RecentlyWatchedProductService;
import org.springframework.stereotype.Service;

@Service
public class RecentlyWatchedProductServiceImpl implements RecentlyWatchedProductService {
    RecentlyWatchedProductRepository recentlyWatchedProductRepository;
    public RecentlyWatchedProductServiceImpl(RecentlyWatchedProductRepository recentlyWatchedProductRepository) {
        this.recentlyWatchedProductRepository = recentlyWatchedProductRepository;
    }
    @Override
    public RecentlyWatchedProduct saveCart(RecentlyWatchedProduct cart) {
        return recentlyWatchedProductRepository.save(cart);
    }

    @Override
    public RecentlyWatchedProduct findCartById(long cartId) {
        RecentlyWatchedProduct cart=recentlyWatchedProductRepository.findById(cartId).get();
        return cart;
    }

    @Override
    public RecentlyWatchedProduct findCartByUser(UserEntity user) {
        RecentlyWatchedProduct cart= recentlyWatchedProductRepository.findByUser(user);
        return cart;
    }

    @Override
    public void updateCart(RecentlyWatchedProduct currentCart) {
        recentlyWatchedProductRepository.save(currentCart);
    }
}
