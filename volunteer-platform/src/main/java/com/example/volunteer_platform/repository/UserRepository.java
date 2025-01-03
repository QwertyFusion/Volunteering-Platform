package com.example.volunteer_platform.repository;
import  com.example.volunteer_platform.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	User findByEmail(String email);

}
