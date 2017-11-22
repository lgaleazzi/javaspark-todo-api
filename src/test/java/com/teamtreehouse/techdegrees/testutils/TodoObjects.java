package com.teamtreehouse.techdegrees.testutils;

import com.teamtreehouse.techdegrees.model.Todo;

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
}
