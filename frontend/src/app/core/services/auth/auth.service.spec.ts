import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  afterEach(() => TestBed.inject(HttpTestingController).verify());
  beforeEach(() => {
    localStorage.removeItem('hotel-parda-auth-v1');
    sessionStorage.removeItem('hotel-parda-auth-v1');
    sessionStorage.removeItem('hotel-parda-auth-reload');
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
  });

  it('rejects login when the backend is unavailable', async () => {
    const auth = TestBed.inject(AuthService);
    const http = TestBed.inject(HttpTestingController);
    const login = auth.login({
      identifier: 'recepcion@hotel.com',
      password: 'recep123',
      turnoId: 1,
      remember: false,
    });
    http
      .expectOne('http://localhost:8080/api/auth/login')
      .flush(null, { status: 503, statusText: 'Unavailable' });
    await expect(login).rejects.toBeDefined();
    expect(auth.isAuthenticated()).toBe(false);
  });

  it('loads shift IDs and times from the real contract', async () => {
    const auth = TestBed.inject(AuthService);
    const loading = auth.loadTurnos();
    const request = TestBed.inject(HttpTestingController).expectOne('http://localhost:8080/api/auth/turnos-disponibles');
    expect(request.request.method).toBe('GET');
    expect(request.request.headers.has('Authorization')).toBe(false);
    request.flush([{ idTurno: 17, horaInicio: '08:00:00', horaFinal: '16:00:00' }]);
    await loading;
    expect(auth.turnos()[0].idTurno).toBe(17);
  });

  it('allows a server-assigned shift outside the former demo catalog', async () => {
    const auth = TestBed.inject(AuthService);
    const login = auth.login({ identifier: 'test@example.com', password: 'secret', turnoId: 17, remember: false });
    const request = TestBed.inject(HttpTestingController).expectOne('http://localhost:8080/api/auth/login');
    expect(request.request.body.turnoId).toBe(17);
    request.flush({ token: 'signed-token', turnoId: 17, user: { cargo: 'RECEPCIONISTA' } });
    await login;
    expect(auth.activeTurno()?.name).toBe('Turno 17');
    expect(auth.landing()).toBe('/inicio');
    auth.prepareHomeReload();
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    const restored = TestBed.inject(AuthService);
    expect(restored.isAuthenticated()).toBe(true);
    expect(sessionStorage.getItem('hotel-parda-auth-reload')).toBeNull();
    expect(sessionStorage.getItem('hotel-parda-auth-v1')).toBeNull();
  });

  it('uses the authenticated role returned by the backend', async () => {
    const auth = TestBed.inject(AuthService);
    const http = TestBed.inject(HttpTestingController);
    const login = auth.login({
      identifier: 'recepcion@hotel.com', password: 'secret', turnoId: 1, remember: true,
    });
    http.expectOne('http://localhost:8080/api/auth/login').flush({
      token: 'signed-token', turnoId: 1,
      user: { id: 2, nombres: 'Recepción', apellidos: 'Hotel', numeroDocumento: '',
        correo: 'recepcion@hotel.com', cargo: 'RECEPCIONISTA', activo: true, turnoId: 1 },
    });
    await login;
    expect(auth.isAuthenticated()).toBe(true);
    expect(auth.activeTurno()?.name).toBe('Turno 1');
    expect(auth.canAccess('recepcion')).toBe(true);
    expect(auth.canAccess('finanzas')).toBe(false);
    expect(auth.canAccess('inicio')).toBe(true);
    expect(sessionStorage.getItem('hotel-parda-auth-v1')).toBeTruthy();
    auth.logout();
    expect(auth.isAuthenticated()).toBe(false);
  });
});
