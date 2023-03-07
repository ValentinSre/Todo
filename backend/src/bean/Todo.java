package bean;

public class Todo {
	  private String id;
	  private String title;
	  private int state;
	  private String description;
	  private int position;
	  
	  public Todo(String id, String title, int state, String description, int position) {
	    super();
	    this.id = id;
	    this.title = title;
	    this.state = state;
	    this.description = description;
	    this.position = position;
	  }
	  
	  public String getId() {
	    return id;
	  }
	  
	  public String getTitle() {
	    return title;
	  }
	  
	  public int getState() {
	    return state;
	  }
	  
	  public String getDescription() {
	    return description;
	  }
	  
	  public int getPosition() {
		  return position;
	  }
	  
	  public void setPosition(int position) {
		  this.position = position;
	  }

	  @Override
	  public String toString() {
	    return "Todo [id=" + id + ", title=" + title + ", state=" + state + ", description=" + description + "]";
	  }
	}