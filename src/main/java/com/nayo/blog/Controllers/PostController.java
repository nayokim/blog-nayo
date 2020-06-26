package com.nayo.blog.Controllers;

import com.nayo.blog.dao.PostsRepository;
import com.nayo.blog.models.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {
    //dependency injection
    private PostsRepository postsDao;//dao = data access object
    public PostController(PostsRepository postsRepository){
        this. postsDao = postsRepository;
    }

    @GetMapping("/posts")
    public String showPosts(Model model){
        //grabs a list of all the posts in the db
        List<Post> posts = postsDao.findAll();
        model.addAttribute("posts", posts);
        return "home/index";
    }

    @GetMapping("/posts/{id}")
    public String onePost(@PathVariable long id, Model model){
        Post postToView = postsDao.getOne(id);
        //"post" is what is in the view and postToView is what is being passed
        model.addAttribute("post",postToView);
        return "blog/show";
    }

    @GetMapping("/posts/create")
    public String save(){
        return "blog/create";
    }

    @PostMapping("/posts/create")
    public String viewPost(
            //add all the parameters from the create form
            @RequestParam(name="title") String title,
            @RequestParam(name="body") String body){
      Post postToAdd = new Post(title,body);
      //save the data in the database.
      Post postinDB = postsDao.save(postToAdd);
      //redirect to the page. THis is mapping to the URL NOT THE VIEW (TEMPLATE)
        System.out.println(postinDB.getId());
        return "redirect:/posts";
    }


    @GetMapping("/posts/{id}/edit")
    public String showEditForm(Model model, @PathVariable long id){
        //find a post
        Post postToEdit = postsDao.getOne(id);
        model.addAttribute("post",postToEdit);
        return "blog/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String update(@PathVariable long id,
                         @RequestParam(name="title") String title,
                         @RequestParam (name ="body") String body){
        //finds post
        Post updatePost = postsDao.getOne(id);

        //edit the post
        updatePost.setTitle(title);
        updatePost.setBody((body));

        //save changes
        postsDao.save(updatePost);
        return "redirect:/posts";

    }

    @PostMapping("/posts/{id}/delete")
    public String destroy(@PathVariable long id){
        postsDao.deleteById(id);
        return "redirect:/posts";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam (name="term") String term){
        List <Post> posts = postsDao.searchByTitle(term);
        model.addAttribute("posts",posts);
        return "home/index";
    }



}
