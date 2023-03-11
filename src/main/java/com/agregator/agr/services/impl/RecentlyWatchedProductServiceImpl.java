package com.agregator.agr.services.impl;

import com.agregator.agr.models.RecentlyWatchedProduct;
import com.agregator.agr.models.UserEntity;
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
        return recentlyWatchedProductRepository.findById(cartId).get();
    }

    @Override
    public RecentlyWatchedProduct findCartByUser(UserEntity user) {
        return recentlyWatchedProductRepository.findByUser(user);
    }

    @Override
    public void updateCart(RecentlyWatchedProduct currentCart) {
        recentlyWatchedProductRepository.save(currentCart);
    }
}
