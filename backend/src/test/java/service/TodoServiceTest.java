package test.java.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import main.java.bean.Todo;
import main.java.service.TodoService;

public class TodoServiceTest {
	
  private TodoService todoService;
  
  @Before
  public void setUp() {
    todoService = new TodoService();
  }
  
  @Test
  public void testGetTodosMap() {
	Map<String, Todo> todos = todoService.getTodosMap();
	assertNotNull(todos);
	assertTrue(todos.containsKey("1"));
	assertTrue(todos.containsKey("2"));
	assertTrue(todos.containsKey("3"));
	assertTrue(todos.containsKey("4"));
  }
  
  @Test
  public void testFindAll() {
    List<Todo> todos = todoService.findAll();
    assertNotNull(todos);
    assertEquals(4, todos.size());
    assertEquals("2", todos.get(0).getId());
    assertEquals("4", todos.get(1).getId());
    assertEquals("3", todos.get(2).getId());
    assertEquals("1", todos.get(3).getId());
  }
  
  @Test
  public void testFindById() {
    Todo todo = todoService.findById("1");
    assertNotNull(todo);
    assertEquals("1", todo.getId());
    assertEquals("Sortir les poubelles", todo.getTitle());
  }
  
  @Test
  public void testAdd() {
    Todo todo = new Todo("", "Faire le ménage", 0, "", 5);
    Todo newTodo = todoService.add(todo);
    assertNotNull(newTodo);
    assertNotNull(newTodo.getId());
    assertEquals("Faire le ménage", newTodo.getTitle());
    assertEquals(0, newTodo.getState());
    assertEquals("", newTodo.getDescription());
    assertEquals(5, newTodo.getPosition());
    assertNotNull(todoService.findById(newTodo.getId()));
  }
  
  @Test
  public void testUpdate() {
    Todo todo = todoService.findById("1");
    todo.setTitle("Sortir les poubelles recyclables");
    todo.setState(1);
    todo.setDescription("Trier les déchets selon la poubelle (recyclable ou non-recyclable)");
    todo.setPosition(2);
    Todo updatedTodo = todoService.update(todo);
    assertNotNull(updatedTodo);
    assertEquals("1", updatedTodo.getId());
    assertEquals("Sortir les poubelles recyclables", updatedTodo.getTitle());
    assertEquals(1, updatedTodo.getState());
    assertEquals("Trier les déchets selon la poubelle (recyclable ou non-recyclable)", updatedTodo.getDescription());
    assertEquals(2, updatedTodo.getPosition());
  }
  
  @Test
  public void testRemove() {
    Todo todo = todoService.findById("1");
    assertNotNull(todo);
    todoService.remove("1");
    assertNull(todoService.findById("1"));
  }
}

