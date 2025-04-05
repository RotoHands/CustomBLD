import React, { useState } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import QueryForm from './QueryForm';
import ScrambleResults from './components/ScrambleResults';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const [results, setResults] = useState([]);
  const [error, setError] = useState(null);

  const handleFormSubmit = (results, errorMessage) => {
    if (errorMessage) {
      setError(errorMessage);
      setResults([]);
    } else {
      setError(null);
      setResults(results);
    }
  };

  return (
    <Container fluid="md">
      <Row>
        <Col>
          <h1 className="text-center my-4">Custom BLD Scramble Generator</h1>
          
          <QueryForm onSubmit={handleFormSubmit} />
          
          {error && (
            <div className="alert alert-danger my-4" role="alert">
              {error}
            </div>
          )}
          
          {results && results.length > 0 && (
            <ScrambleResults results={results} />
          )}
        </Col>
      </Row>
    </Container>
  );
}

export default App;