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
import io.vertx.ext.web.handler.CorsHandler;
import service.TodoService;

public class MyApiVerticle extends AbstractVerticle {
	
	private final TodoService todoService = new TodoService();
	
	private void getAllTodos(RoutingContext routingContext) {
		 LOGGER.info("Fetching all todos...");
		 
		 final List<bean.Todo> todos = todoService.findAll();
		
		 final JsonObject jsonResponse = new JsonObject();
		 jsonResponse.put("todos", todos);

		 routingContext.response()
			 .setStatusCode(200)
			 .putHeader("Access-Control-Allow-Origin", "*")
			 .putHeader("content-type", "application/json")
			 .end(Json.encodePrettily(jsonResponse));
		}
	private static final Logger LOGGER = LoggerFactory.getLogger(MyApiVerticle.class);

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
		
		// Routes
		router.get("/api/todos")
		.handler(this::getAllTodos);
		vertx.createHttpServer()
	 	.requestHandler(router)
	 	.listen(8081);
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info("Fin...");
	}
}
