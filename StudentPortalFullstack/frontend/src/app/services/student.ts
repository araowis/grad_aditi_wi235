import { Injectable } from '@angular/core';
import { Student } from '../models/student';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class StudentService {
  private serverURL = "http://localhost:5000/students";

  constructor(private http : HttpClient) {}
  
  getStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(this.serverURL);
  }

  getStudent(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.serverURL}/${id}`);
  }

  getStudentsBySchool(school: string): Observable<Student[]> {
    let params = new HttpParams().set('name', school);
    return this.http.get<Student[]>(`${this.serverURL}/school`, { params });
  }

  addStudent(student: Student): Observable<any> {
    return this.http.post(this.serverURL, student);
  }

  updateStudent(id: number, student: Student): Observable<any> {
    return this.http.put(`${this.serverURL}/${id}`, student);
  }

  patchStudent(id: number, student: Student): Observable<any> {
    return this.http.patch(`${this.serverURL}/${id}`, student);
  }

  deleteStudent(id: number): Observable<any> {
    return this.http.delete(`${this.serverURL}/${id}`);
  }
}
