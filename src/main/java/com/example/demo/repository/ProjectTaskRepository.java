package com.example.demo.repository;

import com.example.demo.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
    //Dzięki rozszerzeniu klasy przez CrudRepository, te repozytorium jest zarządzane przez Spring Data
    //Adnotacja @Repository, mówi dla Springa, że ten interfejs odpowiada za dostęp do danych
    //Rozszerzając interfejs przez CrudRepository musimy podać typ obiektu oraz typ klucza głównego (skladowej
    //z adnotacja @ID). CrudRepository umożliwia tworzenie oraz aktualizacje obiektów

    //Deklaracja metody pobierającej obiekty z bazy na bazie id:
    public ProjectTask getById(Long id);
}
