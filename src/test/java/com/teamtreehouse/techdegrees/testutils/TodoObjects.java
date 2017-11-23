package com.teamtreehouse.techdegrees.testutils;

import com.teamtreehouse.techdegrees.model.Todo;

import java.util.Arrays;
import java.util.List;

public class TodoObjects
{
    public static Todo todo1()
    {
        return new Todo("Go Shopping", false);
    }

    public static Todo todo2()
    {
        return new Todo("Clean house", true);
    }

    public static Todo todoWithId1(){
        Todo todo = todo1();
        todo.setId(1L);
        return todo;
    }

    public static Todo todoWithId2()
    {
        Todo todo = todo2();
        todo.setId(2L);
        return todo;
    }

    public static List<Todo> todosWithIds()
    {
        return Arrays.asList(
                todoWithId1(),
                todoWithId2()
        );
    }
}
