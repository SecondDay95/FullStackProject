package com.example.demo.validator;

import com.example.demo.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Set;

//Deklarujemy nowy komponent do kontekstu Springa:
@Component
public class UserValidator implements Validator {

    //Walidator umożliwia sprawdzanie danych wejściowych (wprowadzanych przez użytkownika). Do tego celu przed polami
    //klas można używać adnotacji, ale nie zawsze są one wystarczające. W tym przypadku chcemy sprawdzić hasło wprowadzane
    //przez użytkownika, a do tego nie wystarczy adnotacja.

    @Override
    public boolean supports(Class<?> aClass) {

        //Tutaj określamy która klasa ma być walidowana (sprawdzana). Sam obiekt nie jest przekazywany do metody Support,
        //konieczne jest w niej zwrócenie klasy.
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        //Tutaj określamy co ma być sprawdzane.

        User user = (User) o;

        //Sprawdzenie czy hasło ma co najmniej 6 znaków:
        if (user.getPassword().length() < 6) {
            //Zgłoszenie błedu:
            errors.rejectValue("password", "Length", "Password must have at least 6 characters");
        }

        //Sprawdzenie czy potwierdzone hasło zgadza sie z wprowadzanym wczesniej:
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            //Zgłoszenie błędu:
            errors.rejectValue("password", "Match", "Passwords must match");
        }

    }
}
