package com.xthreads.friendship_service.service;

import com.xthreads.friendship_service.repository.FriendShipRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendShipService {
    final FriendShipRepository friendShipRepository;



}
