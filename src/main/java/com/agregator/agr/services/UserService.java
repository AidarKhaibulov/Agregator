package com.agregator.agr.services;

import com.agregator.agr.dto.RegistrationDto;
import com.agregator.agr.models.UserEntity;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
}
