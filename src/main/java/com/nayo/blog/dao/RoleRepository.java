package com.nayo.blog.dao;


import com.nayo.blog.models.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface RoleRepository extends CrudRepository<UserRole, Long> {
    @Query("SELECT ur.role FROM UserRole ur, User u WHERE u.username=?1 and ur.userId = u.id")
    List<String> ofUserWith(String username);
}