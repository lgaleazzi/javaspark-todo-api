package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.model.Todo;
import com.teamtreehouse.techdegrees.testutils.ApiClient;
import com.teamtreehouse.techdegrees.testutils.ApiResponse;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.teamtreehouse.techdegrees.testutils.TodoObjects.*;
import static org.junit.Assert.*;

public class AppTest
{
    private static final String PORT = "4568";
    private static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";
    private static final String PATH = "/api/v1";
    private Connection connection;
    private ApiClient client;
    private Gson gson;
    private TodoDao todoDao;

    @BeforeClass
    public static void startServer()
    {
        String[] args = {PORT, TEST_DATASOURCE};
        App.main(args);
    }

    @AfterClass
    public static void stopServer()
    {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception
    {
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        todoDao = new Sql2oTodoDao(sql2o);
        connection = sql2o.open();
        client = new ApiClient("http://localhost:" + PORT);
        gson = new Gson();
    }

    @After
    public void tearDown()
    {
        connection.close();
    }

    @Test
    public void addingTodoReturnsStatusCreated() throws Exception
    {
        Map<String, String> values = new HashMap<>();
        values.put("name", "Go Shopping");
        values.put("completed", "false");

        ApiResponse response = client.request("POST", PATH + "/todos", gson.toJson(values));

        assertEquals(201, response.getStatus());
    }

    @Test
    public void findAllReturnsAllTodos() throws Exception
    {
        Todo todo1 = todo1();
        Todo todo2 = todo2();
        List<Todo> allTodos = Arrays.asList(
                todo1,
                todo2
        );
        allTodos.forEach(todoDao::add);

        ApiResponse response = client.request("GET", PATH + "/todos");
        Type todoListType = new TypeToken<List<Todo>>() {}.getType();
        List<Todo> retrieved = gson.fromJson(response.getBody(), todoListType);

        assertTrue(retrieved.contains(todo1));
        assertTrue(retrieved.contains(todo2));
    }

    @Test
    public void findTodoByIdReturnsTodo() throws Exception
    {
        Todo todo = todo1();
        todoDao.add(todo);

        ApiResponse response = client.request("GET", PATH + "/todos/" + todo.getId());
        Todo retrieved = gson.fromJson(response.getBody(), Todo.class);

        assertEquals(todo, retrieved);
    }

    @Test
    public void missingTodoReturnsNotFound() throws Exception
    {
        ApiResponse response = client.request("GET", PATH + "/todos/42");

        assertEquals(404, response.getStatus());
    }

    @Test
    public void updateReturnsStatusSuccess() throws Exception
    {
        Todo todo = todo1();
        todoDao.add(todo);

        Todo expected = new Todo("Go Shopping", true);
        expected.setId(todo.getId());

        Map<String, String> values = new HashMap<>();
        values.put("id", "1");
        values.put("name", "Go Shopping");
        values.put("completed", "true");


        ApiResponse response = client.request("PUT", PATH + "/todos/" + todo.getId(), gson.toJson(values));
        Todo retrieved = gson.fromJson(response.getBody(), Todo.class);

        assertEquals(200, response.getStatus());
        assertEquals(expected, retrieved);
    }

    @Test
    public void updateMissingReturnsNotFound() throws Exception
    {
        Map<String, String> values = new HashMap<>();
        values.put("id", "42");
        values.put("name", "Go Shopping");
        values.put("completed", "false");

        ApiResponse response = client.request("PUT", PATH + "/todos/42", gson.toJson(values));

        assertEquals(404, response.getStatus());
    }

    @Test
    public void deleteReturnsStatusSuccess() throws Exception
    {
        Todo todo = todo1();
        todoDao.add(todo);

        ApiResponse response = client.request("DELETE", PATH + "/todos/" + todo.getId());

        assertEquals(200, response.getStatus());
        assertEquals(null, todoDao.findById(todo.getId()));
    }

    @Test
    public void deleteMissingReturnsNotFound() throws Exception
    {
        ApiResponse response = client.request("DELETE", PATH + "/todos/42");

        assertEquals(404, response.getStatus());
    }

}