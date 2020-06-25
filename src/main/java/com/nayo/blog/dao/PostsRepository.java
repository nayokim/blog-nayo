package com.nayo.blog.dao;

import com.nayo.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository  extends JpaRepository <Post, Long>{
}
