package com.agregator.agr.repository;

import com.agregator.agr.models.UserEntity;
import com.agregator.agr.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_Save_ReturnSavedUser(){
        UserEntity user = UserEntity.builder()
                .username("Ivan")
                .email("ivan@gmail.com")
                .password("pass123word")
                .build();

        UserEntity savedUser= userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void UserRepository_FindByUsername_ReturnUser(){
        UserEntity user = UserEntity.builder()
                .username("Ivan")
                .email("ivan@gmail.com")
                .password("pass123word")
                .build();

        userRepository.save(user);
        UserEntity foundedUser=userRepository.findByUsername("Ivan");

        Assertions.assertThat(foundedUser).isNotNull();
        Assertions.assertThat(foundedUser.getUsername()).isEqualTo("Ivan");
    }
    @Test
    public void UserRepository_FindByEmail_ReturnUser(){
        UserEntity user = UserEntity.builder()
                .username("Ivan")
                .email("ivan@gmail.com")
                .password("pass123word")
                .build();

        userRepository.save(user);
        UserEntity foundedUser=userRepository.findByEmail("ivan@gmail.com");

        Assertions.assertThat(foundedUser).isNotNull();
        Assertions.assertThat(foundedUser.getEmail()).isEqualTo("ivan@gmail.com");

    }
    @Test
    public void UserRepository_FindFirstByUsername_ReturnUser(){
        UserEntity user1 = UserEntity.builder()
                .username("Ivan")
                .email("ivan@gmail.com")
                .password("pass123word")
                .build();
        UserEntity user2 = UserEntity.builder()
                .username("Petr")
                .email("petr@gmail.com")
                .password("pass456word")
                .build();
        UserEntity user3 = UserEntity.builder()
                .username("Ivan")
                .email("ivan2@gmail.com")
                .password("pass789word")
                .build();


        userRepository.saveAll(List.of(user1,user2,user3));
        UserEntity foundedUser=userRepository.findFirstByUsername("Ivan");

        Assertions.assertThat(foundedUser).isNotNull();
        Assertions.assertThat(foundedUser.getEmail()).isEqualTo("ivan@gmail.com");

    }

}
