package pojo;

public class User {
    private String name;
    private String bloodgroup;
    private String id;
    private String email;
    private String phonenumber;
    private String age;
    private String profilepictureurl;
    private String search;
    private String type;
    public User(){}

    public User(String name, String bloodgroup, String id, String email, String phonenumber, String age,String profilepictureurl, String search, String type) {
        this.name = name;
        this.bloodgroup = bloodgroup;
        this.id = id;
        this.email = email;
        this.phonenumber = phonenumber;
        this.age=age;
        this.profilepictureurl = profilepictureurl;
        this.search = search;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }


    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
