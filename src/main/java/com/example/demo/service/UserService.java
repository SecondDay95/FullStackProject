package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UsernameAlreadyExistsException;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Adnotacja @Service działa tak jak adnotacja @Component (czyli wskazuje że obiekt tej klasy ma być zarządzany przez
//kontener Spring) ale również sygnalizuje że jest to klasa która dostarcza usługi (realizuje logikę biznesową aplikacji)
@Service
public class UserService {

    //Tu odbywa się wstrzykiwanie zależności obiektu klasy UserRepository za pomocą pól klasy UserService przez autowiązanie:
    @Autowired
    private UserRepository userRepository;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    PasswordEncoder getEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //Utworzenie metody umożliwiającej zapisywanie użytkowników do repozytorium:
    public User saveUser (User newUser) {

        try {

            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

            //Tu może pojawić się wyjątek bo nazwa uzytkownika musi byc unikalna:
            newUser.setUsername(newUser.getUsername());

            newUser.setConfirmPassword("");

            //Zapisanie danych o nowym uzytkowniku do repozytorium:
            return userRepository.save(newUser);

        }
        catch (Exception e) {

            //Wyrzucenie wyjatku o unikalnosci nazwy uzytkownika:
            throw new UsernameAlreadyExistsException("Username " + newUser.getUsername() + " already exists");
        }

    }

}
