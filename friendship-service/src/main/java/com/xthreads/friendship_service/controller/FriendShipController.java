package com.xthreads.friendship_service.controller;

import com.xthreads.friendship_service.service.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendShipController {
    private final FriendShipService friendShipService;


}
