package com.agregator.agr.services;

import com.agregator.agr.models.Cart;
import com.agregator.agr.models.RecentlyWatchedProduct;
import com.agregator.agr.models.UserEntity;

public interface RecentlyWatchedProductService {
    RecentlyWatchedProduct saveCart(RecentlyWatchedProduct cart);

    RecentlyWatchedProduct findCartById(long cartId);

    RecentlyWatchedProduct findCartByUser(UserEntity user);

    void updateCart(RecentlyWatchedProduct currentCart);
}
