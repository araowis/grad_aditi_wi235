package com.example.spring.student_portal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring.student_portal.models.Student;
import com.example.spring.student_portal.repositories.postgres.PostgresStudentRepository;
import com.example.spring.student_portal.repositories.h2.H2StudentRepository;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private PostgresStudentRepository postgresRepo;

    @Autowired
    private H2StudentRepository h2Repo;

    @Transactional
    public void saveToTwoTables(Student student) {
        postgresRepo.save(new Student(student.getRollNo(), student.getName(), student.getStandard(), student.getSchoolName()));
        h2Repo.save(new Student(student.getRollNo(), student.getName(), student.getStandard(), student.getSchoolName()));
    }

    public Student getStudentById(int id) {
        Optional<Student> student = postgresRepo.findById(id);
        return student.orElse(null);
    }

    @Transactional
    public void updateStudent(Student student) {
        postgresRepo.save(student);
        h2Repo.save(student);
    }
}
