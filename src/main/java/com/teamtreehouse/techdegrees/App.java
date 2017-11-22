package com.teamtreehouse.techdegrees;


import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import org.sql2o.Sql2o;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");

        Sql2o sql2o = new Sql2o("jdbc:h2:~/todos.db;INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        TodoDao todoDao = new Sql2oTodoDao(sql2o);

        get("/blah", (req, res) -> "Hello!");
        post("/todos", "application/json", (request, response) -> {
            request.body();
            return null;
        });

    }

}
