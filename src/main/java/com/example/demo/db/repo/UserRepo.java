package com.example.demo.db.repo;

import com.example.demo.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User getUserByName(String username);
}
