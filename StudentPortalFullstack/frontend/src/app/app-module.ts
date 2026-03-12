import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Login } from './components/login/login';
import { StudentList } from './components/student-list/student-list';
import { StudentForm } from './components/student-form/student-form';
import { Navbar } from './components/navbar/navbar';
import { Dashboard } from './components/dashboard/dashboard';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Stats } from './components/stats/stats';

@NgModule({
  declarations: [App, Login, StudentList, StudentForm, Navbar, Dashboard, Stats],
  imports: [BrowserModule, AppRoutingModule, FormsModule, HttpClientModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
