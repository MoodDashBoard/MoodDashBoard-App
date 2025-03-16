package model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private ObjectId id;
    private String name;
    private String surname;
    private Date birthdate;
    private String email;
    private String avatar;
    private String password;
    private List<MoodEntry> records;

    public User() {
        this.records = new ArrayList<>();
    }

    public User(String name, String surname, Date birthdate, String email, String avatar, String password) {
        this();
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<MoodEntry> getRecords() {
        return records;
    }

    public void setRecords(List<MoodEntry> records) {
        this.records = records;
    }

    public void addMoodEntry(MoodEntry entry) {
        this.records.add(entry);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthdate=" + birthdate +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", password='" + password + '\'' +
                ", records=" + records +
                '}';
    }
}