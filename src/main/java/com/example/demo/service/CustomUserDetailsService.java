package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //Aby uwierzytelnić użytkownika lub wykonać różne kontrole oparte na rolach, zabezpieczenia Spring muszą jakoś załadowac
    //dane użytkowników. W tym celu implementuje się interfejsu o nazwie UserDetailsService, który ma pojedynczą metodę,
    // która ładuje użytkownika na podstawie nazwy użytkownika loadUserByUsername().


    //Aby uwierzytelnić użytkownika lub wykonać różne kontrole oparte na rolach, zabezpieczenia Spring muszą jakoś załadowac
    //dane użytkowników. W tym celu implementuje się interfejsu o nazwie UserDetailsService, który ma pojedynczą metodę,
    // która ładuje użytkownika na podstawie nazwy użytkownika loadUserByUsername().


    //Aby uwierzytelnić użytkownika lub wykonać różne kontrole oparte na rolach, zabezpieczenia Spring muszą jakoś załadowac
    //dane użytkowników. W tym celu implementuje się interfejsu o nazwie UserDetailsService, który ma pojedynczą metodę,
    // która ładuje użytkownika na podstawie nazwy użytkownika loadUserByUsername().
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //ładowanie użytkownika:
        User user = userRepository.findByUsername(username);
        if (user == null) {
            //Utworzenie wyjątku gdy nie znaleziono użytkownika
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    //Deklaracja transakcji (zbioru operacji wpływających na stan systemu) to adnotacja @Transactional:
    @Transactional
    public User loadUserById(Long id) {
        //Metoda ta pozwala pobrać użytkownika po ID
        User user = userRepository.getById(id);
        if (user == null)
        {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

}
