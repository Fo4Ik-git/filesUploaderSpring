package com.fo4ik.webFilesSaver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "file")
public class FileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name, path;
    private boolean isDirectory;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "user_id")
    public User user;

    public FileModel() {
    }

    public FileModel(String name, String path, Boolean isDirectory,  User user) {
        this.name = name;
        this.path = path;
        this.user = user;
        this.isDirectory = isDirectory;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }
}
