import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from '../../services/auth';
import { Student } from '../../models/student';
// import { Stats } from '../../services/stats';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  // @ViewChild('studentForm') studentForm!: StudentForm;
  reloadList:boolean = false;
  selectedStudent?: Student;
  stats: any[] = [];

  constructor(public auth: AuthService) {}

  ngOnInit() {
    // this.loadStats();
  }

  // loadStats() {
  //   this.stat.getAllStats().subscribe(data => {
  //     this.stats = [
  //       { type: 'strength', value: data.strength },
  //       { type: 'standard', value: data.standardCount },
  //       { type: 'result', value: data.result }
  //     ];
  //   });
  // }

  triggerReload(){
    this.reloadList = !this.reloadList
    this.stats
  }
}
