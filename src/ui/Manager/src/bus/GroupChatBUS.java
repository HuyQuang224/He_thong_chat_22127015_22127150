package bus;

import dao.GroupChatDAO;
import datastructure.GroupChat;
import java.util.ArrayList;

public class GroupChatBUS {
    private GroupChatDAO groupChatDAO;

    public GroupChatBUS() {
        this.groupChatDAO = new GroupChatDAO();
    }

    public ArrayList<GroupChat> fetchAllGroupChats() {
        return groupChatDAO.getAllGroupChats();
    }

    // Lấy nhóm chat theo ID
    public GroupChat getGroupChatById(int groupChatId) {
        return groupChatDAO.getGroupChatById(groupChatId);
    }
}
