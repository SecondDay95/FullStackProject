package com.example.demo.service;

//Klasa ta odpowiada za wykonywanie logiki biznesowej aplikacji, która nie będzie zawarta w kontrolerze (żeby nie był
//zbyt rozległy). Będzie to klasa środkowa między repozytorium, a kontrolerem.

import com.example.demo.domain.ProjectTask;
import com.example.demo.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Tworzenie obiektu sevice tej klasy
@Service
public class ProjectTaskService {

    //Wstrzykiwanie zależności projectTaskRepository do obiektu klasy ProjectTaskService
    @Autowired
    ProjectTaskRepository projectTaskRepository;

    //Metoda umożliwiająca dodanie projektu do bazy mysql
    public ProjectTask saveOrUpdateProjectTask (ProjectTask projectTask) {

        if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
            //Ustawienie statusu projektu, jeżeli status projektu nie zostanie ustawiony
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

    //Metoda umożliwiająca odczyt danych z bazy mysql:
    public Iterable<ProjectTask> findAll() {
        return projectTaskRepository.findAll();
    }

    public ProjectTask findById(Long id) {
        return projectTaskRepository.getById(id);
    }
}
