export interface AddUserRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  role: string;
}

export interface UpdateUserRequest {
  firstName: string;
  lastName: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface UpdatePasswordRequest {
  email: string;
  password: string;
}

export interface UserDetails {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  referenceId: string;
}
