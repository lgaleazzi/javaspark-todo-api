package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;


public class Sql2oTodoDaoUnitTest
{
    private Sql2oTodoDao dao;
    private Connection connection;

    @Before
    public void setUp() throws Exception
    {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oTodoDao(sql2o);
        //Keep connection open through entire test
        connection = sql2o.open();
    }

    @After
    public void tearDown()
    {
        connection.close();
    }

    @Test
    public void addingTodoId() throws Exception
    {
        Todo todo = new Todo("Go Shopping", false);
        Long originalId = todo.getId();

        dao.add(todo);

        assertNotEquals(originalId, todo.getId());
    }

    @Test
    public void findAllReturnsAllTodos() throws Exception
    {
        Todo todo1 = new Todo("Go Shopping", false);
        Todo todo2 = new Todo("Clean house", true);

        dao.add(todo1);
        dao.add(todo2);

        assertEquals(2, dao.findAll().size());
    }

}