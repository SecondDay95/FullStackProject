package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class ProjectTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project name cannot be blank")
    private String name;

    @NotBlank(message = "Project description cannot be blank")
    private String description;

    @NotBlank(message = "Employee name cannot be blank")
    private String employee;

    @NotBlank(message = "Number of hours cannot be blank")
    private String hours;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    public ProjectTask() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String summary) {
        this.name = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String acceptanceCriteria) {
        this.description = acceptanceCriteria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }
}
