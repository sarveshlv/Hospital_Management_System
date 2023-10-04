package com.hms.userms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;

import com.hms.userms.entity.User;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserRepository userRepositoryMock;

    // Test case for finding a user by email when the user exists (asserts email value)
    @Test
    public void testFindByEmail() {
    	//Arrange
        User existingUser = new User("1", "singh.prateek2599@gmail.com", "Prateek@2000", "Prateek", "Singh", "USER",
                true, "patientRef");
        
        //Mock
        Mockito.when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        	
        //Act
        Optional<User> actualUser = userRepository.findByEmail("singh.prateek2599@gmail.com");
        
        //Assert
        assertEquals("singh.prateek2599@gmail.com", actualUser.orElseThrow().getEmail());
    }

    // Test case for finding a user by email when the user doesn't exist (asserts optional empty)
    @Test
    public void testFindByEmailNotFound() {
    	//Arrange and Mock
        Mockito.when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        //Act
        Optional<User> user = userRepository.findByEmail("singh.prateek2599@gmail.com");

        //Assert
        assertEquals(Optional.empty(), user);
    }
}