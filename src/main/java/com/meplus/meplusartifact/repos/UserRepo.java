package com.meplus.meplusartifact.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import com.meplus.meplusartifact.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long>{
    
    Boolean existsByUsername(String username);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query("UPDATE User u SET u.firstName=:firstName, u.lastName=:lastName WHERE u.id=:id")
    public void updateUserInfo(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT u FROM User u WHERE u.username=:username AND u.password=:password ")
    public List<User> loginUser(@Param("username") String username, @Param("password") String password);
}
