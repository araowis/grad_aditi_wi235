package com.example.spring.university_portal;

import com.example.spring.university_portal.controllers.UniversityController;
import com.example.spring.university_portal.models.Student;
import com.example.spring.university_portal.repositories.UniversityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UniversityController.class)
class UniversityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // do not use the ACTUAL repo, use a mock so that the db doesn't get messed up
    // so this makes 
    @MockBean
    private UniversityRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    private Student student(Integer id) {
        Student s = new Student();
        s.setRegNo(id);
        s.setName("Student-" + id);
        s.setGender("Male");
        s.setStandard(10);
        s.setPercentage(65.5);
        s.setRollNo(100 + id);
        s.setSchoolName("School-" + id);
        return s;
    }

    @Test
    void getAllStudentsShouldReturnList() throws Exception {
        List<Student> students = List.of(student(1), student(2));
        when(repo.findAll()).thenReturn(students);

        mockMvc.perform(get("/students"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(students.size())))
        .andExpect(jsonPath("$[0].regNo").value(1))
        .andExpect(jsonPath("$[1].regNo").value(2));

        verify(repo).findAll();
    }
    
    @Test
    void getStudentShouldReturnStudentIfExists() throws Exception {
        Student s = student(16);
        when(repo.findById(16)).thenReturn(Optional.of(s));

        mockMvc.perform(get("/students/{id}", 16))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.regNo").value(16))
        .andExpect(jsonPath("$.name").value("Student-16"));

        verify(repo).findById(16);
    }

    @Test
    void getStudentShouldReturn404IfItDoesNotExist() throws Exception {
        when(repo.findById(4)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/{id}", 4))
        .andExpect(status().isNotFound());

        verify(repo).findById(4);
    }

    @Test
    void addStudentShouldSaveIfItDoesNotExist() throws Exception {
        Student s = student(10);
        when(repo.existsById(10)).thenReturn(false);

        mockMvc.perform(post("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(s)))
        .andExpect(status().isOk())
        .andExpect(content().string("Successfully created student"));

        verify(repo).existsById(10);
        verify(repo).save(any(Student.class));
    }

    @Test
    void addStudentShouldNotSaveIfItExists() throws Exception {
        Student s = student(20);
        when(repo.existsById(20)).thenReturn(true);

        mockMvc.perform(post("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(s)))
        .andExpect(status().isOk())
        .andExpect(content().string("Sorry, student already exists"));

        verify(repo).existsById(20);
        verify(repo, never()).save(any());
    }

    @Test
    void patchShouldMergeFieldsDynamicallyIfTheyExist() throws Exception {
        Student existing = student(7);

        Student partial = new Student();
        partial.setName("SomeRandom Name"); // only updating name

        when(repo.existsById(7)).thenReturn(true);
        when(repo.findById(7)).thenReturn(Optional.of(existing));

        mockMvc.perform(patch("/students/{id}", 7)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(partial)))
        .andExpect(status().isOk())
        .andExpect(content().string("Successfully updated student record."));

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(repo).save(captor.capture());

        Student saved = captor.getValue();

        // Updated field should be the new name
        assert saved.getName().equals("SomeRandom Name");

        // Preserved fields should remain the same
        assert saved.getGender().equals(existing.getGender());
        assert saved.getStandard().equals(existing.getStandard());
        assert saved.getPercentage().equals(existing.getPercentage());
        assert saved.getRollNo().equals(existing.getRollNo());
        assert saved.getSchoolName().equals(existing.getSchoolName());
    }

    @Test
    void patchShouldReturnMessageIfItDoesNotExist() throws Exception {
        when(repo.existsById(11)).thenReturn(false);

        mockMvc.perform(patch("/students/{id}", 11)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
        .andExpect(status().isOk())
        .andExpect(content().string("Sorry, this student does not exist in the database."));

        verify(repo).existsById(11);
        verify(repo, never()).save(any());
    }

    @Test
    void putShouldReplaceStudentIfItExists() throws Exception {
        Student s = student(80);
        when(repo.existsById(80)).thenReturn(true);

        mockMvc.perform(put("/students/{id}", 80)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(s)))
        .andExpect(status().isOk())
        .andExpect(content().string("Successfully updated student record."));

        verify(repo).existsById(80);
        verify(repo).save(any(Student.class));
    }

    @Test
    void deleteShouldCallRepositoryIfItExists() throws Exception {
        when(repo.existsById(90)).thenReturn(true);

        mockMvc.perform(delete("/students/{id}", 90))
        .andExpect(status().isOk())
        .andExpect(content().string("Student deleted successfully."));

        verify(repo).deleteById(90);
    }

    @Test
    void deleteShouldNotCallRepositoryIfItDoesNotExist() throws Exception {
        when(repo.existsById(100)).thenReturn(false);

        mockMvc.perform(delete("/students/{id}", 100))
        .andExpect(status().isOk())
        .andExpect(content().string("Sorry, this student does not exist in the database."));

        verify(repo).existsById(100);
        verify(repo, never()).deleteById(any());
    }

    @Test
    void countEndpointsShouldReturnRepositoryValue() throws Exception {
        when(repo.countBySchoolName("School-1")).thenReturn(3L);

        mockMvc.perform(get("/students/school/count")
        .param("name", "School-1"))
        .andExpect(status().isOk())
        .andExpect(content().string("3"));

        verify(repo).countBySchoolName("School-1");
    }

    @Test
    void resultEndpointShouldCallCorrectRepositoryMethod() throws Exception {
        when(repo.findByPercentageGreaterThanEqualOrderByPercentageDesc(40.0))
        .thenReturn(List.of(student(1)));

        mockMvc.perform(get("/students/result")
        .param("pass", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));

        verify(repo).findByPercentageGreaterThanEqualOrderByPercentageDesc(40.0);
        verify(repo, never()).findByPercentageLessThanOrderByPercentageDesc(any());
    }
}