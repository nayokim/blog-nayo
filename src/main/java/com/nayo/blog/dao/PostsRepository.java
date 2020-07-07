package com.nayo.blog.dao;

import com.nayo.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Long> {


    Post findById(long id);

    @Query
            ("from Post as p where p.title like %:term% or p.body like %:term% or p.hashTags like %:term% ")
    List<Post> searchByTitle(@Param("term") String term);

    @Query
            ("from Post as p where p.hashTags like %:hashtags%")
    List<Post> searchByHash(@Param("hashtags")String hashtags);


    Post findByTitle(String ad_to_be_deleted);
}
