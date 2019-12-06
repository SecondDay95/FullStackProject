package com.example.demo.web;

//Adnotacja @RestController to adnotacja sprawiająca, że klasa będzie mogła wykonywać żadania REST (GET,POST,PUT,DELETE, itp).
//Użycie adnotacji @RestController jest równoważne zastosowaniu dwóch innych adnotacji @Controller (- tworzącej obiekt bean klasy oraz
//wskazaniu tej klasy jako kontroler w warstwie prezentacji) oraz @ResponseBody (- która mówi dla Springa aby odpowiedź z serwera
//zawierała to co jest zwracane przez tę klase.
//REST (ang. Representational State Transfer) jest wzorcem narzucającym dobre praktyki tworzenia architektury aplikacji rozproszonych.
// RESTful Webservices (inaczej RESTful web API) jest usługą sieciową zaimplementowaną na bazie protokołu HTTP i głównych zasad wzorca
// REST.

import com.example.demo.domain.User;
import com.example.demo.payload.JWTLoginSuccessResponse;
import com.example.demo.payload.LoginRequest;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import com.example.demo.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.example.demo.security.SecurityConstants.TOKEN_PREFIX;

@RestController
//Adnotacja @RequestMapping określa adres pod jakim ma być dostępny kotroler:
@RequestMapping("/api/users")
public class UserController {

    //Wstrzykiwanie zależności obiektu klasy UserService za pomocą pól klasy UserController przez autowiązanie:
    @Autowired
    private UserService userService;

    //Wstrzykiwanie zależności obiektu klasy UserValidator za pomocą pól klasy UserController przez autowiązanie:
    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;


    //Określenie adresu dla danych logowania:
    @CrossOrigin
    @PostMapping("/login")
    //Metoda ResponseEntity umożlwia reprezentację całej odpowiedzi HTTP z serwera: status code, header, body.
    //Konieczne jest podanie w niej ciała odpowiedzi (to po adnotacji @RequestBody) oraz można w niej tez podać
    //parametr sprawdzający poprawność wiązania, który będzie zawierał błędy które wystąpiły podczas tworzenia
    //odpowiedzi - BindingResult.
    //Adnotacja @Valid — użyta w kontrolerze przy atrybucie metody powoduje, że obiekt ten jest traktowany jako formularz
    // i uruchamiana jest jego walidacja (np. na podstawie poniższych adnotacji). Kolejny argument metody po tym musi być
    // typu BindingResult — tam Spring umieści informacje o problemach
    ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        //Jeżeli podczas mapowania metody Post wystąpi błąd:
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap1 = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap1.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<Map<String, String>>(errorMap1, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX +  tokenProvider.generateToken(authentication);

        System.out.println(jwt);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));


    }

    //Proces rejestracji:
    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {

        //Sprawdzenie poprawności hasła
        userValidator.validate(user, result);

        //Jeżeli podczas mapowania metody Post wystąpi błąd:
        if (result.hasErrors()) {
            Map<String, String> errorMap1 = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap1.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<Map<String, String>>(errorMap1, HttpStatus.BAD_REQUEST);
        }

        //Zapisanie nowego użytkownika:
        User newUser = userService.saveUser(user);

        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }


}
