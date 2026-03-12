import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Dashboard } from './components/dashboard/dashboard';
import { adminGuard } from './guards/admin-guard';

const routes: Routes = [
  { path: '', component: Login },
  { path: 'dashboard', component: Dashboard },
  {
    path: 'admin',
    component: Dashboard,
    canActivate: [adminGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
