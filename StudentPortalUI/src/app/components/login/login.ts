import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  username = '';
  password = '';

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  login() {
    this.auth.login(this.username, this.password);
    this.router.navigate(['/dashboard']);
  }
}
