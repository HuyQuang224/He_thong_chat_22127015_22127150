package model;

public class Message {
    private int id;
    private int fromUser;
    private int toUser;
    private String content;
    private int visibleOnly;

    public Message(int id, int fromUser, int toUser, String content, int visibleOnly) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.visibleOnly = visibleOnly;
    }

    public int getId() {
        return this.id;
    }

    public int getFromUser() {
        return this.fromUser;
    }

    public int getToUser() {
        return this.toUser;
    }

    public String getContent() {
        return this.content;
    }

    public int getVisibleOnly() {
        return this.visibleOnly;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFromUser(int fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(int toUser) {
        this.toUser = toUser;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVisibleOnly(int visibleOnly) {
        this.visibleOnly = visibleOnly;
    }
}


