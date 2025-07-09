package com.chithanh.italk.chat.domain.enums;

public enum JoinStatus {
    PENDING, // User has requested to join the community
    ACCEPTED, // User has been accepted into the community
    REJECTED, // User's request to join has been rejected
    LEFT, // User has left the community
    BANNED, // User has been banned from the community
    ALREADY_MEMBER // User is already a member of the community
}