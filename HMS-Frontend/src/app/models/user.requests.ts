export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterUserRequest {
  firstName: string;
  lastName: string;
  emailId: string;
  password: string;
  role: string;
}

export interface UpdateUserRequest {
  email: string;
  firstName: string;
  lastName: string;
  password: string;
}

export interface UserDetails {
  userId: string;
  emailId: string;
  firstName: string;
  lastName: string;
  role: string;
  loggedIn: boolean;
  referenceId: string;
}
