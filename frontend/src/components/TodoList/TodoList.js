import React, { useState, useEffect } from 'react';
import axios from 'axios';

function TodoList() {
  const [todos, setTodos] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8081/api/todos')
      .then(response => setTodos(response.data.todos))
      .catch(error => console.error(error));
  }, []);

  return (
    <ul>
      {todos.map(todo => (
        <li key={todo.id}>
          {todo.title} - {todo.state ? 'DONE' : 'TO DO'}
        </li>
      ))}
    </ul>
  );
}

export default TodoList;
