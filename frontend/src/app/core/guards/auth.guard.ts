import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  return auth.isAuthenticated() || inject(Router).createUrlTree(['/login']);
};

export const roleGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  return (
    auth.canAccess(route.routeConfig?.path ?? '') || inject(Router).createUrlTree([auth.landing()])
  );
};
