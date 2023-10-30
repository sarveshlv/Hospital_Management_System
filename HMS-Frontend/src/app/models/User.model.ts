export class User {
  userid: string;
  emailId: string;
  password: string;
  firstName: string;
  lastName: string;
  role: string;
  referenceId: string;

  constructor(
    userid: string,
    emailId: string,
    password: string,
    firstName: string,
    lastName: string,
    role: string,
    referenceId: string
  ) {
    this.userid = userid;
    this.emailId = emailId;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
    this.referenceId = referenceId;
  }
}
