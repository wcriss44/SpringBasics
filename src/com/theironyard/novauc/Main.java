package com.theironyard.novauc;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {
    public static HashMap<String, User> userAccounts = new HashMap<>();
    public static boolean loggedIn = false;
    public static String loginName;

    public static void main(String[] args) {
        Spark.staticFileLocation("/styles");
        Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {
                    return new ModelAndView(userAccounts,"index.html" );
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/home.html",
                ((request, response) -> {
                    if (loggedIn) {
                        HashMap <String, String> m= new HashMap();
                        String post = "";
                        if (userAccounts.get(loginName).getPostSize() > 0){
                            for (int i = 1; i <= userAccounts.get(loginName).getPostSize(); i++){
                                post += String.format("<p>%s) %s</p>", i, userAccounts.get(loginName).getPost(i));
                            }
                            m.put("post", post);
                            return new ModelAndView(m, "home.html");
                        }
                        return new ModelAndView(m, "home.html");
                    } else {
                        return new ModelAndView(userAccounts, "index.html");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/registration.html",
                ((request, response) -> {
                    return new ModelAndView(userAccounts, "registration.html");
        }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/viewall.html",
                ((request, response) -> {
                    String post = "";
                    HashMap<String, String>m = new HashMap<>();
                    for(User user: userAccounts.values()){
                        post += String.format("<h4>%s</h4>", user.getName());
                        for(int i = 1; i <= user.getPostSize(); i++){
                            post += String.format("<p>%s) %s</p>", i, user.getPost(i));
                        }
                    }
                    m.put("post", post);
                    return new ModelAndView(m, "viewall.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/logout.html",
                ((request, response) -> {
                    loggedIn = false;
                    loginName = "";
                    return new ModelAndView(userAccounts, "index.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    if (userAccounts.containsKey(name)){
                        if (userAccounts.get(name).getPassword().equals(password)){
                            loggedIn = true;
                            loginName = name;
                            response.redirect("home.html");
                        } else {
                            response.redirect("/");
                        }
                    } else {
                        response.redirect("/");

                    }
                    return "";
                })
        );
        Spark.post(
                "/registration",
                ((request, response) -> {
                    String name = request.queryParams("registrationName");
                    String password = request.queryParams("registrationPassword");
                    if(userAccounts.containsKey(name) || name.equals("") || password.equals("")){
                        response.redirect("/");
                    } else {
                        userAccounts.put(name, new User(name, password));
                        response.redirect("/");
                    }
                    return "";
                })
        );
        Spark.post(
                "/add",
                ((request, response) -> {
                    String post = request.queryParams("message");
                    if (post.length() > 140 || post.equals("")){
                        response.redirect("/home.html");
                    } else {
                        userAccounts.get(loginName).addPost(post);
                        response.redirect("/home.html");
                    }
                    return "";
                })
        );
    }
}
