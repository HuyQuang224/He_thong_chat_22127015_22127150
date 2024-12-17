package model;

public class groupMessage {
    private int id;
    private int fromUserId;
    private int toGroupId;
    private String content;

    public groupMessage(int id, int fromUserId, int toGroupId, String content) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toGroupId = toGroupId;
        this.content = content;
    }

    public int getId() {
        return this.id;
    }

    public int getFromUserId() {
        return this.fromUserId;
    }

    public int getToGroupId() {
        return this.toGroupId;
    }

    public String getContent() {
        return this.content;
    }
}
