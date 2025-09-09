public class User {
    private String name;
    private String password;
    private String fullName;
    private String contact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return fullName + " (" + name + ")";
    }

    public User(String name, String password, String fullName, String contact) {
        this.name = name;
        this.password = password;
        this.fullName = fullName;
        this.contact = contact;
    }
}
