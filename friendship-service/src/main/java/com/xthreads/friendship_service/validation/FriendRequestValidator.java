package com.xthreads.friendship_service.validation;

import com.xthreads.friendship_service.dto.request.SendFriendRequest;
import com.xthreads.friendship_service.exception.AppException;
import com.xthreads.friendship_service.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestValidator {
    public void validateSendRequest(SendFriendRequest request) {
        if (request.getSenderId() == null || request.getReceiverId() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        if (request.getSenderId().equals(request.getReceiverId())) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
    }
}
