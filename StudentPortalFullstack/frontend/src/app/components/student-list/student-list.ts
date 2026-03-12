import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { StudentService } from '../../services/student';
import { AuthService } from '../../services/auth';
import { StudentForm } from '../student-form/student-form';
import { Student } from '../../models/student';

@Component({
  selector: 'app-student-list',
  standalone: false,
  templateUrl: './student-list.html',
  styleUrl: './student-list.css',
})
export class StudentList implements OnInit, OnChanges {

  @Input() reload: boolean = false;
  @Output() edit = new EventEmitter<Student>();

  students: Student[] = [];
  school: string = '';

  constructor(
    public studentService: StudentService,
    public auth: AuthService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadStudents();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['reload'] && !changes['reload'].firstChange) {
      this.loadStudents();
    }
  }

  loadStudents() {
    this.cd.detectChanges();
    const schoolFilter = this.school?.trim();
    const request = schoolFilter && schoolFilter.length > 0
      ? this.studentService.getStudentsBySchool(schoolFilter)
      : this.studentService.getStudents();

    request.subscribe(data => {
      this.students = data;
      console.log("Students:", data);
    });

  }

  deleteStudent(id: number) {
    this.studentService.deleteStudent(id).subscribe(() => {
      this.loadStudents();
    });
  }

  editStudent(student: Student) {
    this.edit.emit(student);
    console.log("Edit emit:", student.name);
  }

}