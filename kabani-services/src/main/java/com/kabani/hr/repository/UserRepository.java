package com.kabani.hr.repository;

import org.springframework.data.repository.CrudRepository;

import com.kabani.hr.entity.User;


 

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
	User findByNameAndPassword(String name, String password);

}