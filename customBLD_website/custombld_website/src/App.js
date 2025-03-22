import React from 'react';
import { Container } from 'react-bootstrap';
import QueryForm from './QueryForm';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const App = () => {
  const handleFormSubmit = async (formData) => {
    try {
      const response = await fetch('http://localhost:5000/query-scarmbels', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });
      const result = await response.json();
      console.log(result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <>
      <Container className="mt-5">
        <h1>Custom BLD Query</h1>
        <QueryForm onSubmit={handleFormSubmit} />
      </Container>
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </>
  );
};

export default App;