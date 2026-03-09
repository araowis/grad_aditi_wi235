import { Component, ViewChild } from '@angular/core';
import { AuthService } from '../../services/auth';
import { StudentForm } from '../student-form/student-form';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  @ViewChild('studentForm') studentForm!: StudentForm;
  constructor(public auth: AuthService) {}
}
