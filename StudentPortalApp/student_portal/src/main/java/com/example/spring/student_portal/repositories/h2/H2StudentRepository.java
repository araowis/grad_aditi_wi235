package com.example.spring.student_portal.repositories.h2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring.student_portal.models.Student;

@Repository
public interface H2StudentRepository extends JpaRepository<Student, Integer> {

}
