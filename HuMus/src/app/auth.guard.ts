import { CanActivateFn, Router } from '@angular/router';
import { JwtStorageService } from './services/jwt/jwt.storage.service';
import { inject } from '@angular/core';

export const AuthGuard: CanActivateFn = (route, state) => {
  return inject(JwtStorageService).isAuthenticated()
    ? true
    : inject(Router).createUrlTree(['/login']);
};
