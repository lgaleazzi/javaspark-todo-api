package com.teamtreehouse.techdegrees.service;


import com.teamtreehouse.techdegrees.exception.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoService
{
    void add(Todo todo);

    List<Todo> findAll();

    Todo findById(String id);

    void update(Todo todo);

    void delete(String id);
}
