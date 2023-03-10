package main.java.backend;

import java.util.List;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import main.java.bean.Todo;
import main.java.service.TodoService;

public class TodoApiVerticle extends AbstractVerticle {
	
	// Init
	private final TodoService todoService = new TodoService();
	private static final Logger LOGGER = LoggerFactory.getLogger(TodoApiVerticle.class);

	// CRUD methods
	private void createTodo(RoutingContext routingContext) {
		  LOGGER.info("Creating a todo...");
		  
		  final JsonObject body = routingContext.getBodyAsJson();
		  final String title = body.getString("title");
		  final String description = body.getString("description");
		  
		  final Map<String, Todo> todosMap = todoService.getTodosMap();
		  todosMap.values().stream().forEach(todo -> todo.setPosition(todo.getPosition() + 1));
		  
		  final Todo todo = new Todo(null, title, 0, description, 1);
		  final Todo createdTodo = todoService.add(todo);

		  routingContext.response()
		      .setStatusCode(201)
		      .putHeader("content-type", "application/json")
		      .end(Json.encode(createdTodo));
	}
	
	private void getTodo(RoutingContext routingContext) {
		  LOGGER.info("Getting a precise todo...");
		  final String id = routingContext.request().getParam("id");
		  
		  final Todo todo = todoService.findById(id);

		  routingContext.response()
		      .setStatusCode(200)
		      .putHeader("content-type", "application/json")
		      .end(Json.encode(todo));
	}

	private void getAllTodos(RoutingContext routingContext) {
		  LOGGER.info("Fetching all todos...");

		  final List<main.java.bean.Todo> todos = todoService.findAll();
		  LOGGER.info(todos);
		  
		  final JsonObject jsonResponse = new JsonObject();
		  jsonResponse.put("todos", todos);
		  LOGGER.info(jsonResponse);
		  routingContext.response()
		      .setStatusCode(200)
		      .putHeader("Access-Control-Allow-Origin", "*") // autorise toutes les origines
		      .putHeader("content-type", "application/json")
		      .end(Json.encode(jsonResponse));
		}
		  
	private void updateTodo(RoutingContext routingContext) {
	    LOGGER.info("Updating a todo...");

	    final String id = routingContext.request().getParam("id");

	    final Todo existingTodo = todoService.findById(id);
	    if (existingTodo == null) {
	        routingContext.response()
	            .setStatusCode(404)
	            .putHeader("content-type", "application/json")
	            .end();
	        return;
	    }

	    final JsonObject body = routingContext.getBodyAsJson();
	    if (body == null) {
	        routingContext.response()
	            .setStatusCode(400)
	            .putHeader("content-type", "application/json")
	            .end();
	        return;
	    }

	    final String title = body.getString("title", existingTodo.getTitle());
	    final int state = body.getInteger("state", existingTodo.getState());
	    final String description = body.getString("description", existingTodo.getDescription());
	    int position;
	    
	    final Map<String, Todo> todosMap = todoService.getTodosMap();

	    if (state != existingTodo.getState()) {
	        if (state == 1) {
	            position = todosMap.size();
	            todosMap.values().stream()
	                .filter(todo -> todo.getPosition() > existingTodo.getPosition())
	                .forEach(todo -> todo.setPosition(todo.getPosition() - 1));
	        } else {
	            int maxPositionWithState0 = todosMap.values().stream()
	                    .filter(todo -> todo.getState() == 0)
	                    .mapToInt(Todo::getPosition)
	                    .max()
	                    .orElse(0);

	            position = maxPositionWithState0 + 1;
	            todosMap.values().stream()
	                    .filter(todo -> todo.getState() == 1 && todo.getPosition() >= position && todo.getPosition() < existingTodo.getPosition())
	                    .forEach(todo -> todo.setPosition(todo.getPosition() + 1));
	        }
	    } else {
	    	position = existingTodo.getPosition();
	    }

	    final Todo updatedTodo = new Todo(id, title, state, description, position);
	    final Todo savedTodo = todoService.update(updatedTodo);

	    final List<main.java.bean.Todo> todos = todoService.findAll();
	    final JsonObject jsonResponse = new JsonObject();
	    jsonResponse.put("todos", todos);
	    routingContext.response()
	        .setStatusCode(200)
	        .putHeader("Access-Control-Allow-Origin", "*")
	        .putHeader("content-type", "application/json")
	        .end(Json.encode(jsonResponse));
	}

	private void deleteTodo(final RoutingContext routingContext) {
	    LOGGER.info("Deleting a todo...");
	    
	    final String id = routingContext.request().getParam("id");
	    
	    final Todo existingTodo = todoService.findById(id);
		final Map<String, Todo> todosMap = todoService.getTodosMap();
		todosMap.values().stream()
			.filter(todo -> todo.getPosition() > existingTodo.getPosition())
			.forEach(todo -> todo.setPosition(todo.getPosition() - 1));
	    
		todoService.remove(id);

	    final List<main.java.bean.Todo> todos = todoService.findAll();
	    final JsonObject jsonResponse = new JsonObject();
	    jsonResponse.put("todos", todos);
	    routingContext.response()
	        .setStatusCode(200)
	        .putHeader("content-type", "application/json")
	        .end(Json.encode(jsonResponse));
	}
	
	// Managing verticle
	@Override
	public void start() throws Exception {
		LOGGER.info("Lancement du verticle");
    
		Router router = Router.router(vertx);
    
		CorsHandler corsHandler = CorsHandler.create("*")
			    .allowedMethod(HttpMethod.GET)
			    .allowedMethod(HttpMethod.POST)
			    .allowedMethod(HttpMethod.PUT)
			    .allowedMethod(HttpMethod.DELETE)
			    .allowedHeader("Access-Control-Allow-Origin")
			    .allowedHeader("Access-Control-Allow-Headers")
			    .allowedHeader("Content-Type");
			router.route().handler(corsHandler);

		// Routes
		router.route().handler(BodyHandler.create());
		router.post("/api/todo").handler(this::createTodo);
		router.get("/api/todos/:id").handler(this::getTodo);
		router.get("/api/todos").handler(this::getAllTodos);
		router.route(HttpMethod.PUT, "/api/todos/:id").handler(this::updateTodo);
	    router.delete("/:id").handler(this::deleteTodo);

		vertx.createHttpServer()
	    	.requestHandler(router)
	    	.listen(8081);
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info("Fermeture du verticle");
	}
}
