package com.wooyeon.yeon.user.controller;

import com.wooyeon.yeon.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
}