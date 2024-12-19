package com.xthreads.friendship_service.mapper;

import com.xthreads.friendship_service.dto.request.SendFriendRequest;
import com.xthreads.friendship_service.dto.response.AcceptFriendResponse;
import com.xthreads.friendship_service.model.FriendRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FriendShipMapper {
    FriendRequest toFriendRequest(SendFriendRequest request);
    AcceptFriendResponse toAcceptFriendResponse(FriendRequest friendRequest);
}
