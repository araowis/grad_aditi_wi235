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

@NgModule({
  declarations: [App, Login, StudentList, StudentForm, Navbar, Dashboard],
  imports: [BrowserModule, AppRoutingModule, FormsModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
