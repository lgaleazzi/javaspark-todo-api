package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.exception.ApiError;
import com.teamtreehouse.techdegrees.model.Todo;
import com.teamtreehouse.techdegrees.service.TodoService;
import com.teamtreehouse.techdegrees.service.TodoServiceImpl;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    private final static String TYPE_JSON = "application/json";
    private static TodoDao todoDao;

    public static void main(String[] args) {
        staticFileLocation("/public");
        String datasource = "jdbc:h2:~/todos.db";

        if (args.length == 2)
        {
            port(Integer.parseInt(args[0]));
            datasource = args[1];
        } else if (args.length > 0)
        {
            System.out.println("java App <port> <datasource>");
        }

        Sql2o sql2o = new Sql2o(datasource + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        TodoService todoService = new TodoServiceImpl(sql2o);
        todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();



        path("/api/v1", () -> {
            get("/todos", TYPE_JSON,
                    (request, response) -> todoService.findAll(), gson::toJson);

            post("/todos", TYPE_JSON, (request, response) -> {
                Todo todo = gson.fromJson(request.body(), Todo.class);
                todoService.add(todo);
                response.status(201);
                return todo;
            }, gson::toJson);

            get("/todos/:id", TYPE_JSON,
                    ((request, response) -> todoService.findById(request.params("id"))),
                    gson::toJson);

            put("/todos/:id", TYPE_JSON, ((request, response) -> {
                Todo todo = todoService.findById(request.params("id"));

                Todo updatedTodo = gson.fromJson(request.body(), Todo.class);
                todo.setName(updatedTodo.getName());
                todo.setCompleted(updatedTodo.isCompleted());

                todoService.update(todo);

                response.status(200);
                return todo;
            }), gson::toJson);

            delete("/todos/:id", TYPE_JSON, ((request, response) -> {
                todoService.delete(request.params("id"));
                response.status(200);
                return "";
            }), gson::toJson);

            exception(ApiError.class, (e, request, response) -> {
                ApiError error = (ApiError) e;
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("status", error.getStatus());
                jsonMap.put("errorMessage", error.getMessage());
                response.type(TYPE_JSON);
                response.status(error.getStatus());
                response.body(gson.toJson(jsonMap));
            });

            after(((request, response) -> response.type(TYPE_JSON)));
        });

    }
}
