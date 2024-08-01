package com.reciperabbit.reciperabbit.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reciperabbit.reciperabbit.model.User;
import com.reciperabbit.reciperabbit.repository.UserRepository;

import com.reciperabbit.reciperabbit.Exception.IncorrectPasswordException;
import com.reciperabbit.reciperabbit.Exception.UserNotFoundException;


@Service
public class UserService {
	
	private final UserRepository userRepo;
	
	@Autowired
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	public User addUser(User user) {
		return userRepo.save(user);
	}
	
	public List<User> findAllUser(){
		return userRepo.findAll();
	}
	
	public User updateUser(User user){
		return userRepo.save(user);			
	}
	
	public User findUser(Long id) {
		return userRepo.findUserById(id).orElseThrow(()-> new UserNotFoundException ("User by id" + id + "was not found."));
	}
	
	@Transactional
	public void deleteUser(Long id) {
		userRepo.deleteUserById(id);
	}
	
	 public User verifyUserCredentials(String username, String password) throws UserNotFoundException {
	        User user = userRepo.findByNameAndPassword(username, password);
	        if (user == null) {
	            throw new UserNotFoundException("User not found or Incorrect Password");
	        }
	        
	        return user;
	    }
	
	
}
