package com.Capg.User.UserControllerTest;

import com.Capg.User.Controller.UserController;
import com.Capg.User.DTO.SignUpDTO;
import com.Capg.User.DTO.UpdateUserDTO;
import com.Capg.User.DTO.UserDetails;
import com.Capg.User.Exception.CentralExceptionHandler;
import com.Capg.User.Model.User;
import com.Capg.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new CentralExceptionHandler())
                .build();
    }

    @Test
    public void testSignupUser() throws Exception {
        SignUpDTO signupDTO = createSignUpDTO();
        UserDetails userDetails = createUserDetails();

        when(userService.addUser(any(SignUpDTO.class))).thenReturn(userDetails);

        mockMvc.perform(post("/User/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(userDetails.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDetails.getLastName()));

        verify(userService, times(1)).addUser(any(SignUpDTO.class));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UpdateUserDTO updateUserDTO = createUpdateUserDTO();
        User updatedUser = createUser();

        when(userService.updateUser(any(UpdateUserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/User/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("$.emailId").value(updatedUser.getEmailId()));

        verify(userService, times(1)).updateUser(any(UpdateUserDTO.class));
    }

    //
    @Test
    public void testGetUserByEmailId() throws Exception {
        String emailId = "sarvi0830@gmail.com";
        User user = createUser();

        when(userService.getUserByEmail(emailId)).thenReturn(user);

        mockMvc.perform(get("/User/findByEmailId/{emailId}", emailId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));

        verify(userService, times(1)).getUserByEmail(emailId);
    }

    //
    @Test
    public void testAddReference() throws Exception {
        String emailId = "sarvi0830@gmail.com";
        String referenceId = "abcd123";
        User user = createUser();

        when(userService.addReferenceId(emailId, referenceId)).thenReturn(user);

        mockMvc.perform(get("/User/addReference/{emailId}/{referenceId}", emailId, referenceId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.referenceId").value(user.getReferenceId()));

        verify(userService, times(1)).addReferenceId(emailId, referenceId);
    }

    // Utility method to convert object to JSON string
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
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

    private static UserDetails createUserDetails() {
        UserDetails obj = new UserDetails();
        obj.setUserId("asada");
        obj.setFirstName("Sarvesh");
        obj.setLastName("LV");
        obj.setRole("USER");
        obj.setEmailId("sarvi0830@gmail.com");
        obj.setReferenceId(null);
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
