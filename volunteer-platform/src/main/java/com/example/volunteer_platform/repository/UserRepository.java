package com.example.volunteer_platform.repository;
import  org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.volunteer_platform.model.User;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	User findByEmail(String email);

}
