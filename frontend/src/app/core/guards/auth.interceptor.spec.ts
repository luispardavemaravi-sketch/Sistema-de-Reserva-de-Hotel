import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';
import { authInterceptor } from './auth.interceptor';
import { environment } from '../../../environments/environment';

describe('authInterceptor', () => {
  const logout = vi.fn();
  const navigateByUrl = vi.fn();
  beforeEach(() => {
    vi.clearAllMocks();
    TestBed.configureTestingModule({ providers: [
      provideHttpClient(withInterceptors([authInterceptor])), provideHttpClientTesting(),
      { provide: AuthService, useValue: { token: () => 'test-token', logout } },
      { provide: Router, useValue: { navigateByUrl } },
    ] });
  });
  afterEach(() => TestBed.inject(HttpTestingController).verify());

  it('sends the JWT only to the configured API', () => {
    const http = TestBed.inject(HttpClient);
    const testing = TestBed.inject(HttpTestingController);
    http.get(`${environment.apiUrl}/reservas`).subscribe();
    const api = testing.expectOne(`${environment.apiUrl}/reservas`);
    expect(api.request.headers.get('Authorization')).toBe('Bearer test-token');
    api.flush([]);
    http.get('https://external.example/api/reservas').subscribe();
    const external = testing.expectOne('https://external.example/api/reservas');
    expect(external.request.headers.has('Authorization')).toBe(false);
    external.flush([]);
  });

  it('clears the session on 401 but preserves it on 403', () => {
    for (const status of [403, 401]) {
      TestBed.inject(HttpClient).get(`${environment.apiUrl}/reservas`).subscribe({ error: () => {} });
      TestBed.inject(HttpTestingController).expectOne(`${environment.apiUrl}/reservas`)
        .flush({}, { status, statusText: 'Denied' });
      expect(logout).toHaveBeenCalledTimes(status === 401 ? 1 : 0);
    }
    expect(navigateByUrl).toHaveBeenCalledWith('/login');
  });
});
