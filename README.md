# HuMuS - Hospital Management System

HuMuS (Hospital Management System) is an application that provides a comprehensive platform for managing hospitals, beds, patients, bookings, billing, and user authentication.

This system is built using Spring, MongoDB, Angular integrated with Spring Security, Eureka client for service discovery, and a gateway for routing requests.

## API Documentation

This section provides details about the RESTful API endpoints available in the HuMuS portal.

### Auth Controller

#### Base URL: `/api/auth`

- `POST /login` - User login without authentication.

- `GET /validate` - Validate user tokens without authentication.

### User Controller

#### Base URL: `/api/users`

- `POST /signup` - User registration without authentication.

- `PUT /update` - Update user information.

- `GET /findByEmail` - Retrieve user details by email.

- `GET /addReference/{emailId}/{referenceId}`

### Hospital Controller

#### Base URL: `/api/hospitals`

- `POST /add` - Add a new hospital.

- `PUT /update/{id}` - Update hospital information.

- `GET /findById/{id}` - Retrieve hospital details by ID.

- `GET /isVerified/{id}` - Check if a hospital is verified.

- `PUT /verify/{id}` - Verify a hospital.

- `GET /nearby/{pincode}` - Retrieve nearby hospitals by pincode.

### Bed Controller

#### Base URL: `/api/beds`

- `POST /add` - Add a new bed.

- `PUT /update/{id}` - Update bed information.

- `GET /findById/{id}` - Retrieve bed details by ID.

- `GET /findAll` - Retrieve all beds.

- `GET /findNearby/{pincode}` - Retrieve nearby beds by pincode.

- `GET /findByType/{bedType}` - Retrieve beds by type.

- `GET /findByHospital/{hospitalId}` - Retrieve beds by hospital ID.

### Patient Controller

#### Base URL: `/api/patients`

- `POST /add` - Add a new patient.

- `PUT /update/{id}` - Update patient information.

- `GET /findById/{id}` - Retrieve patient details by ID.

### Booking Controller

#### Base URL: `/api/bookings`

- `POST /add` - Add a new booking.

- `GET /findById/{bookingId}` - Retrieve booking details by ID.

- `GET /findByPatientId/{patientId}` - Retrieve bookings by patient ID.

- `GET /findByHospitalId/{hospitalId}` - Retrieve bookings by hospital ID.

- `GET /approve/{bookingId}` - Approve a booking.

- `GET /reject/{bookingId}` - Reject a booking.

- `GET /cancel/{bookingId}` - Cancel a booking.

- `GET /complete/{bookingId}` - Complete a booking.

### Billing Controller

#### Base URL: `/api/billings`

- `POST /add` - Add a new billing record.

- `GET /findById/{billingId}` - Retrieve billing details by ID.

## Service Documentation

This section provides an overview of the service classes and their methods in the hospital management system (HuMuS) application.

## Authentication Service

- Provides authentication-related services.
- Methods:
  - `generateToken(String email, Collection<? extends GrantedAuthority> authorities)`: Generates an authentication token.
  - `validateToken(String token)`: Validates an authentication token.

## User Service

- Manages user-related operations.
- Methods:
  - `saveUser(AddUserRequest addUserRequest)`: Saves a new user.
  - `updateUser(UpdateUserRequest updateUserRequest)`: Updates user information.
  - `getUserByEmail(String email)`: Retrieves user details by email.
  - `addReferenceId(String email, String referenceId)`: Adds a reference ID to a user.

## Hospital Service

- Handles hospital-related operations.
- Methods:
  - `addHospital(AddHospitalRequest addHospitalRequest)`: Adds a new hospital.
  - `updateHospital(String id, AddHospitalRequest addHospitalRequest)`: Updates hospital information.
  - `findHospitalById(String id)`: Retrieves hospital details by ID.
  - `isHospitalVerified(String id)`: Checks if a hospital is verified.
  - `verifyHospital(String id)`: Verifies a hospital.
  - `getNearbyHospitals(Long pincode)`: Retrieves nearby hospitals by pincode.

## Patient Service

- Manages patient-related operations.
- Methods:
  - `addPatient(AddPatientRequest addPatientRequest)`: Adds a new patient.
  - `updatePatient(String id, AddPatientRequest addPatientRequest)`: Updates patient information.
  - `findPatientById(String id)`: Retrieves patient details by ID.

## Booking Service

- Handles booking-related operations.
- Methods:
  - `addBooking(String authorizationHeader, AddBookingRequest addBookingRequest)`: Adds a new booking.
  - `findBookingById(String bookingId)`: Retrieves booking details by ID.
  - `getBookingByPatientId(String patientId)`: Retrieves bookings by patient ID.
  - `approveBooking(String authorizationHeader, String bookingId)`: Approves a booking.
  - `rejectBooking(String bookingId)`: Rejects a booking.
  - `cancelBooking(String authorizationHeader, String bookingId)`: Cancels a booking.
  - `completeBooking(String authorizationHeader, String bookingId)`: Completes a booking.
  - `getBookingByHospitalId(String hospitalId)`: Retrieves bookings by hospital ID.

## Billing Service

- Manages billing-related operations.
- Methods:
  - `addBilling(String authroizationHeader, String bookingId)`: Adds a new billing record.
  - `findById(String billingId)`: Retrieves billing details by ID.

## Integration Points

This section provides details about the Feign client interfaces used for integration between microservices.

## Hospital Service Client (Bedms)

- Interface: `IHospitalServiceClient`
- Methods:
  - `isHospitalFound(String authorizationValue, String hospitalId)`: Checks if a hospital is found.
  - `getNearbyHospitals(String authorizationValue, Long pincode)`: Retrieves nearby hospitals by pincode.

## Bed Service Client (Bookingms)

- Interface: `IBedServiceClient`
- Methods:
  - `bookBed(String authorizationHValue, String bedId)`: Books a bed.
  - `unbookBed(String authorizationHValue, String bedId)`: Unbooks a bed.
  - `findBedsByType(String authorizationHValue, BedType bedType)`: Retrieves beds by type.

## Hospital Service Client (Bookingms)

- Interface: `IHospitalServiceClient`
- Methods:
  - `isHospitalFound(String authorizationValue, String hospitalId)`: Checks if a hospital is found.

## Patient Service Client (Bookingms)

- Interface: `IPatientServiceClient`
- Methods:
  - `isPatientFound(String authorizationValue, String hospitalId)`: Checks if a patient is found.

## Hospital Service Client (Billingms)

- Interface: `IHospitalServiceClient`
- Methods:
  - `makeBedAvaialbe(String authorizationHValue, String bedId)`: Makes a bed available.

## Booking Service Client (Billingms)

- Interface: `IBookingServiceClient`
- Methods:
  - `completeBooking(String authorizationHValue, String bookingId)`: Completes a booking.
