import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';


import TodoList from './components/TodoList/TodoList';
import TodoDetails from './components/TodoDetails/TodoDetails';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<TodoList />} />
        <Route path="/todo/:id" element={<TodoDetails />} />
      </Routes>
    </Router>
  );
}


export default App;