package com.example.spring.university_portal.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring.university_portal.models.Student;

@Repository
public interface UniversityRepository extends JpaRepository<Student, Integer> {
    List<Student> findAll();
    List<Student> findBySchoolName(String schoolName);
    Optional<Student> findById(Integer id);
    long countBySchoolName(String schoolName);
    long countByStandard(Integer standard);
    List<Student> findByPercentageGreaterThanEqualOrderByPercentageDesc(Double percentage);
    List<Student> findByPercentageLessThanOrderByPercentageDesc(Double percentage);
    long countByGenderAndStandard(String gender, Integer standard);
}
