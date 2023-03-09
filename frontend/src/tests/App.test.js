import React from 'react';
import { render, screen } from '@testing-library/react';
import App from '../App';
import { MemoryRouter } from 'react-router-dom';

test('renders TodoList component', () => {
  render(
    <MemoryRouter>
      <App />
    </MemoryRouter>
  );
  const todoListElement = screen.getByText(/Ma TODO List/i);
  expect(todoListElement).toBeInTheDocument();
});
