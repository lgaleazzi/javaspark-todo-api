package com.teamtreehouse.techdegrees.service;

import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.exception.ApiError;
import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static com.teamtreehouse.techdegrees.testutils.TodoObjects.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TodoServiceUnitTest
{

    private TodoService todoService;
    private TodoDao todoDao;

    @Before
    public void setUp() throws Exception
    {
        todoDao = mock(TodoDao.class);
        todoService = new TodoServiceImpl(todoDao);
    }

    @Test
    public void findByIdReturnsTodo() throws Exception
    {
        Todo todo = todoWithId1();
        when(todoDao.findById(1L)).thenReturn(todo);

        Todo found = todoService.findById("1");

        assertEquals(todo, found);
    }

    @Test(expected = ApiError.class)
    public void findByIdThrowsExceptionWhenMissing() throws Exception
    {
        when(todoDao.findById(1L)).thenReturn(null);

        todoService.findById("1");
    }

    @Test(expected = ApiError.class)
    public void findByIdThrowsExceptionWhenInvalid() throws Exception
    {
        todoService.findById("a");
    }

    @Test
    public void findAllReturnsList() throws Exception
    {
        List<Todo> todos = todosWithIds();

        when(todoDao.findAll()).thenReturn(todos);

        List<Todo> found = todoService.findAll();

        assertEquals(todos, found);
    }

    @Test public void updateCallsDao() throws Exception
    {
        todoService.update(todoWithId1());

        verify(todoDao).update(todoWithId1());
    }

    @Test(expected = ApiError.class)
    public void deleteThrowsExceptionWhenInvalidId() throws Exception
    {
        todoService.delete("a");
    }

    @Test(expected = ApiError.class)
    public void deleteMissingThrowsException() throws Exception
    {
        when(todoDao.findById(1L)).thenReturn(null);

        todoService.delete("1");
    }

    @Test
    public void deleteCallsDao() throws Exception
    {
        when(todoDao.findById(1L)).thenReturn(todoWithId1());

        todoService.delete("1");

        verify(todoDao).delete(1L);
    }

}