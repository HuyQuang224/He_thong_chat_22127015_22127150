package model;

public class groupChat {
    private int id;
    private String name;
    private String created_at;

    public groupChat(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
