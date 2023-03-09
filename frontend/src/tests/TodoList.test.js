import React from 'react';
import { render, fireEvent, waitFor, screen } from '@testing-library/react';
import axios from 'axios';
import TodoList from '../components/TodoList/TodoList';
import { MemoryRouter } from 'react-router-dom';

jest.mock('axios');

describe('TodoList', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('should fetch and display todos', async () => {
    const todos = [
      { id: 1, title: 'Todo 1', description: 'Description 1', state: false },
      { id: 2, title: 'Todo 2', description: 'Description 2', state: true },
      { id: 3, title: 'Todo 3', description: 'Description 3', state: false }
    ];

    axios.get.mockResolvedValue({ data: { todos } });

    render(<MemoryRouter><TodoList /></ MemoryRouter>);

    expect(await screen.findByText('Todo 1')).toBeInTheDocument();
    expect(screen.getByText('Todo 2')).toBeInTheDocument();
    expect(screen.getByText('Todo 3')).toBeInTheDocument();

    expect(axios.get).toHaveBeenCalledTimes(1);
    expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('/api/todos'));
  });

  it('should toggle todo state when checkbox is clicked', async () => {
    const todos = [{ id: 1, title: 'Todo 1', description: 'Description 1', state: false }];

    axios.get.mockResolvedValue({ data: { todos } });
    axios.put.mockResolvedValue({ data: { todos: [{ id: 1, title: 'Todo 1', description: 'Description 1', state: true }] } });

    render(<MemoryRouter><TodoList /></ MemoryRouter>);

    expect(await screen.findByText('Todo 1')).toBeInTheDocument();

    fireEvent.click(screen.getByRole('checkbox'));

    expect(axios.put).toHaveBeenCalledTimes(1);
    expect(axios.put).toHaveBeenCalledWith(expect.stringContaining('/1'), { state: 1 });
  });

  it('should delete todo when delete button is clicked', async () => {
    const todos = [{ id: 1, title: 'Todo 1', description: 'Description 1', state: false }];

    axios.get.mockResolvedValue({ data: { todos } });
    axios.delete.mockResolvedValue({ data: { todos: [] } });

    render(<MemoryRouter><TodoList /></ MemoryRouter>);

    expect(await screen.findByText('Todo 1')).toBeInTheDocument();

    fireEvent.click(screen.getByText('Supprimer'));

    expect(axios.delete).toHaveBeenCalledTimes(1);
    expect(axios.delete).toHaveBeenCalledWith(expect.stringContaining('/1'));
  });

  it('should create todo when form is submitted', async () => {
    const todos = [];

    axios.get.mockResolvedValue({ data: { todos } });
    axios.post.mockResolvedValue({ data: { todos: [{ id: 1, title: 'Todo 1', description: 'Description 1', state: false }] } });

    render(<MemoryRouter><TodoList /></ MemoryRouter>);

    fireEvent.change(screen.getByPlaceholderText('Titre'), { target: { value: 'Todo 1' } });
    fireEvent.change(screen.getByPlaceholderText('Description'), { target: { value: 'Description 1' } });
    fireEvent.click(screen.getByText('Ajouter'));

    expect(axios.post).toHaveBeenCalledTimes(1);
    expect(axios.post).toHaveBeenCalledWith(expect.stringContaining('/api/todo'), {
      title: 'Todo 1',
      description: 'Description 1'
    });
  });
});  




