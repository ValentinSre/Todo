import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './todoList.css';

import config from '../../config';

function TodoList() {
  const [todos, setTodos] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8081/api/todos')
      .then(response => setTodos(response.data.todos))
      .catch(error => console.error(error));
  }, []);

  const handleCheckboxChange = (todoId, todoState) => {
    axios.put(`http://localhost:8081/api/todos/${todoId}`, {
      state: todoState ? config.state.unchecked : config.state.checked
    })
    .then(response => setTodos(response.data.todos))
    .catch(error => console.error(error));
  };

  const renderTodo = (todo) => {
    return (
      <li key={todo.id} className={`todo-item ${todo.state ? 'completed' : ''}`}>
        <div className="todo-details">
          <input type="checkbox" checked={todo.state} onChange={() => handleCheckboxChange(todo.id, todo.state)} />
          <Link to={`/todo/${todo.id}`} className="todo-title">{todo.title}</Link>
        </div>
        <button>Supprimer</button>
      </li>
    );
  };
 
  const sortedTodos = todos.sort((a, b) => a.state - b.state);

  return (
    <div className="todo-list-container">
      <h1>Ma TODO List</h1>
      <ul className="todo-list">
        {sortedTodos.map(todo => renderTodo(todo))}
      </ul>
    </div>
  );
}

export default TodoList;
