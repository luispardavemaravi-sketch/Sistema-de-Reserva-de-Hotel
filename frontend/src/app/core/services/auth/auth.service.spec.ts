import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  beforeEach(() => {
    localStorage.removeItem('hotel-parda-auth-v1');
    sessionStorage.removeItem('hotel-parda-auth-v1');
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
  });

  it('uses a local test account when the backend is unavailable and limits its routes', async () => {
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
    await login;
    expect(auth.isAuthenticated()).toBe(true);
    expect(auth.activeTurno()?.name).toBe('Mañana');
    expect(auth.canAccess('recepcion')).toBe(true);
    expect(auth.canAccess('finanzas')).toBe(false);
    expect(sessionStorage.getItem('hotel-parda-auth-v1')).toBeTruthy();
    auth.logout();
    expect(auth.isAuthenticated()).toBe(false);
  });
});
