package test.java.bean;

import main.java.bean.Todo;

import static org.junit.Assert.*;
import org.junit.Test;

public class TodoTest {

  // Test constructor and getters
  @Test
  public void testConstructorAndGetters() {
    Todo todo = new Todo("1", "Aller à l'épicerie", 0, "Lait, pain, oeufs", 1);

    assertEquals("1", todo.getId());
    assertEquals("Aller à l'épicerie", todo.getTitle());
    assertEquals(0, todo.getState());
    assertEquals("Lait, pain, oeufs", todo.getDescription());
    assertEquals(1, todo.getPosition());
  }

  // Test setters and getters
  @Test
  public void testSettersAndGetters() {
    Todo todo = new Todo("1", "Aller à l'épicerie", 0, "Lait, pain, oeufs", 1);

    todo.setId("2");
    assertEquals("2", todo.getId());

    todo.setTitle("Acheter de nouveaux vêtements");
    assertEquals("Acheter de nouveaux vêtements", todo.getTitle());

    todo.setState(1);
    assertEquals(1, todo.getState());

    todo.setDescription("T-shirt, jean, chaussures");
    assertEquals("T-shirt, jean, chaussures", todo.getDescription());

    todo.setPosition(2);
    assertEquals(2, todo.getPosition());
  }

  // Test toString method
  @Test
  public void testToString() {
    Todo todo = new Todo("1", "Aller à l'épicerie", 0, "Lait, pain, oeufs", 1);

    String expected = "Todo [id=1, title=Aller à l'épicerie, state=0, description=Lait, pain, oeufs]";
    assertEquals(expected, todo.toString());
  }
}