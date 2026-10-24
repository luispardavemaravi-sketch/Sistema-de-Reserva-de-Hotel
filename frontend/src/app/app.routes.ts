import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login.component').then((m) => m.LoginComponent),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./shared/layout/admin-layout.component').then((m) => m.AdminLayoutComponent),
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'inicio' },
      {
        path: 'inicio',
        loadComponent: () => import('./features/inicio/inicio.component').then((m) => m.InicioComponent),
      },
      {
        path: 'dashboard',
        canActivate: [roleGuard],
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent),
      },
      {
        path: 'recepcion',
        canActivate: [roleGuard],
        loadComponent: () =>
          import('./features/recepcion/recepcion.component').then((m) => m.RecepcionComponent),
      },
      {
        path: 'reservas',
        canActivate: [roleGuard],
        loadComponent: () =>
          import('./features/reservas/reservas.component').then((m) => m.ReservasComponent),
      },
      {
        path: 'habitaciones',
        canActivate: [roleGuard],
        loadComponent: () =>
          import('./features/habitaciones/habitaciones.component').then(
            (m) => m.HabitacionesComponent,
          ),
      },
      {
        path: 'huespedes',
        canActivate: [roleGuard],
        loadComponent: () =>
          import('./features/huespedes/huespedes.component').then((m) => m.HuespedesComponent),
      },
      {
        path: 'servicios',
        canActivate: [roleGuard],
        loadComponent: () =>
          import('./features/servicios/servicios.component').then((m) => m.ServiciosComponent),
      },
      {
        path: 'finanzas',
        canActivate: [roleGuard],
        loadComponent: () =>
          import('./features/finanzas/finanzas.component').then((m) => m.FinanzasComponent),
      },
      {
        path: 'personal',
        canActivate: [roleGuard],
        loadComponent: () =>
          import('./features/personal/personal.component').then((m) => m.PersonalComponent),
      },
    ],
  },
  { path: '**', redirectTo: 'inicio' },
];
