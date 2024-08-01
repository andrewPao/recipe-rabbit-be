package com.reciperabbit.reciperabbit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reciperabbit.reciperabbit.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	void deleteUserById(Long id);

	Optional<User> findUserById(Long id);
	
	User findByNameAndPassword(String username, String password);

}
