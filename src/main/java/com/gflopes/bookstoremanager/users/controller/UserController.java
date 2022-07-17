package com.gflopes.bookstoremanager.users.controller;

import com.gflopes.bookstoremanager.publishers.service.PublisherService;
import com.gflopes.bookstoremanager.users.controller.docs.UserControllerDocs;
import com.gflopes.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDocs {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


}
