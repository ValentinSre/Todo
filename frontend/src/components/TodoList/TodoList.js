import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './todoList.css';

import config from '../../config';

function TodoList() {
  const navigate = useNavigate();
  const [todos, setTodos] = useState([]);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  
  const apiUrl = 'http://localhost:8081';

  useEffect(() => {
    axios.get(`${apiUrl}/api/todos`)
      .then(response => setTodos(response.data.todos))
      .catch(error => console.error(error));
  }, [apiUrl]);

  const handleCheckboxChange = (todoId, todoState) => {
    axios.put(`${apiUrl}/api/todos/${todoId}`, {
      state: todoState ? config.state.unchecked : config.state.checked
    })
    .then(response => setTodos(response.data.todos))
    .catch(error => console.error(error));
  };

  const renderTodo = (todo) => {
    const handleDeleteClick = () => {
      axios.delete(`${apiUrl}/${todo.id}`)
        .then(response => setTodos(response.data.todos))
        .catch(error => console.error(error));
    };

    return (
      <li key={todo.id} className={`todo-item ${todo.state ? 'completed' : ''}`}>
        <div className="todo-details">
          <input type="checkbox" checked={todo.state} onChange={() => handleCheckboxChange(todo.id, todo.state)} />
          <Link to={`/todo/${todo.id}`} className="todo-title">{todo.title}</Link>
        </div>
        <button id={todo.id} onClick={handleDeleteClick}>Supprimer</button>
      </li>
    );
  };
 
  const sortedTodos = todos.sort((a, b) => a.state - b.state);

  const handleTitleChange = (event) => {
    setTitle(event.target.value);
  };

  const handleDescriptionChange = (event) => {
    setDescription(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!title) {
      alert('Le titre est obligatoire');
      return;
    }

    axios.post(`${apiUrl}/api/todo`, {
      title,
      description
    })
    .then(response => {
      const newTodoId = response.data.id;
      navigate(`/todo/${newTodoId}`);
    })
    .catch(error => console.error(error));
  };

  return (
    <div className="todo-list-container">
      <h1>Ma TODO List</h1>

      <ul className="todo-list">
        {sortedTodos.map(todo => renderTodo(todo))}
      </ul>
      <div className="form-container">
        <form onSubmit={handleSubmit}>
          <input type="text" placeholder="Titre" value={title} onChange={handleTitleChange} label="Titre" />
          <textarea placeholder="Description" value={description} onChange={handleDescriptionChange} label="Description" />
          <button type="submit">Ajouter</button>
        </form>
      </div>
    </div>
  );
}

export default TodoList;