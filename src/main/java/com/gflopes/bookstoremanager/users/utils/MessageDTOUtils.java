package com.gflopes.bookstoremanager.users.utils;

import com.gflopes.bookstoremanager.users.dto.MessageDTO;
import com.gflopes.bookstoremanager.users.entity.User;

public class MessageDTOUtils {

    public static MessageDTO createdMessage(User createdUser) {
        return returnMessage(createdUser, "created");
    }

    public static MessageDTO updatedMessage(User updatedUser) {
        return returnMessage(updatedUser, "updated");
    }

    public static MessageDTO returnMessage(User user, String action) {
        String updatedUsername = user.getUsername();
        Long updateId = user.getId();
        String updateStringUserMessage = String.format("User %s with ID %s successfully %s", updatedUsername, updateId, action  );
        return MessageDTO.builder()
                .message(updateStringUserMessage)
                .build();
    }
}
