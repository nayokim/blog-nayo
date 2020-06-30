package com.nayo.blog.Controllers;

import com.nayo.blog.dao.PostsRepository;
import com.nayo.blog.dao.UsersRepository;
import com.nayo.blog.models.Post;
import com.nayo.blog.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {
    //dependency injection
    private PostsRepository postsDao;//dao = data access object
    private UsersRepository usersDao;

    public PostController(PostsRepository postsRepository, UsersRepository usersDao) {
        this.postsDao = postsRepository;
        this.usersDao = usersDao;
    }

    @GetMapping("/posts")
    public String showPosts(Model model) {
        //grabs a list of all the posts in the db
        List<Post> posts = postsDao.findAll();
        model.addAttribute("posts", posts);
        return "home/index";
    }

    @GetMapping("/posts/{id}")
    public String onePost(@PathVariable long id, Model model) {
        Post postToView = postsDao.getOne(id);
        //"post" is what is in the view and postToView is what is being passed
        model.addAttribute("post", postToView);
        return "blog/show";
    }

    @GetMapping("/posts/create")
    public String showForm(Model viewModel) {
        viewModel.addAttribute("post", new Post());
        return "blog/create";
    }

    @PostMapping("/posts/create")
    public String viewPost(@ModelAttribute Post postToBeSaved) {
        User currentUser = usersDao.getOne(1L);
        //save the data in the database.
        postToBeSaved.setUser(currentUser);
        Post savedPost = postsDao.save(postToBeSaved);
        return "redirect:/posts";
    }


    @GetMapping("/posts/{id}/edit")
    public String showEditForm(Model model, @PathVariable long id) {
        //find a post
        Post postToEdit = postsDao.getOne(id);
        model.addAttribute("post", postToEdit);
        return "blog/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String update(@ModelAttribute Post postToEdit) {
        User currentUser = usersDao.getOne(1L);
        postToEdit.setUser(currentUser);

        //save the changes
        postsDao.save(postToEdit);// update posts set title =? where id=?
        return "redirect:/posts/" + postToEdit.getId();

    }

    @PostMapping("/posts/{id}/delete")
    public String destroy(@PathVariable long id) {
        postsDao.deleteById(id);
        return "redirect:/posts";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(name = "term") String term) {
        List<Post> posts = postsDao.searchByTitle(term);
        model.addAttribute("posts", posts);
        return "home/index";
    }


}
