package com.example.demo.security;

//JWT (JSON Web Tokens) to otwarty standard (RFC 7519), który definiuje sposób wymiany danych między stronami
// w bezpieczny sposób poprzez obiekt JSON. Przesyłane informacje mogą być weryfikowane dzięki cyfrowemu podpisowi,
// który jest elementem tokenu. Na podstawie tokenu, który jest przechowywany po stronie klienta, można uzyskać dostęp
// do zasobów serwera. Najczęściej JWT jest stosowany do autoryzacji przy dostępie do API.
//JSON wen token posiada trzy kluczowe elementy:
//Header – przechowuje on informacje na temat algorytmu szyfrowania oraz typie tokena.
//Payload – dowolny przekazywany ładunek. Najczęściej są w nim przechowywane informacje na temat roli i praw użytkownika, czy też długości życia dla tokena.
//Verify – podpis cyfrowy, który składa się z zaszyfrowanego Headera i Paylodu. Stanowi on sumę kontrolną.
//Token, który trafia do serwera od klienta jest parsowany, a następnie weryfikowany pod kontem prawidłowości, uprawnień, ważności, TTL i innych.

import com.example.demo.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.demo.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.demo.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    //Powyższa klasa narzędzi zostanie użyta do wygenerowania JWT po pomyślnym zalogowaniu użytkownika
    // i sprawdzania poprawności JWT wysłanego w nagłówku autoryzacji żądań.

    //Tworzenie Tokena:
    public String generateToken(Authentication authentication) {

        //Pobranie danych o aktualnie używającym stronę użytkowniku:
        User user = (User) authentication.getPrincipal();
        //Pobranie bieżącej daty:
        Date now = new Date(System.currentTimeMillis());

        //Określenie daty ważności dla utworzonego Tokena:
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        //Zamiana id użytkownika z typu Long na String:
        String userId = Long.toString(user.getId());

        //Deklaracja mapy zawierającej payload JWT:
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", Long.toString(user.getId()));
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullname());

        //Budowa zwracanego Tokena:
        return Jwts.builder()
                .setSubject(userId) //id uzytkownika
                .setClaims(claims)
                .setIssuedAt(now) //data teraźniejsza
                .setExpiration(expiryDate) //data ważności
                .signWith(SignatureAlgorithm.HS512, SECRET) //Hashowanie klucza i zamienienie do ciągu tekstowego
                .compact();
    }

    //Sprawdzenie poprawności tokena użytkownika (walidacja tokena):
    public boolean validateToken (String token) {
        //Próba uwierzytelnienia:
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException e) {
            //jeżeli sygnatura utworzonego klucza jest niepoprawna
            System.out.println("JWT signature is invalid");
        }
        catch (MalformedJwtException ex){
            System.out.println("Invalid JWT Token");
        }catch (ExpiredJwtException ex){
            System.out.println("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            System.out.println("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    //Pobranie id użytkownika przez token:
    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }
}
