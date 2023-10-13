package com.hms.userms.service;

import com.hms.userms.dto.AddUserRequest;
import com.hms.userms.dto.UpdateUserRequest;
import com.hms.userms.dto.UserDetails;
import com.hms.userms.entity.User;
import com.hms.userms.exception.UserNotFoundException;
import com.hms.userms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

    // Test case for successfully saving a new user
	@Test
	public void testSaveUser_Success() {
		// Arrange
		AddUserRequest request = new AddUserRequest();
		request.setFirstName("Prateek");
		request.setLastName("Singh");
		request.setEmail("singh.prateek2599@gmail.com");
		request.setPassword("Prateek@2000");
		request.setRole("USER");

		//Mock
		User savedUser = new User();
		savedUser.setId("1");
		savedUser.setFirstName(request.getFirstName());
		savedUser.setLastName(request.getLastName());
		savedUser.setEmail(request.getEmail());
		savedUser.setLoggedId(false);
		savedUser.setPassword("encoded_Prateek@2000");
		savedUser.setRole(request.getRole());
		when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_Prateek@2000");
		when(userRepository.save(any(User.class))).thenReturn(savedUser);

		// Act
		UserDetails userDetails = userService.saveUser(request);

		// Assert
		assertNotNull(userDetails);
		assertEquals(savedUser.getId(), userDetails.getId());
		assertEquals(savedUser.getFirstName(), userDetails.getFirstName());
		assertEquals(savedUser.getLastName(), userDetails.getLastName());
		assertEquals(savedUser.getEmail(), userDetails.getEmail());
		assertEquals(savedUser.getRole(), userDetails.getRole());
		assertEquals(savedUser.getLoggedId(), userDetails.getLoggedId());
		assertEquals(savedUser.getReferenceId(), userDetails.getReferenceId());
	}

    // Test case for successfully updating an existing user
	@Test
	public void testUpdateUser_Success() throws UserNotFoundException {
		// Arrange
		UpdateUserRequest request = new UpdateUserRequest();
		request.setEmail("singh.prateek2599@gmail.com");
		request.setFirstName("UpdatedFirstName");
		request.setLastName("UpdatedLastName");
		request.setPassword("UpdatedPassword");

		//Mock
		User existingUser = new User();
		existingUser.setId("1");
		existingUser.setFirstName("Prateek");
		existingUser.setLastName("Singh");
		existingUser.setEmail("singh.prateek2599@gmail.com");
		existingUser.setLoggedId(false);
		existingUser.setPassword("encoded_Prateek@2000");
		existingUser.setRole("USER");
		User updatedUser = new User();
		updatedUser.setId("1");
		updatedUser.setFirstName("UpdatedFirstName");
		updatedUser.setLastName("UpdatedLastName");
		updatedUser.setEmail("singh.prateek2599@gmail.com");
		updatedUser.setLoggedId(false);
		updatedUser.setPassword("encoded_UpdatedPassword");
		updatedUser.setRole("USER");
		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
		when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_UpdatedPassword");
		when(userRepository.save(any(User.class))).thenReturn(updatedUser);

		// Act
		UserDetails userDetails = userService.updateUser(request);

		// Assert
		assertNotNull(userDetails);
		assertEquals(updatedUser.getId(), userDetails.getId());
		assertEquals(updatedUser.getFirstName(), userDetails.getFirstName());
		assertEquals(updatedUser.getLastName(), userDetails.getLastName());
		assertEquals(updatedUser.getEmail(), userDetails.getEmail());
		assertEquals(updatedUser.getRole(), userDetails.getRole());
		assertEquals(updatedUser.getLoggedId(), userDetails.getLoggedId());
		assertEquals(updatedUser.getReferenceId(), userDetails.getReferenceId());
	}

    // Test case for attempting to update a user that doesn't exist
	@Test
	public void testUpdateUser_UserNotFound() {
		// Arrange
		UpdateUserRequest request = new UpdateUserRequest();
		request.setEmail("singh.prateek2599@gmail.com");
		request.setFirstName("Prateek");
		request.setLastName("Singh");
		request.setPassword("Pra733k@2000");

		//Mock
		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(UserNotFoundException.class, () -> userService.updateUser(request));
	}
	
    // Test case for successfully retrieving a user by email
	@Test
	public void testGetUserByEmail_Success() throws UserNotFoundException {
	    // Arrange
	    String userEmail = "singh.prateek2599@gmail.com";

	    //Mock
	    User existingUser = new User();
	    existingUser.setId("1");
	    existingUser.setFirstName("Prateek");
	    existingUser.setLastName("Singh");
	    existingUser.setEmail(userEmail);
	    existingUser.setLoggedId(false);
	    existingUser.setPassword("encoded_Prateek@2000");
	    existingUser.setRole("USER");
	    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));

	    // Act
	    UserDetails userDetails = userService.getUserByEmail(userEmail);

	    // Assert
	    assertNotNull(userDetails);
	    assertEquals(existingUser.getId(), userDetails.getId());
	    assertEquals(existingUser.getFirstName(), userDetails.getFirstName());
	    assertEquals(existingUser.getLastName(), userDetails.getLastName());
	    assertEquals(existingUser.getEmail(), userDetails.getEmail());
	    assertEquals(existingUser.getRole(), userDetails.getRole());
	    assertEquals(existingUser.getLoggedId(), userDetails.getLoggedId());
	    assertEquals(existingUser.getReferenceId(), userDetails.getReferenceId());
	}
	
    // Test case for attempting to retrieve a user by email that doesn't exist
	@Test
	public void testGetUserByEmail_UserNotFound() {
	    // Arrange
	    String userEmail = "singh.prateek2599@gmail.com";

	    // Mock
	    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

	    // Act and Assert
	    assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(userEmail));
	}
	
    // Test case for successfully adding a reference ID to a user
	@Test
	public void testAddReferenceId_Success() throws UserNotFoundException {
	    // Arrange
	    String userEmail = "singh.prateek2599@gmail.com";
	    String referenceId = "patientRef";

	    //Mock
	    User existingUser = new User();
	    existingUser.setId("1");
	    existingUser.setFirstName("Prateek");
	    existingUser.setLastName("Singh");
	    existingUser.setEmail(userEmail);
	    existingUser.setLoggedId(false);
	    existingUser.setPassword("encoded_Prateek@2000");
	    existingUser.setRole("USER");
	    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
	    when(userRepository.save(any(User.class))).thenReturn(existingUser);

	    // Act
	    UserDetails userDetails = userService.addReferenceId(userEmail, referenceId);

	    // Assert
	    assertNotNull(userDetails);
	    assertEquals(existingUser.getReferenceId(), userDetails.getReferenceId());
	}
	
    // Test case for attempting to add a reference ID to a user that doesn't exist
	@Test
	public void testAddReferenceId_UserNotFound() {
	    // Arrange
	    String userEmail = "singh.prateek2599@gmail.com";
	    String referenceId = "patientRef";

	    // Mock
	    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

	    // Act and Assert
	    assertThrows(UserNotFoundException.class, () -> userService.addReferenceId(userEmail, referenceId));
	}

}