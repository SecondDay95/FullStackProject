package com.example.demo.security;

//Tutaj dokonujemy autentykacji użytkownika (sprawdzamy czy użytkownik próbujący wyświetlić stronę jest dostępny w systemie).
//Musimy więc pobrać token z żądania, zweryfikować go, załadować użytkownika powiązanego z tokenem i przekazać do SpringSecurity.

import com.example.demo.domain.User;
import com.example.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.example.demo.security.SecurityConstants.HEADER_STRING;
import static com.example.demo.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //Rozszerzenie klasy Jwt po klasie OncePer.. gwarantuje że pobranie tokena wykonywane jest 1 dla każdego żądania.

    //Wstrzykiwanie zależności JwtTokenProvider tworzącej token:
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    //Wstrzykiwanie zależności CustomUserDetailsService przechowującej dane użytkownika:
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    //Metoda umożliwiająca pobranie tokena z żądania:
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        //Próba pobranie tokena JWT z żądania:
        try {
            //Pobranie tokena z żądania
            String jwt = getJwtFromRequest(httpServletRequest);
            //Sprawdzenie czy token zawiera tekst i jest odpowiednio uwierzytelniony
            if(StringUtils.hasText(jwt)&& jwtTokenProvider.validateToken(jwt)){
                //Pobranie id z tokena
                Long userId = jwtTokenProvider.getUserIdFromJwt(jwt);
                //pobranie id z obiektu uzytkownika
                User userDetails = customUserDetailsService.loadUserById(userId);

                //Autentykacja poprzez nazwę i hasło użytkownika
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList());

                //Ustawienie uwierzytelniania w kontekście Spring
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

        }catch (Exception ex){
            logger.error("Could not set user authentication in security context", ex);
        }


        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    //Metoda pobierająca jwt z żądania:
    private String getJwtFromRequest(HttpServletRequest request){
        //Pobranie headera(nagłówka) z jwt:
        String bearerToken = request.getHeader(HEADER_STRING);

        //Sprawdzenie czy jwt zawiera nagłówek i zaczyna się odpowiednio
        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith(TOKEN_PREFIX)){
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }
}
