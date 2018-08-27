package com.teamtreehouse.courses;



import com.teamtreehouse.courses.model.CourseIdea;
import com.teamtreehouse.courses.model.CourseIdeaDAO;
import com.teamtreehouse.courses.model.SimpleCourseIdeaDAO;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;


import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        staticFileLocation("/public");

        CourseIdeaDAO dao = new SimpleCourseIdeaDAO();

        get("/",(req, res) -> {
            Map<String,String> model = new HashMap<>();
            model.put("username", req.cookie("username"));

            return new ModelAndView(model, "index.hbs");
            }, new HandlebarsTemplateEngine());

        post("/sign-in",(req, res) -> {
            Map<String, String> model = new HashMap<>();

            String username = req.queryParams("username");
            model.put("username", username);
            res.cookie("username", username);
            res.redirect("/");
            return null;
        });

        get("/ideas", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ideas",dao.findAll());
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (req, res) -> {
            dao.add(new CourseIdea(req.queryParams("title"),req.cookie("username")));
            res.redirect("/ideas");
            return null;

        });
    }
}
