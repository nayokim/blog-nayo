package com.nayo.blog;

import com.nayo.blog.BlogApplication;
import com.nayo.blog.dao.PostsRepository;
import com.nayo.blog.dao.UsersRepository;
import com.nayo.blog.models.Post;
import com.nayo.blog.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//classes = BlogApplication.class : needs to match your blog name
@SpringBootTest(classes = BlogApplication.class)
@AutoConfigureMockMvc
public class PostsIntegrationTests<UserRepository> {

    private User testUser;
    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UsersRepository userDao;

    @Autowired
    PostsRepository postsDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testUser");

        // Creates the test user if not exists
        if(testUser == null){
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setPassword(passwordEncoder.encode("pass"));
            newUser.setEmail("testUser@codeup.com");
            testUser = userDao.save(newUser);
        }

        // Throws a Post request to /login and expect a redirection to the Ads index page after being logged in
        httpSession = this.mvc.perform(post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "pass"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/posts"))
                .andReturn()
                .getRequest()
                .getSession();
    }

    @Test
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() throws Exception {
        // It makes sure the returned session is not null
        assertNotNull(httpSession);
    }


    // For this test we want to make a HTTP POST request
    //CRUD - Create

    @Test
    public void testCreatePost() throws Exception {
        // Makes a Post request to /posts/create and expect a redirection to the post
        this.mvc.perform(
                post("/posts/create").with(csrf())
                        .session((MockHttpSession) httpSession)
                        // Add all the required parameters to your request like this
                        .param("title", "test")
                        .param("body", "for sale"))
                .andExpect(status().is3xxRedirection());
    }

    //Check if posts index ("/posts") and show ("posts/{id}") will render correctly
    //CRUD - Read

    @Test
    public void testShowPost() throws Exception {

        Post existingPost = postsDao.findAll().get(0);

        // Makes a Get request to /posts/{id} and expect a redirection to the post show page
        this.mvc.perform(get("/posts/" + existingPost.getId()))
                .andExpect(status().isOk())
                // Test the dynamic content of the page
                .andExpect(content().string(containsString(existingPost.getBody())));
    }

    @Test
    public void testPostsIndex() throws Exception {
        Post existingPost = postsDao.findAll().get(0);

        // Makes a Get request to /postss and verifies that we get some of the static text of the ads/index.html template and at least the title from the first post is present in the template.
        this.mvc.perform(get("/posts"))
                .andExpect(status().isOk())
                // Test the static content of the page
                //containsString("")  - looking for  content on the page
                .andExpect(content().string(containsString("test")))
                // Test the dynamic content of the page
                .andExpect(content().string(containsString(existingPost.getTitle())));
    }

}