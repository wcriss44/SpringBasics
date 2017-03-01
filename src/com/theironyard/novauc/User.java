package com.theironyard.novauc;

import java.util.HashMap;

public class User {
    private String name;
    private String password;
     HashMap<Integer, String> post = new HashMap<>();

    public User(String name, String password){
        this.name = name;
        this.password = password;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public void addPost(String newPost){
        if (post.isEmpty()){
           post.put(1, newPost);
        } else {
            post.put((post.size() + 1), newPost);
        }
    }
    public int getPostSize(){
        return post.size();
    }
    public String getPost(int n){
        return post.get(n);
    }
}
