package com.Capg.User.UserRepositoryTest;

import com.Capg.User.Model.User;
import com.Capg.User.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindByEmailId_ExistingUser() {
        String emailId = "sarvi0830@gmail.com";
        User user = createUser();

        when(userRepository.findByEmailId(emailId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByEmailId(emailId);

        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get().getEmailId(), emailId);

        verify(userRepository, times(1)).findByEmailId(emailId);
    }

    @Test
    public void testFindByEmailId_NonExistingUser() {
        String emailId = "nonexistent@example.com";
        when(userRepository.findByEmailId(emailId)).thenReturn(Optional.empty());
        Optional<User> foundUser = userRepository.findByEmailId(emailId);
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByEmailId(emailId);
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
}
