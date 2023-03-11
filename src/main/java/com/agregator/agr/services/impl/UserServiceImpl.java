package com.agregator.agr.services.impl;

import com.agregator.agr.dto.RegistrationDto;
import com.agregator.agr.models.Cart;
import com.agregator.agr.models.RecentlyWatchedProduct;
import com.agregator.agr.models.Role;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.repositories.RoleRepository;
import com.agregator.agr.repositories.UserRepository;
import com.agregator.agr.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user= new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Role role= roleRepository.findByName("user");
        user.setRoles(Collections.singletonList(role));
        Cart cart = new Cart();
        user.setCart(cart);
        RecentlyWatchedProduct recentlyWatchedProductCart = new RecentlyWatchedProduct();
        user.setRecentlyWatchedProduct(recentlyWatchedProductCart);
        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateUser(UserEntity userExisting) {
        userRepository.save(userExisting);
    }
}
