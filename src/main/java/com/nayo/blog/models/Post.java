package com.nayo.blog.models;

import javax.persistence.*;

@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable=true)
    private String hashTags;

    @OneToOne
    private User user;

    public Post(){
    }

    public Post(String title, String body, String hashTags){
        this.title = title;
        this.body = body;
        this.hashTags = hashTags;
    }

    public Post(String title, String body, String hashTags, User user){
        this.title = title;
        this.body = body;
        this.hashTags = hashTags;
        this.user= user;
    }

    //this constructor is used with the edit post method
    public Post(long id, String title, String body, User user, String hashTag) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.user= user;
        this.hashTags = hashTags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHashTags(){
        return hashTags;
    }

    public void setHashTags(String hashTags){
        this.hashTags = hashTags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
