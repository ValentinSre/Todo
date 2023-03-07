package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import bean.Todo;

public class TodoService {
	
  private final Map<String, Todo> todos = new HashMap<String, Todo>();
  
  public TodoService() {
    super();
    initTodos();
  }
  
  private void initTodos() {
    final Todo todo1 = new Todo("1", "Sortir les poubelles", 0, "Trier les dechets selon la poubelle", 4);
    final Todo todo2 = new Todo("2", "Relire mon cours", 0, "", 2);
    final Todo todo3 = new Todo("3", "Faire la vaisselle", 0, "", 3);
    final Todo todo4 = new Todo("4", "Faire les courses", 0, "Penser a acheter du beurre", 1);
    todos.put(todo1.getId(), todo1);
    todos.put(todo2.getId(), todo2);
    todos.put(todo3.getId(), todo3);
    todos.put(todo4.getId(), todo4);
  }
  
  public Map<String, Todo> getTodosMap() {
	return todos;
  }
  
  public List<Todo> findAll() {
    return todos.values().stream()
        .sorted((t1, t2) -> Integer.compare(t1.getPosition(), t2.getPosition()))
        .collect(Collectors.toList());
  }
  
  public Todo findById(final String id) {
    return todos.get(id);
  }
  
  public Todo add(final Todo todo) {
	final String id = "TD" + System.currentTimeMillis() + "_ID";
	final Todo newTodo = new Todo(id,
	   todo.getTitle(),
	   todo.getState(),
	   todo.getDescription(),
	   todo.getPosition());
	todos.put(id, newTodo);
	
	return newTodo;
  }
  
  public Todo update(final Todo todo) {
    todos.put(todo.getId(), todo);
    return todo;
  }
  
  public void remove(final String id) {
    todos.remove(id);
  }
}