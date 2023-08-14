package com.wooyeon.yeon.profileChoice.controller;

import com.wooyeon.yeon.profileChoice.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
}
