package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static com.teamtreehouse.techdegrees.testutils.TodoObjects.todo1;
import static com.teamtreehouse.techdegrees.testutils.TodoObjects.todo2;
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
    public void addingTodoReturnsCorrectId() throws Exception
    {
        Todo todo = todo1();
        Long originalId = todo.getId();

        dao.add(todo);

        assertNotEquals(originalId, todo.getId());
    }

    @Test
    public void findAllReturnsAllTodos() throws Exception
    {
        Todo todo1 = todo1();
        Todo todo2 = todo2();

        dao.add(todo1);
        dao.add(todo2);

        assertEquals(2, dao.findAll().size());
    }

    @Test
    public void noTodosReturnsEmptyList() throws Exception
    {
        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void findByIdReturnsCorrectTodo() throws Exception
    {
        Todo todo = todo1();
        dao.add(todo);

        Todo foundById = dao.findById(todo.getId());

        assertEquals(todo, foundById);
    }

    @Test
    public void updatePersistUpdatedTodo() throws Exception
    {
        Todo todo = todo1();
        dao.add(todo);

        todo.setName("Updated name");
        todo.setCompleted(true);
        dao.update(todo);

        Todo persistedTodo = dao.findById(todo.getId());
        assertEquals("Updated name", persistedTodo.getName());
        assertEquals(true, persistedTodo.isCompleted());
    }

    @Test
    public void deleteRemovesTodo() throws Exception
    {
        Todo todo = todo1();
        dao.add(todo);

        dao.delete(todo.getId());

        assertEquals(0, dao.findAll().size());
    }
}