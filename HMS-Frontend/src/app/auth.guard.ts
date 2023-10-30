import { CanActivateFn, Router } from '@angular/router';
import { JwtService } from './services/jwt/jwt.service';
import { inject } from '@angular/core';

export const AuthGuard: CanActivateFn = (route, state) => {
  return inject(JwtService).isAuthenticated() ? true: inject(Router).createUrlTree(['/login']);
};
