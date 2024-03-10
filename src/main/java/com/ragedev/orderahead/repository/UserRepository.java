package com.ragedev.orderahead.repository;

import com.ragedev.orderahead.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);
}
