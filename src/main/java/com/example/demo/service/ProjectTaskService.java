package com.example.demo.service;

//Klasa ta odpowiada za wykonywanie logiki biznesowej aplikacji, która nie będzie zawarta w kontrolerze (żeby nie był
//zbyt rozległy). Będzie to klasa środkowa między repozytorium, a kontrolerem.

import com.example.demo.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    ProjectTaskRepository projectTaskRepository;
}
