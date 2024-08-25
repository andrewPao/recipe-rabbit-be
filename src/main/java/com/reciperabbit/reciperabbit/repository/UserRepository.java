package com.reciperabbit.reciperabbit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reciperabbit.reciperabbit.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	void deleteUserById(Long id);

	Optional<User> findUserById(Long id);
	
	User findByNameAndPassword(String username, String password);

}
