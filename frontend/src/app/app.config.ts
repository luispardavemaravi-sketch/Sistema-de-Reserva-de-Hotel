import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withViewTransitions } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { authInterceptor } from './core/guards/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes, withViewTransitions({
      skipInitialTransition: true,
      onViewTransitionCreated: ({ transition, from, to }) => {
        while (from.firstChild) from = from.firstChild;
        while (to.firstChild) to = to.firstChild;
        if (matchMedia('(prefers-reduced-motion: reduce)').matches || from.routeConfig === to.routeConfig)
          transition.skipTransition();
      },
    })),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
  ],
};
