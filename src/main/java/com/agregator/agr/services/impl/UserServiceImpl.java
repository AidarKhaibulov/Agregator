package com.agregator.agr.services.impl;

import com.agregator.agr.dto.RegistrationDto;
import com.agregator.agr.models.Cart;
import com.agregator.agr.models.Product;
import com.agregator.agr.models.Role;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.repositories.CartRepository;
import com.agregator.agr.repositories.RoleRepository;
import com.agregator.agr.repositories.UserRepository;
import com.agregator.agr.security.SecurityUtil;
import com.agregator.agr.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CartRepository cartRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartRepository=cartRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user= new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Role role= roleRepository.findByName("user");
        user.setRoles(Arrays.asList(role));
        Cart cart = new Cart();
        user.setCart(cart);
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
