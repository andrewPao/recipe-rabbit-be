package com.reciperabbit.reciperabbit.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reciperabbit.reciperabbit.Exception.IncorrectPasswordException;
import com.reciperabbit.reciperabbit.Exception.UserNotFoundException;
import com.reciperabbit.reciperabbit.Service.UserService;
import com.reciperabbit.reciperabbit.model.LoginRequest;
import com.reciperabbit.reciperabbit.model.User;
import com.reciperabbit.reciperabbit.response.Response;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> userList = userService.findAllUser();
		
		return new ResponseEntity<>(userList, HttpStatus.OK);
	} 
	
	@PostMapping("/findSingleUser")
	public ResponseEntity<User> getUserById(@RequestBody LoginRequest loginRequest){
		 try {
		        User user = userService.findUser(loginRequest.getUsername(), loginRequest.getPassword());
		        return new ResponseEntity<>(user, HttpStatus.OK);
		    } catch (UserNotFoundException ex) {
		        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		    }
	} 
	
	@PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody LoginRequest loginRequest) {
        
        try {
        	User user = userService.findUser(loginRequest.getUsername(), loginRequest.getPassword());
        	if(user.getRole().equals("admin")){
        		return new ResponseEntity<>(new Response("admin"), HttpStatus.OK);
        	}
        	return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
        
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new Response("failed"), HttpStatus.OK);
        } 
        
//        catch (IncorrectPasswordException e) {
//            return new ResponseEntity<>(new Response("Incorrect password"), HttpStatus.OK);
//        }
    }
	
	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User requestUser){
		requestUser.setRole("user");
		User user = userService.addUser(requestUser);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<User> updateUser(@RequestBody User requestUser){
		User user = userService.updateUser(requestUser);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@Transactional
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id){
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.OK);
	} 
	

}
