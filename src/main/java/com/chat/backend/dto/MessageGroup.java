package com.chat.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageGroup {
    // Yesterday, today, unread messages
    private String date;

    private LocalDate localDate;

    private List<DecryptedChatMessage> messages;

    /**
     * this comparator will reverse the message group order.
     * i.e 2022-02-19 will be shown before 2022-02-20
     */
    public static class MessageGroupComparator implements Comparator<MessageGroup> {
        @Override
        public int compare(MessageGroup o1, MessageGroup o2) {
            return o1.getLocalDate().isAfter(o2.getLocalDate()) ? 0 : -1;
        }
    }
}
