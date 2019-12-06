package com.example.demo.security;

//Spring Security to narzędzie, które pomaga uporządkować kwestie związane z uwierzytelnieniem i autoryzacją.
// Generalnie robi to wszystko za nas. Jednak jedynym z jego minusów jest to, że nie do końca jest przystosowane do
// pracy z usługami restowymi z obsługą jsona. Dlatego konieczne jest dodanie klasy konfiguracyjnej SpringSecurity.

import com.example.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.demo.security.SecurityConstants.H2_URL;
import static com.example.demo.security.SecurityConstants.SIGN_UP_URLS;

//Adnotacja @Configuration oznacza że klasa zostanie wykorzystana do utworzenia obiektów bean w kodzie
//Adnotacja @WebSecurity jest główną adnotacją SpringSecurity i  umożliwia wykorzystanie web security w projekcie.
//Adnotacja @EnableGlobalMethodSecurity służy do określenia dostępu do poszczególnych metod (np. user, admin, viewer, itp.).
//Zawiera 3 następujące parametry: 1.securedEnabled - umożliwiającą korzystanie z adnotacji @Secured, która pozwala na określeniu
//dostępu do poszczególnych metod na podstawie roli (np. admin, user, itp).
//2. jsr250Enabled - pozwala na korzystanie z adnotacji @RoleAllowed, która działa podobnie jak @Secured.
//3. prePostEnabled - pozwala na korzystanie z adnotacji @PreAuthorize i @PostAuthorize. Adnotacja @PreAuthorize sprawdza
//przekazane do niej wyrażenie przed wykonaniem metody przed którą się znajduje, a @PostAuthorize po wykonaniu metody.
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //Klasa ta rozszerza klasę WebSecurityConfigurerAdapter, który zapewnia domyślną konfiurację zabezpieczeń
    // i pozwala innym klasom na jej rozszerzenie i dostosowanie konfiguracji zabezpieczeń poprzez nadpisanie jej metod.

    //Wstrzykiwanie obiektu CustomUserDetailsService, który zawiera dane użytkowników oraz którego SpringSecurity używa
    //do przeprowadzania różnych uwierzytelnień i sprawdzeń poprawności opartych na rolach bo dziedziczy po UserDetails
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    //Wstrzykiwanie obiektu JwtAuthenticationEntryPoint, który umożliwia zgłoszenie wyjątku z powodu nieuwierzytelnionego
    //użytkownika próbującego uzyskać dostęp do zasobu wymagającego uwierzytelnienia. W takim przypadku po prostu odpowiemy
    // błędem 401 zawierającym komunikat o wyjątku.
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    //Utworzenie obiektu bean JwtAuthenticationFilter pobierającego i sprawdzającego jwt pobrany z żądania:
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    };

    //Obiekt umożliwiający hashowanie hasła:
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //Wstrzykiwanie obiektu i konfiguracja menedżera autentykacji, który służy do uwierzytelniania użytkownika.
    //AuthenticationManagerBuilder służy do tworzenia instancji AuthenticationManager, która jest głównym interfejsem
    //Spring Security do uwierzytelniania użytkownika.
    //Za pomocą AuthenticationManagerBuilder można zbudować uwierzytelnianie w pamięci, uwierzytelnianie LDAP,
    //uwierzytelnianie JDBC lub dodać niestandardowego dostawcę uwierzytelniania.
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //Tu odbywa się konfiguracja HttpSecurity, która pozwala na konfigurowania funkcji bezpieczeństwa, takich jak csrf,
    // sessionManagement i dodawania reguł w celu ochrony zasobów na podstawie różnych warunków.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().sameOrigin() //To enable H2 Database
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers(SIGN_UP_URLS).permitAll()
                .antMatchers("/api/users/login").permitAll()
                .antMatchers(HttpMethod.GET, SIGN_UP_URLS).permitAll()
                .antMatchers(H2_URL).permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
