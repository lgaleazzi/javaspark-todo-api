package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.model.Todo;
import com.teamtreehouse.techdegrees.testutils.ApiClient;
import com.teamtreehouse.techdegrees.testutils.ApiResponse;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

import static com.teamtreehouse.techdegrees.testutils.TodoObjects.todo1;
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
    public void findTodoById() throws Exception
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

}