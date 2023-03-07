import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useParams } from 'react-router-dom';
import { FaHome } from 'react-icons/fa';

import './todoDetails.css';

function TodoDetails() {
  const [todo, setTodo] = useState(null);
  const { id } = useParams();

  useEffect(() => {
    axios.get(`http://localhost:8081/api/todos/${id}`)
      .then(response => setTodo(response.data))
      .catch(error => console.error(error));
  }, [id]);

  if (!todo) {
    return <div>Loading...</div>;
  }

  return (
    <div className="todo-details-wrapper">
      <div className="todo-details-header">
        <Link to="/" className="home-link">
          <FaHome className="home-icon" />
          Accueil
        </Link>
        <span className="breadcrumb-separator">/</span>
        <span>TODO {todo.id}</span>
      </div>
      <div className="todo-details-container">
        <h2 className="todo-details-title">{todo.title}</h2>
        <p className="todo-details-description">{todo.description}</p>
        <div className={`todo-details-state ${todo.state ? 'done' : 'todo'}`}>
            {todo.state ? 'DONE' : 'TO DO'}
        </div>
      </div>
    </div>
  );
}

export default TodoDetails;
