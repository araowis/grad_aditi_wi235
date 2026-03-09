import { Component } from '@angular/core';
import { StudentService } from '../../services/student';

@Component({
  selector: 'app-student-form',
  standalone: false,
  templateUrl: './student-form.html',
  styleUrl: './student-form.css',
})
export class StudentForm {
  student:any = {}
  editIndex:number | null = null

  constructor(private studentService : StudentService){}

  addOrUpdate(){

    if(this.editIndex === null){
      this.studentService.addStudent(this.student)
    }
    else{
      this.studentService.updateStudent(this.editIndex,this.student)
      this.editIndex = null
    }

    this.student={}
  }

  editStudent(student:any,index:number){
    this.student = {...student}
    this.editIndex = index
  }
}
