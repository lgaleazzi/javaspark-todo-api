package com.teamtreehouse.techdegrees.model;


import com.google.gson.annotations.SerializedName;

public class Todo
{
    private Long id;
    private String name;
    @SerializedName(value = "completed")
    private boolean isCompleted;

    public Todo(String name, boolean isCompleted)
    {
        this.name = name;
        this.isCompleted = isCompleted;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isCompleted()
    {
        return isCompleted;
    }

    public void setCompleted(boolean completed)
    {
        isCompleted = completed;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Todo todo = (Todo)o;

        if (id != null ? !id.equals(todo.id) : todo.id != null)
            return false;
        return name != null ? name.equals(todo.name) : todo.name == null;

    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
