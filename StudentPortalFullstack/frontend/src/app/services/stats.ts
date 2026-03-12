import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, forkJoin } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class StatsService {
  constructor(private http : HttpClient) {}

  private serverURL = "http://localhost:5000/students";

  getStrength(gender: string, standard: number): Observable<any> {
    let params = new HttpParams().set('gender', gender)
                                 .set('standard', standard);
    return this.http.get(`${this.serverURL}/strength`, { params })
  }
  
  getStandardCount(clas : number): Observable<any> {
    let params = new HttpParams().set('class', clas);
    return this.http.get(`${this.serverURL}/standard/count`, { params });
  }

  getResult(pass: boolean): Observable<any> {
    let params = new HttpParams().set('pass', pass);
    return this.http.get(`${this.serverURL}/result`, { params });
  }
}
