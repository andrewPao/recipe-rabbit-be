package com.reciperabbit.reciperabbit.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reciperabbit.reciperabbit.Service.UserService;
import com.reciperabbit.reciperabbit.model.LoginRequest;
import com.reciperabbit.reciperabbit.model.User;
import com.reciperabbit.reciperabbit.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TestUserController {
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetAllUsers() throws Exception {
    	
    	List<User> userList = userService.findAllUser();
        assertThat(userList).isNotEmpty();
       
        mockMvc.perform(get("/user/findAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    void testGetAdminByLoginRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");
        
        User user = userService.findUser(loginRequest.getUsername(), loginRequest.getPassword());
        
        mockMvc.perform(post("/user/findSingleUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()); 

       Assertions.assertTrue(user.getName().equals(loginRequest.getUsername()) && user.getPassword().equals(loginRequest.getPassword()));
    }
    
    @Test
    void testUserNotFound() throws Exception {
    	
    	LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("NotFound");
        loginRequest.setPassword("NotFound");

        mockMvc.perform(post("/user/findSingleUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound()); 
    }

    @Test
    void testLoginUserWithAdminRole() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");
        
        User user = userService.findUser(loginRequest.getUsername(), loginRequest.getPassword());
        
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()); 
 
       Assertions.assertTrue(user.getRole().equals("admin"));
      
    }
    
    @Test
    void testLoginUserNotAdminRole() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Harry");
        loginRequest.setPassword("potter");
        
        User user = userService.findUser(loginRequest.getUsername(), loginRequest.getPassword());
        
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()); 
 
       Assertions.assertTrue(!user.getRole().equals("admin"));
      
    }
    
    @Test
    void testLoginNoUserFound() throws Exception{
    	
    	LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("NotFound");
        loginRequest.setPassword("NotFound");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()); 
    }
    
    @Test
    void testAddUser() throws Exception {
        
        User requestUser = new User();
        requestUser.setName("testuser");
        requestUser.setEmailAddress("testuser@example.com");
        requestUser.setPassword("testpassword");
        requestUser.setPhoneNumber("1234567");

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestUser)))
                .andExpect(status().isCreated());
    }
    
    @Test 
    void testUpdateUser() throws Exception{
    	LoginRequest userLogin = new LoginRequest();
	    userLogin.setUsername("Harry");
	    userLogin.setPassword("potter");
	         
        User beforeUpdate = userService.findUser(userLogin.getUsername(), userLogin.getPassword());
        beforeUpdate.setEmailAddress("updatedEmail@hotmail.com");
    		
    	
    	MvcResult result = mockMvc.perform(put("/user/update")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(beforeUpdate)))
    			.andExpect(status().isOk())
    			.andReturn();
    	
    	String jsonResponse = result.getResponse().getContentAsString();
    	User afterUpdate = objectMapper.readValue(jsonResponse, User.class);
    	
    	Assertions.assertTrue(beforeUpdate.getEmailAddress().equals(afterUpdate.getEmailAddress()));
  
    	
    }
    

    @Test
    void testDeleteUserById() throws Exception {
        Long userId = 46L;
        mockMvc.perform(delete("/user/delete/{id}", userId))
        .andExpect(status().isOk()); 
    }
}
