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
        //jego wystąpienia.

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

}
