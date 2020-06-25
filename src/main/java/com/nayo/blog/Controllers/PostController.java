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
    private PostsRepository postsDao;
    public PostController(PostsRepository postsRepository){
        postsDao = postsRepository;
    }

    @GetMapping("/posts")
    public String showPosts(Model model){
        List<Post> posts = postsDao.findAll();
        model.addAttribute("posts", posts);
        return "home/index";
    }

    @GetMapping("/posts/{id}")
    public String onePost(@PathVariable long id, Model model){
        Post post = new Post(4,"Yesterday","Oh I believe in yesterday");
        model.addAttribute("post",post);
        return "blog/show";
    }


    @GetMapping("/posts/create")
    @ResponseBody
    public String viewPost(){
        return "get";
    }

    @PostMapping("/posts/create")
    @ResponseBody
    public String save(){
        Post newPost = new Post("test 5", "test 5");
        postsDao.save(newPost);
        return "create a new ad";
    }

    @GetMapping("/posts/{id}/edit")
    public String showEditForm(Model model, @PathVariable long id){
        //find a post
        Post postToEdit = postsDao.getOne(id);
        model.addAttribute("post",postToEdit);
        return "blog/edit";
    }

    @PostMapping("/posts/{id}/edit")
    @ResponseBody
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
        return "post updated";

    }

    @PostMapping("/posts/{id}/delete")
    @ResponseBody
    public String destroy(@PathVariable long id){
        postsDao.deleteById(id);
        return "blog deleted";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam (name="term") String term){
        List <Post> posts = postsDao.searchByTitle(term);
        model.addAttribute("posts",posts);
        return "home/index";
    }



}
