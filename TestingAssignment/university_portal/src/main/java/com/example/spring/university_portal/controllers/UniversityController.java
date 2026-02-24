package com.example.spring.university_portal.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.university_portal.models.Student;
import com.example.spring.university_portal.repositories.UniversityRepository;
// import com.example.spring.university_portal.services.StudentService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/students")
public class UniversityController {

    @Autowired
    private UniversityRepository repo;

    @GetMapping(value = "/greet", produces = "text/plain")
    public String hi() {
        return "HENLOOOO";
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Integer studentId) {
        return repo.findById(studentId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public String addStudent(@RequestBody Student s) {
        if (repo.existsById(s.getRegNo())) {
            return "Sorry, student already exists";
        }
        repo.save(s);
        return "Successfully created student";
    }

    @PatchMapping("/{id}")
    public String patchStudent(@PathVariable Integer id, @RequestBody Student s) {
        if (!repo.existsById(id)) {
            return "Sorry, this student does not exist in the database.";
        }
        Optional<Student> n = repo.findById(id);
        if(n.isPresent()) {
            Student nw = n.get();
            if (s.getGender() == null) {
                s.setGender(nw.getGender());
            }
            if (s.getName() == null) {
                s.setName(nw.getName());
            }
            if (s.getPercentage() == null) {
                s.setPercentage(nw.getPercentage());
            }
            if (s.getRollNo() == null) {
                s.setRollNo(nw.getRollNo());
            }
            if (s.getSchoolName() == null) {
                s.setSchoolName(nw.getSchoolName());
            }
            if (s.getStandard() == null) {
                s.setStandard(nw.getStandard());
            }
        }
        s.setRegNo(id);
        repo.save(s);
        return "Successfully updated student record.";
    }

    @PutMapping("/{id}")
    public String putStudent(@PathVariable Integer id, @RequestBody Student s) {
        if (!repo.existsById(id)) {
            return "Sorry, this student does not exist in the database.";
        }
        repo.save(s);
        return "Successfully updated student record.";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable("id") Integer id) {
        if (!repo.existsById(id)) {
            return "Sorry, this student does not exist in the database.";
        } else {
            repo.deleteById(id);
            return "Student deleted successfully.";
        }
    }

    @GetMapping("/school")
    public List<Student> getStudentsBySchool(@RequestParam("name") String schoolName) {
        return repo.findBySchoolName(schoolName);
    }

    @GetMapping("/school/count")
    public long countStudentsBySchool(@RequestParam("name") String schoolName) {
        return repo.countBySchoolName(schoolName);
    }

    @GetMapping("/standard/count")
    public long countStudentsByStandard(@RequestParam("class") Integer standard) {
        return repo.countByStandard(standard);
    }

    @GetMapping("/result")
    public List<Student> getStudentResult(@RequestParam("pass") boolean pass) {
        if (pass) {
            return repo.findByPercentageGreaterThanEqualOrderByPercentageDesc(Double.valueOf(40));
        }
        else {
            return repo.findByPercentageLessThanOrderByPercentageDesc(Double.valueOf(40));
        }
    }

    @GetMapping("/strength")
    public long getStudentsByGenderAndStandard(@RequestParam("gender") String gender, @RequestParam("standard") Integer standard) {
        return repo.countByGenderAndStandard(gender, standard);
    }
}
