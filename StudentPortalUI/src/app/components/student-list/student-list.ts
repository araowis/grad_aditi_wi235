import { Component, Input } from '@angular/core';
import { StudentService } from '../../services/student';
import { AuthService } from '../../services/auth';
import { StudentForm } from '../student-form/student-form';

@Component({
  selector: 'app-student-list',
  standalone: false,
  templateUrl: './student-list.html',
  styleUrl: './student-list.css',
})
export class StudentList {
  @Input() form!: StudentForm;

  constructor(public studentService: StudentService, public auth: AuthService) {}

  // students = this.studentService.getStudents();

  get students() {
    return this.studentService.getStudents();
  }

  deleteStudent(i: number) {
    this.studentService.deleteStudent(i);
  }
}
