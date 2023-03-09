import React from 'react';
import { Routes, Route } from 'react-router-dom';


import TodoList from './components/TodoList/TodoList';
import TodoDetails from './components/TodoDetails/TodoDetails';


function App() {
  return (
    <Routes>
      <Route path="/" element={<TodoList />} />
      <Route path="/todo/:id" element={<TodoDetails />} />
    </Routes>
  );
}


export default App;
