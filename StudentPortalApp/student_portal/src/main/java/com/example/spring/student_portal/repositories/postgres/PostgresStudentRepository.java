package com.example.spring.student_portal.repositories.postgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring.student_portal.models.Student;

import jakarta.persistence.PersistenceContext;

@Repository
@PersistenceContext
public interface PostgresStudentRepository extends JpaRepository<Student, Integer> {

}
