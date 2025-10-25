package com.M.act1.repository;

import com.M.act1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // returns null if not found (we use null-checks in service)
    User findByUsername(String username);
}
