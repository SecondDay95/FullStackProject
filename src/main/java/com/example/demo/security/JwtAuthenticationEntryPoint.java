package com.example.demo.security;

import com.example.demo.exception.InvalidLoginResponse;
import com.google.gson.Gson;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Ta klasa jest używana do zwracania nieautoryzowanego błędu 401 (wymagana autoryzacja) klientom, którzy próbują uzyskać dostęp
// do chronionego zasobu bez odpowiedniego uwierzytelnienia. Implementuje interfejs Spring Security AuthenticationEntryPoint.
//Taka funkcjonalnosc wystepuje dzieki implementacji interfejsu AuthenticationEntryPoint oraz metody commence().
//Jeżeli nastąpi próba nieautoryzowanego dostępu to W takim przypadku po prostu odpowiemy błędem 401 zawierającym komunikat o wyjątku.
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {

        //Utworzenie obiektu sprawdzającego czy dany uzytkownik jest zalogowany:
        InvalidLoginResponse loginResponse = new InvalidLoginResponse();

        //Zamiana obiektu Javy loginResponse na obiekt JSON (dzięki bibliotece Gson):
        String jsonLoginResponse = new Gson().toJson(loginResponse);

        //Ustawienie że utworzony obiekt ma być typu JSON:
        httpServletResponse.setContentType("application/json");
        //Ustawienie statusu błędu 401:
        httpServletResponse.setStatus(401);
        //Wyświetlenie odpowiedzi z serwera:
        httpServletResponse.getWriter().print(jsonLoginResponse);


    }
}
