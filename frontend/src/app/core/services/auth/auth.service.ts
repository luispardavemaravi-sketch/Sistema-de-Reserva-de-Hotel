import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { CargoEmpleado, EmpleadoResponseDTO } from '../../models/personal.model';
import { environment } from '../../../../environments/environment';

export interface LoginCredentials {
  identifier: string;
  password: string;
  turnoId: number;
  remember: boolean;
}
export interface TurnoDisponible {
  idTurno: number;
  horaInicio: string;
  horaFinal: string;
}
export interface AuthSession {
  user: EmpleadoResponseDTO;
  token: string;
  turnoId: number;
}

const storageKey = 'hotel-parda-auth-v1';
const reloadKey = 'hotel-parda-auth-reload';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  readonly turnos = signal<TurnoDisponible[]>([]);
  private readonly session = signal<AuthSession | null>(this.restore());
  readonly currentUser = computed(() => this.session()?.user ?? null);
  readonly isAuthenticated = computed(() => !!this.currentUser());
  readonly activeTurno = computed(() => {
    const id = this.session()?.turnoId;
    if (!id) return null;
    const turno = this.turnos().find((item) => item.idTurno === id);
    return { name: `Turno ${id}`, start: turno?.horaInicio ?? '—', end: turno?.horaFinal ?? '—' };
  });
  readonly token = computed(() => this.session()?.token ?? null);
  readonly role = computed(() => this.currentUser()?.cargo ?? null);
  readonly landing = computed(() => '/inicio');

  prepareHomeReload() {
    // Carry an in-memory session through this explicit reload only.
    if (this.session() && typeof window !== 'undefined')
      sessionStorage.setItem(reloadKey, JSON.stringify(this.session()));
  }

  async loadTurnos(): Promise<void> {
    this.turnos.set(await firstValueFrom(
      this.http.get<TurnoDisponible[]>(`${environment.apiUrl}/auth/turnos-disponibles`),
    ));
  }

  async login(credentials: LoginCredentials): Promise<void> {
    const identifier = credentials.identifier.trim().toLowerCase();
    if (!Number.isInteger(credentials.turnoId) || credentials.turnoId <= 0)
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
      sessionStorage.removeItem(reloadKey);
      if (credentials.remember) sessionStorage.setItem(storageKey, JSON.stringify(session));
    }
  }
  logout() {
    this.session.set(null);
    if (typeof window !== 'undefined') {
      localStorage.removeItem(storageKey);
      sessionStorage.removeItem(storageKey);
      sessionStorage.removeItem(reloadKey);
    }
  }
  canAccess(path: string) {
    const role = this.role();
    if (path === 'inicio') return this.isAuthenticated();
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
      const raw = sessionStorage.getItem(reloadKey) ?? sessionStorage.getItem(storageKey);
      sessionStorage.removeItem(reloadKey);
      if (!raw) return null;
      const value = JSON.parse(raw) as AuthSession;
      return value.user?.cargo && value.token ? value : null;
    } catch {
      return null;
    }
  }
}
