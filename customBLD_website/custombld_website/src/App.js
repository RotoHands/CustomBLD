import React, { useState } from 'react';
import { Container, Card, ListGroup } from 'react-bootstrap';
import QueryForm from './QueryForm';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const App = () => {
  const [scrambleResults, setScrambleResults] = useState(null);

  return (
    <>
      <Container className="mt-5">
        <h1>Custom BLD Query</h1>
        <QueryForm onSubmit={setScrambleResults} />
        
        {scrambleResults && scrambleResults.scrambles && (
          <Card className="mt-4">
            <Card.Header as="h5">Scramble Results</Card.Header>
            <ListGroup variant="flush">
              {scrambleResults.scrambles.map((scramble, index) => (
                <ListGroup.Item key={index}>
                  <div className="fw-bold">Scramble {index + 1}:</div>
                  <div className="mb-2">{scramble}</div>
                  {scrambleResults.solutions && scrambleResults.solutions[index] && (
                    <>
                      <div className="fw-bold">Solution:</div>
                      <div>{scrambleResults.solutions[index]}</div>
                    </>
                  )}
                </ListGroup.Item>
              ))}
            </ListGroup>
          </Card>
        )}
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