package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

//Klasę przechowującą dane użytkowników musimy rozszerzyć przez interfejs UserDetails, aby SpringSecurity mógł obsługiwać autoryzację.
//Interfejs UserDetails pozwala na dostarczanie podstawowych informacji o użytkowniku.
//Implementacje nie są używane bezpośrednio przez SpringSecurity, a po prosyu przetrzymują informację o użytkowniku, które są następnie
//umieszczane w obiektach uwierzytelniania.
//Implementacje nie są używane bezpośrednio przez Spring Security do celów bezpieczeństwa. Po prostu przechowują informacje o
// użytkowniku, które są następnie umieszczane w obiektach uwierzytelniania. Umożliwia to przechowywanie informacji o użytkownikach
// niezwiązanych z bezpieczeństwem (takich jak adresy e-mail, numery telefonów itp.) w dogodnej lokalizacji.
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Parametr andotacji unique określa ze wartość w kolumnie nie moze sie powtarzac
    @Email(message = "Username needs to be an email !")
    @NotBlank(message = "Username is required")
    @Column(unique = true)
    private String username;

   // @NotBlank(message = "Please enter your full name")
    private String fullname;

    @NotBlank(message = "Password is required")
    private String password;

    //@Transient służy aby pole w ogóle nie było pobierane z bazy danych
    @Transient
    private String confirmPassword;

    private Date create_At;
    private Date update_At;

    //Wprowadzenie zależności między użytkownikami a projektami (jeden użytkownik może przynależeć do wielu projektów).
    //Parametr cascade oznacza kaskadowanie operacji na encjach, czyli że jak wykonujemy konkretną operację na obiekcie,
    //to obiekty entity powiązane relacją też zostaną poddane tej operacji. Przypisanie REFRESH do parametru cascade oznacz
    //ponowne załadowanie obiektów z bazy danych.
    //Argument fetch określa w jaki sposób obikety są pobierane z bazy danych. Przypisanie do niego EAGER powoduje że powiązane
    //obiekty są pobierane razem z obiektem bazowym, a LAZY że obiekty nie są pobierane z obiektem bazowym a dopiero kiedy ich zażądamy.
    //Prametr mappedBy służy do określenia strony zależnej (użytkownika) od obiektu głównego (projektu).
    //Ostatnim parametrem jest OrphanRemoval, który powoduje usunięcie obiektu zależnego w momencie utracenia zależności między
    //obiektem nadrzędnym i zależnym. Jeżeli więc projekt zostanie usunięty, to obiekty użytkowników przypisanych do tego rpojektu
    //również zostaną usunięci.
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    private List<ProjectTask> projects = new ArrayList<>();

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Date getCreate_At() {
        return create_At;
    }

    public void setCreate_At(Date create_At) {
        this.create_At = create_At;
    }

    public Date getUpdate_At() {
        return update_At;
    }

    public void setUpdate_At(Date update_At) {
        this.update_At = update_At;
    }

    public List<ProjectTask> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectTask> projects) {
        this.projects = projects;
    }


    //Metody implementowane z interfejsu UserDetails. Nie dodajemy zwracanych przez nie typów do wyjsciowego obiektu JSON.

    @Override
    //Deserializacja metody zwracającej autoryzację przyznaną użytkownikowi (nie będzie ona dodawana do obiektu JSON):
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}
