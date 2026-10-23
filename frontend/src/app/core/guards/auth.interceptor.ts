import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const token = inject(AuthService).token();
  if (!token || token === 'local-session' || !request.url.startsWith('http://localhost:8080/'))
    return next(request);
  return next(request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
};
