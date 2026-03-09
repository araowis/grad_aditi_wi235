import { Injectable } from '@angular/core';
import { Student } from '../models/student';

@Injectable({
  providedIn: 'root',
})
export class StudentService {
  students: Student[] = JSON.parse(localStorage.getItem('students') || '[]');

  save() {
    localStorage.setItem('students', JSON.stringify(this.students));
  }

  getStudents() {
    return this.students;
  }

  addStudent(student: Student) {
    this.students.push(student);
    this.save();
  }

  updateStudent(index: number, student: Student) {
    this.students[index] = student;
    this.save();
  }

  deleteStudent(index: number) {
    this.students.splice(index, 1);
    this.save();
  }
}
