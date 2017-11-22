package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class App {
    private final static String TYPE_JSON = "application/json";

    public static void main(String[] args) {
        staticFileLocation("/public");

        Sql2o sql2o = new Sql2o("jdbc:h2:~/todos.db;INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        TodoDao todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();

        get("/blah", (req, res) -> "Hello!");

        path("/api/v1", () -> {
            post("/todos", TYPE_JSON, (request, response) -> {
                Todo todo = gson.fromJson(request.body(), Todo.class);
                todoDao.add(todo);
                response.status(201);
                return todo;
            }, gson::toJson);

            get("/todos", TYPE_JSON,
                    (request, response) -> todoDao.findAll(), gson::toJson);

            get("/todos/:id", TYPE_JSON, ((request, response) -> {
                Long id = Long.parseLong(request.params("id"));
                Todo todo = todoDao.findById(id);
                return todo;
            }), gson::toJson);

            put("todos/:id", TYPE_JSON, ((request, response) -> {
                Todo todo = gson.fromJson(request.body(), Todo.class);
                todoDao.update(todo);
                return todo;
            }));

            after(((request, response) -> response.type(TYPE_JSON)));
        });


    }

}
