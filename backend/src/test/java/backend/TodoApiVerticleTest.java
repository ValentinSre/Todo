import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import main.java.bean.Todo;
import main.java.service.TodoService;

@RunWith(MockitoJUnitRunner.class)
public class TodoApiVerticleTest {

    @InjectMocks
    private TodoApiVerticle todoApiVerticle;

    @Mock
    private TodoService todoService;

    @Mock
    private RoutingContext routingContext;

    @Before
    public void setUp() {
        Map<String, Todo> todosMap = new HashMap<>();
        Todo todo1 = new Todo("1", "Todo 1", 0, "Description 1", 1);
        Todo todo2 = new Todo("2", "Todo 2", 0, "Description 2", 2);
        todosMap.put(todo1.getId(), todo1);
        todosMap.put(todo2.getId(), todo2);
        when(todoService.getTodosMap()).thenReturn(todosMap);

        List<Todo> todosList = new ArrayList<>();
        todosList.add(todo1);
        todosList.add(todo2);
        when(todoService.findAll()).thenReturn(todosList);

        when(todoService.findById("1")).thenReturn(todo1);
        when(todoService.findById("2")).thenReturn(todo2);

        Todo todoToUpdate = new Todo("1", "Updated Todo 1", 1, "Updated Description 1", 1);
        when(todoService.update(todoToUpdate)).thenReturn(todoToUpdate);

        todoService.remove("2");
    }

    @Test
    public void testCreateTodo() {
        JsonObject requestBody = new JsonObject().put("title", "New Todo").put("description", "New Todo description");
        when(routingContext.getBodyAsJson()).thenReturn(requestBody);
        Todo createdTodo = new Todo("3", "New Todo", 0, "New Todo description", 3);
        when(todoService.add(createdTodo)).thenReturn(createdTodo);
        JsonObject expectedResponse = new JsonObject(Json.encode(createdTodo)).putNull("items");
        when(routingContext.response()).thenReturn(mock(io.vertx.core.http.HttpServerResponse.class));
        todoApiVerticle.createTodo(routingContext);
        verify(routingContext.response(), times(1)).setStatusCode(201);
        verify(routingContext.response(), times(1)).putHeader(eq("content-type"), eq("application/json"));
        verify(routingContext.response(), times(1)).end(eq(expectedResponse.encode()));
    }

    @Test
    public void testGetTodo() {
        when(routingContext.request()).thenReturn(mock(io.vertx.core.http.HttpServerRequest.class));
        when(routingContext.request().getParam("id")).thenReturn("1");
        Todo todo = new Todo("1", "Todo 1", 0, "Description 1", 1);
        JsonObject expectedResponse = new JsonObject(Json.encode(todo)).putNull("items");
        when(routingContext.response()).thenReturn(mock(io.vertx.core.http.HttpServerResponse.class));
        todoApiVerticle.getTodoById(routingContext);
        verify(routingContext.response(), times(1)).setStatusCode(200);
        verify(routingContext.response(), times(1)).putHeader(eq("content-type"), eq("application/json"));
        verify(routingContext.response(), times(1)).end(eq(expectedResponse.encode()));
    }

    @Test
    public void testGetTodos() {
        when(routingContext.response()).thenReturn(mock(io.vertx.core.http.HttpServerResponse.class));
        todoApiVerticle.getTodos(routingContext);
        verify(routingContext.response(), times(1)).setStatusCode(200);
        verify(routingContext.response(), times(1)).putHeader(eq("content-type"), eq("application/json"));
        verify(routingContext.response(), times(1)).end(eq(new JsonArray(Json.encodePrettily(todoService.findAll())).encode()));
    }

    @Test
    public void testUpdateTodo() {
        JsonObject requestBody = new JsonObject().put("id", "1").put("title", "Updated Todo 1").put("description", "Updated Description 1");
        when(routingContext.getBodyAsJson()).thenReturn(requestBody);
        when(routingContext.request()).thenReturn(mock(io.vertx.core.http.HttpServerRequest.class));
        when(routingContext.request().getParam("id")).thenReturn("1");
        Todo todo = new Todo("1", "Updated Todo 1", 1, "Updated Description 1", 1);
        JsonObject expectedResponse = new JsonObject(Json.encode(todo)).putNull("items");
        when(routingContext.response()).thenReturn(mock(io.vertx.core.http.HttpServerResponse.class));
        todoApiVerticle.updateTodoById(routingContext);
        verify(routingContext.response(), times(1)).setStatusCode(200);
        verify(routingContext.response(), times(1)).putHeader(eq("content-type"), eq("application/json"));
        verify(routingContext.response(), times(1)).end(eq(expectedResponse.encode()));
    }

    @Test
    public void testDeleteTodo() {
        when(routingContext.request()).thenReturn(mock(io.vertx.core.http.HttpServerRequest.class));
        when(routingContext.request().getParam("id")).thenReturn("2");
        when(routingContext.response()).thenReturn(mock(io.vertx.core.http.HttpServerResponse.class));
        todoApiVerticle.deleteTodoById(routingContext);
        verify(routingContext.response(), times(1)).setStatusCode(204);
        verify(routingContext.response(), times(1)).end();
    }

    @Test
    public void testGetRouter() {
        Router router = todoApiVerticle.getRouter();
        assertNotNull(router);
    }

    @Test
    public void testGetCorsHandler() {
        CorsHandler corsHandler = todoApiVerticle.getCorsHandler();
        assertNotNull(corsHandler);
    }

    @Test
    public void testGetBodyHandler() {
        BodyHandler bodyHandler = todoApiVerticle.getBodyHandler();
        assertNotNull(bodyHandler);
    }

    @Test
    public void testGetTodoService() {
        TodoService todoService = todoApiVerticle.getTodoService();
        assertNotNull(todoService);
    }
}