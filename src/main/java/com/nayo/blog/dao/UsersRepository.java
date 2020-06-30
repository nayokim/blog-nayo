package com.nayo.blog.dao;

import com.nayo.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository <User, Long>{
}