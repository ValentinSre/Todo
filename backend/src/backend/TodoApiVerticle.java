package backend;

import bean.Todo;

import java.util.List;

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
import service.TodoService;

public class TodoApiVerticle extends AbstractVerticle {
	
	private final TodoService todoService = new TodoService();
	
	private void getAllTodos(RoutingContext routingContext) {
		  LOGGER.info("Fetching all todos...");

		  final List<bean.Todo> todos = todoService.findAll();
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
  
	private void updateOneTodo(RoutingContext routingContext) {
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

	    final Todo updatedTodo = new Todo(id, title, state, description);
	    final Todo savedTodo = todoService.update(updatedTodo);

	    final List<bean.Todo> todos = todoService.findAll();
	    final JsonObject jsonResponse = new JsonObject();
	    jsonResponse.put("todos", todos);
	    routingContext.response()
	        .setStatusCode(200)
	        .putHeader("Access-Control-Allow-Origin", "*")
	        .putHeader("content-type", "application/json")
	        .end(Json.encode(jsonResponse));
	}


	private static final Logger LOGGER = LoggerFactory.getLogger(TodoApiVerticle.class);

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
		router.get("/api/todos")
    		.handler(this::getAllTodos);
		router.route(HttpMethod.PUT, "/api/todos/:id")
	    	.handler(this::updateOneTodo);

		vertx.createHttpServer()
	    	.requestHandler(router)
	    	.listen(8081);
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info("Fin...");
	}
}
