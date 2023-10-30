package com.Capg.User.UserServiceTest;

import com.Capg.User.DTO.SignUpDTO;
import com.Capg.User.DTO.UpdateUserDTO;
import com.Capg.User.DTO.UserDetails;
import com.Capg.User.Exception.UserNotFoundException;
import com.Capg.User.Model.User;
import com.Capg.User.Repository.UserRepository;
import com.Capg.User.Service.UserService;
import com.Capg.User.Service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private static PasswordEncoder passwordEncoder;

    @Test
    public void testAddUser() {
        SignUpDTO signUpDTO = createSignUpDTO();
        User user = createUser();
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDetails userDetails = userService.addUser(signUpDTO);

        assertNotNull(userDetails);
        assertEquals(userDetails.getFirstName(), signUpDTO.getFirstName());
        assertEquals(userDetails.getLastName(), signUpDTO.getLastName());
        assertEquals(userDetails.getEmailId(), signUpDTO.getEmailId());
        assertEquals(userDetails.getRole(), signUpDTO.getRole());

        verify(passwordEncoder, times(1)).encode(signUpDTO.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser() throws UserNotFoundException {
        UpdateUserDTO updateUserDTO = createUpdateUserDTO();
        User existingUser = createUser();

        when(userRepository.findByEmailId(updateUserDTO.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(updateUserDTO);

        assertNotNull(updatedUser);
        assertEquals(updatedUser.getFirstName(), updateUserDTO.getFirstName());
        assertEquals(updatedUser.getLastName(), updateUserDTO.getLastName());
        assertEquals(updatedUser.getEmailId(), updateUserDTO.getEmail());

        verify(userRepository, times(1)).findByEmailId(updateUserDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(updateUserDTO.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
//
    @Test
    public void testGetUserByEmail() throws UserNotFoundException {
        String email = "sarvi0830@gmail.com";

        User user = createUser(); // Use the actual encoded password here

        when(userRepository.findByEmailId(email)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail(email);

        assertNotNull(foundUser);
        assertEquals(foundUser.getEmailId(), email);
        // Check other fields as needed

        verify(userRepository, times(1)).findByEmailId(email);
    }
//
    @Test
    public void testAddReferenceId() throws UserNotFoundException {
        String emailId = "sarvi0830@gmail.com";
        String referenceId = "abcd123";

        User user = createUser();

        when(userRepository.findByEmailId(emailId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.addReferenceId(emailId, referenceId);

        assertNotNull(updatedUser);
        assertEquals(updatedUser.getEmailId(), emailId);
        assertEquals(updatedUser.getReferenceId(), referenceId);
        // Check other fields as needed

        verify(userRepository, times(1)).findByEmailId(emailId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    private static SignUpDTO createSignUpDTO() {
        SignUpDTO obj = new SignUpDTO();
        obj.setFirstName("Sarvesh");
        obj.setLastName("LV");
        obj.setEmailId("sarvi0830@gmail.com");
        obj.setPassword("S@rvi410830");
        obj.setRole("USER");
        return obj;
    }

    private static User createUser() {
        User obj = new User();
        obj.setUserId("abcd123");
        obj.setFirstName("Sarvesh");
        obj.setLastName("LV");
        obj.setRole("USER");
        obj.setEmailId("sarvi0830@gmail.com");
        obj.setReferenceId(null);
        obj.setPassword("S@rvi410830");
        return obj;
    }

    private static UpdateUserDTO createUpdateUserDTO() {
        UpdateUserDTO obj = new UpdateUserDTO();
        obj.setEmail("sarvi0830@gmail.com");
        obj.setFirstName("Sarvesh");
        obj.setLastName("LV");
        obj.setPassword("S@rvi410830");
        return obj;
    }
}
