package com.teamtreehouse.techdegrees.dao;


import com.teamtreehouse.techdegrees.exception.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oTodoDao implements TodoDao
{
    private final Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o)
    {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Todo todo) throws DaoException
    {
        String sql = "INSERT INTO todo(name, is_completed) VALUES (:name, :is_completed)";
        try (Connection connection = sql2o.open())
        {
            Long id = (Long) connection.createQuery(sql)
                    .addParameter("name", todo.getName())
                    .addParameter("is_completed", todo.isCompleted())
                    .executeUpdate()
                    .getKey();
            todo.setId(id);
        } catch (Sql2oException e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }

    @Override
    public List<Todo> findAll()
    {
        try (Connection connection = sql2o.open())
        {
            return connection.createQuery("SELECT * FROM todo")
                    .addColumnMapping("is_completed", "isCompleted")
                    .executeAndFetch(Todo.class);
        }
    }
}