package com.teamtreehouse.techdegrees.service;


import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.exception.ApiError;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import java.util.List;

public class TodoServiceImpl implements TodoService
{
    private TodoDao todoDao;

    public TodoServiceImpl(Sql2o sql2o)
    {
        this.todoDao = new Sql2oTodoDao(sql2o);
    }

    @Override
    public void add(Todo todo)
    {
        todoDao.add(todo);
    }

    @Override
    public List<Todo> findAll()
    {
        return todoDao.findAll();
    }

    @Override
    public Todo findById(String id)
    {
        Long idLong = convertToLong(id);
        Todo todo = todoDao.findById(idLong);
        if (todo == null)
        {
            throw new ApiError(404, "Sorry, we couldn't find a todo with id " + idLong);
        }
        return todo;
    }

    @Override
    public void update(Todo todo)
    {
        todoDao.update(todo);
    }

    @Override
    public void delete(String id)
    {
        Long idLong = convertToLong(id);
        if (todoDao.findById(idLong) == null)
        {
            throw new ApiError(404, "Sorry, we couldn't find a todo with id " + idLong);
        }
        todoDao.delete(idLong);
    }

    private Long convertToLong(String stringId)
    {
        try
        {
            Long id = Long.parseLong(stringId);
            return id;
        } catch (NumberFormatException e)
        {
            throw new ApiError(400, stringId + " is not a valid id");
        }

    }
}
