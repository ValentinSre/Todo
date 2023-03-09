import React from 'react';
import { render, waitFor, screen } from '@testing-library/react';
import axios from 'axios';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import TodoDetails from '../components/TodoDetails/TodoDetails';

jest.mock('axios');

describe('TodoDetails component', () => {
  const mockedTodo = {
    id: 1,
    title: 'Test todo',
    description: 'Test todo description',
    state: false,
  };

  it('should render loading message and fetch todo', async () => {
    axios.get.mockResolvedValueOnce({ data: mockedTodo });

    render(
      <MemoryRouter initialEntries={[`/todo/${mockedTodo.id}`]}>
        <Routes>
          <Route path="/todo/:id" element={<TodoDetails />} />
        </Routes>
      </MemoryRouter>
    );

    expect(screen.getByText('Loading...')).toBeInTheDocument();
    await waitFor(() => expect(axios.get).toHaveBeenCalled());
  });

  it('should render todo details', async () => {
    axios.get.mockResolvedValueOnce({ data: mockedTodo });

    render(
      <MemoryRouter initialEntries={[`/todo/${mockedTodo.id}`]}>
        <Routes>
          <Route path="/todo/:id" element={<TodoDetails />} />
        </Routes>
      </MemoryRouter>
    );

    expect(await screen.findByText(mockedTodo.title)).toBeInTheDocument();
    expect(screen.getByText(mockedTodo.description)).toBeInTheDocument();
    expect(screen.getByText(mockedTodo.state ? 'DONE' : 'TO DO')).toBeInTheDocument();
  });

  it('should log error when todo fetch fails', async () => {
    const error = new Error('Something went wrong!');
    axios.get.mockRejectedValueOnce(error);
    jest.spyOn(console, 'error').mockImplementation(() => {});

    render(
      <MemoryRouter initialEntries={[`/todo/${mockedTodo.id}`]}>
        <Routes>
          <Route path="/todo/:id" element={<TodoDetails />} />
        </Routes>
      </MemoryRouter>
    );

    expect(await screen.findByText('Loading...')).toBeInTheDocument();
    await waitFor(() => expect(console.error).toHaveBeenCalledWith(error));
  });
});
