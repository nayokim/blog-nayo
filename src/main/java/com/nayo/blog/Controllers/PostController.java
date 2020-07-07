package com.nayo.blog.Controllers;

import com.nayo.blog.dao.PostsRepository;
import com.nayo.blog.dao.UsersRepository;
import com.nayo.blog.services.EmailService;
import com.nayo.blog.models.Post;
import com.nayo.blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    //dependency injection
    private PostsRepository postsDao;//dao = data access object
    private UsersRepository usersDao;
    private EmailService emailService;

    public PostController(PostsRepository postsRepository, UsersRepository usersDao, EmailService emailService) {
        this.postsDao = postsRepository;
        this.usersDao = usersDao;
        this.emailService = emailService;
    }


    @GetMapping("/posts")
    public String showPosts(Model model) {
        //grabs a list of all the posts in the db
        List<Post> posts = postsDao.findAll();
        model.addAttribute("posts", posts);
        return "blog/index";
    }

//    @GetMapping("/posts/{id}")
//    public String onePost(@PathVariable long id, Model model) {
//        Post postToView = postsDao.getOne(id);
//        //"post" is what is in the view and postToView is what is being passed
//        model.addAttribute("post", postToView);
//        return "blog/show";
//    }

    //    SHOW POST DETAILS
    @GetMapping("/posts/{id}")
    public String viewOnePost(@PathVariable long id, Model model) {
        model.addAttribute("post", postsDao.findById(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userId", user.getId());
        return "blog/show";
    }

    @GetMapping("/posts/create")
    public String showForm(Model viewModel) {
        viewModel.addAttribute("post", new Post());
        return "blog/create";
    }

    @PostMapping("/posts/create")
    public String viewPost(@ModelAttribute Post postToBeSaved) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //save the data in the database.
        postToBeSaved.setUser(currentUser);
        Post savedPost = postsDao.save(postToBeSaved);
        emailService.prepareAndSend(savedPost, "New Post", "A new Post has been created by " + savedPost.getUser());
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
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        return "blog/index";
    }

    @GetMapping("/hashtag")
    public String hashRep(Model model, @RequestParam(name = "hashtags") String hashtags) {
        List<Post> posts = postsDao.searchByHash(hashtags);
        model.addAttribute("hashtags", hashtags);
        return "blog/index";
    }


}