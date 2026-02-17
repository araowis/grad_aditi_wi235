package com.example.spring.student_portal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.student_portal.models.Student;
import com.example.spring.student_portal.services.StudentService;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/")
    public ModelAndView landing() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("landing.jsp");
        return mv;
    }

    @PostMapping("/insert-student")
    public ModelAndView insertStudent(@RequestParam("studentId") int id, @RequestParam("studentName") String name, @RequestParam("studentStandard") int std, @RequestParam("studentSchoolName") String school) {

        Student student = new Student(id, name, std, school);
        studentService.saveToTwoTables(student);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("success.jsp");
        mv.addObject("student", student);
        
        return mv; 
    }
}
