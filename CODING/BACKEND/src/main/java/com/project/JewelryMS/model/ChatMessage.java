package com.project.JewelryMS.model;

import com.project.JewelryMS.enumClass.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String timestamp;
    private RoleEnum role;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

}
