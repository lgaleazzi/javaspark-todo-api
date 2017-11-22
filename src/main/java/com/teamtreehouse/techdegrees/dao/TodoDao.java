package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exception.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoDao
{
    void add(Todo todo) throws DaoException;

    List<Todo> findAll();
}
