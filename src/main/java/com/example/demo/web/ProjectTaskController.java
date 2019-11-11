package com.example.demo.web;

import com.example.demo.domain.ProjectTask;
import com.example.demo.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@CrossOrigin
public class ProjectTaskController {

    @Autowired
    ProjectTaskService projectTaskService;

    //Obsługa metody POST umożliwiającej dodanie projektu do tablicy:
    @PostMapping("")
    public ResponseEntity<?> addProjectTaskToBoard(@Valid @RequestBody ProjectTask projectTask, BindingResult result) {
        //Interfejs BindingResult umożliwia filtrowanie rezultatu walidacji (@Valid), czyli raportu o błędzie w miejscu
        //jego wystąpienia. Klasa ResponseEntity jest przeznaczona do prezentacji odpowiedzi HTTP. Można dzieki niej
        //kontrolować wszystko co dotyczy odpowiedzi HTTP: status code, header, body.

        //Jeżeli podczas mapowania metody Post wystąpi błąd:
        if (result.hasErrors()) {
            //to wyświetlamy listę błędów:
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        ProjectTask newProjectTask = projectTaskService.saveOrUpdateProjectTask(projectTask);

        return new ResponseEntity<ProjectTask>(newProjectTask, HttpStatus.CREATED);
    }

    //Dodanie endpointu do pobierania wszystkich danych z serwera (bazy mysql):
    @GetMapping("/all")
    public Iterable<ProjectTask> getAllProjectTasks() {
        return projectTaskService.findAll();
    }

    //Odwołanie się do konkretnego projektu po numerze Id:
    @GetMapping("{pt_id}")
    public ResponseEntity<?> getProjectTaskById(@PathVariable Long pt_id) {
        //Adnotacja @PathVariable umożliwia wydobycie parametru z adresu URL
        ProjectTask projectTask = projectTaskService.findById(pt_id);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    //Metoda usuwająca projekt po id
    @DeleteMapping("{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable Long pt_id) {
        projectTaskService.delete(pt_id);
        return new ResponseEntity<String>("Project task deleted", HttpStatus.OK);
    }


}
