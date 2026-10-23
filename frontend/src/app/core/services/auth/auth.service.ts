import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { CargoEmpleado, EmpleadoResponseDTO } from '../../models/personal.model';
import { HotelAdminStore } from '../../demo/hotel-admin.store';
import { environment } from '../../../../environments/environment';

export interface LoginCredentials {
  identifier: string;
  password: string;
  turnoId: number;
  remember: boolean;
}
export interface AuthSession {
  user: EmpleadoResponseDTO;
  token: string;
  turnoId: number;
}

const storageKey = 'hotel-parda-auth-v1';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly admin = inject(HotelAdminStore);
  private readonly session = signal<AuthSession | null>(this.restore());
  readonly currentUser = computed(() => this.session()?.user ?? null);
  readonly isAuthenticated = computed(() => !!this.currentUser());
  readonly activeTurno = computed(
    () => this.admin.shifts().find((shift) => shift.id === this.session()?.turnoId) ?? null,
  );
  readonly token = computed(() => this.session()?.token ?? null);
  readonly role = computed(() => this.currentUser()?.cargo ?? null);
  readonly landing = computed(() =>
    this.role() === CargoEmpleado.CAJERO ? '/finanzas' : '/dashboard',
  );

  async login(credentials: LoginCredentials): Promise<void> {
    const identifier = credentials.identifier.trim().toLowerCase();
    if (!this.admin.shifts().some((shift) => shift.id === credentials.turnoId))
      throw new Error('Seleccione un turno válido.');
    let session: AuthSession;
    try {
      session = await firstValueFrom(
        this.http.post<AuthSession>(`${environment.apiUrl}/auth/login`, {
          usuario: identifier,
          password: credentials.password,
          turnoId: credentials.turnoId,
        }),
      );
    } catch (error) {
      if (error instanceof HttpErrorResponse && error.status === 401)
        throw new Error('Usuario, contraseña o turno incorrectos.');
      throw new Error('No se pudo conectar con el servidor. Inténtelo de nuevo.');
    }
    if (!session.user?.cargo || !session.token)
      throw new Error('La respuesta de acceso no es válida.');
    this.session.set(session);
    if (typeof window !== 'undefined') {
      localStorage.removeItem(storageKey);
      sessionStorage.removeItem(storageKey);
      if (credentials.remember) sessionStorage.setItem(storageKey, JSON.stringify(session));
    }
  }
  logout() {
    this.session.set(null);
    if (typeof window !== 'undefined') {
      localStorage.removeItem(storageKey);
      sessionStorage.removeItem(storageKey);
    }
  }
  canAccess(path: string) {
    const role = this.role();
    if (role === CargoEmpleado.ADMINISTRADOR) return true;
    if (role === CargoEmpleado.RECEPCIONISTA)
      return ['dashboard', 'recepcion', 'reservas', 'huespedes', 'servicios'].includes(path);
    if (role === CargoEmpleado.CAJERO) return path === 'finanzas';
    return false;
  }
  private restore(): AuthSession | null {
    if (typeof window === 'undefined') return null;
    try {
      localStorage.removeItem('hotel-parda-operations-v1');
      localStorage.removeItem('hotel-parda-admin-v1');
      const raw = sessionStorage.getItem(storageKey);
      if (!raw) return null;
      const value = JSON.parse(raw) as AuthSession;
      return value.user?.cargo && value.token ? value : null;
    } catch {
      return null;
    }
  }
}
