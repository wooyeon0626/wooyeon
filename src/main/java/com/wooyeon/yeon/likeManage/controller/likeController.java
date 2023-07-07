package com.wooyeon.yeon.likeManage.controller;

import com.wooyeon.yeon.likeManage.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Controller
public class likeController {
    private final LikeService likeService;
}
