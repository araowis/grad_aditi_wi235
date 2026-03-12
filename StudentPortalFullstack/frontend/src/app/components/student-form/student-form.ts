import { ChangeDetectorRef, Component, EventEmitter, Input, Output } from '@angular/core';
import { StudentService } from '../../services/student';
import { Student } from '../../models/student';

@Component({
  selector: 'app-student-form',
  standalone: false,
  templateUrl: './student-form.html',
  styleUrl: './student-form.css',
})
export class StudentForm {

  student:any = {}
  editId:number | null = null

  @Input() studentToEdit?: Student;
  //a class used primarily in child components to emit custom events and data to their parent components
  @Output() refresh = new EventEmitter<void>()

  constructor(private studentService : StudentService, private cd: ChangeDetectorRef){}

  ngOnChanges() {
    if (this.studentToEdit) {
      this.student = { ...this.studentToEdit };
      this.editId = this.student.regNo || null;
      console.log("Received in form! " + this.student.name + " with edit id " + this.editId);
    }
  }

  addOrUpdate(){

    if(this.editId === null){
      this.studentService.addStudent(this.student).subscribe(() => {
        console.log("Entered the add service!");
        console.log(this.refresh.emit());
        this.cd.detectChanges();
      })

    } else {
      this.studentService.updateStudent(this.editId, this.student).subscribe(() => {
        console.log("Entered the update service!");
        console.log(this.refresh.emit());
      })

      this.editId = null
    }

    this.student = {}
  }

  editStudent(student:any){
    this.student = {...student}
    this.editId = student.regNo
  }

}