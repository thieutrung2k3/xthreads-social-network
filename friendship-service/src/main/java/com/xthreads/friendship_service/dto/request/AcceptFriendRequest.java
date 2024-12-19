package com.xthreads.friendship_service.dto.request;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class AcceptFriendRequest {
    @NotNull
    String id;
}
