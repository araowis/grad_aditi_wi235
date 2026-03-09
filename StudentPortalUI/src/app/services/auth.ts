import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  role: string | null = null;

  login(username: string, password: string) {
    if (username === 'admin') {
      this.role = 'admin';
    } else {
      this.role = 'staff';
    }

    localStorage.setItem('role', this.role);
  }

  getRole() {
    return localStorage.getItem('role');
  }

  isAdmin() {
    return this.getRole() === 'admin';
  }

  logout() {
    localStorage.removeItem('role');
  }
}
