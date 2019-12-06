package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//Spring Data JPA dostarcza narzędzia obsługi repozytorium danych dla Java Persistence API (JPA).
// Największą zaletą tego rozwiązania jest ułatwienie procesu zarządzania dostępem do źródeł danych.
// Mamy na przykład tabelę w bazie danych, dla której przygotowaliśmy encję w Hibernate i chcemy jej od razu używać.
// Tak po prostu. Dzięki Spring Data JPA nie musimy wymyślać koła od nowa, aby móc wykonywać najprostsze operacje
// bazodanowe w stylu CRUD. Od razu dostajemy gotowe interfejsy, które posiadają odpowiednio przygotowane podstawowe metody dostępowe.

//Adnotacja @Repository oznacza DAO (Data Access Object) w warstwie przechowywania danych(Dokladnie mowiac Spring
// tworzy obiekt i oznacza go jako repozytorium, adnotacja ta dziala jak Component). Dzięki temu Spring wie
//że ten interfejs to repozytorium i tworzy repozytorium do komunikacji między aplikacją a źródłem danych.
//Rozszerzenie utworzonego interfejsu po CrudRepository umożliwia dla SpringData realizowanie operacji CRUD, czyli takich jak:
//Create, Read, Update, Delete. W tym interfejsie deklarujemy metody, na podstawie których Spring sam tworzy zapytania
//i obsługuje zwracanie encji/danych.
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    User getById(Long id);
}
