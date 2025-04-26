import React, { useState, useEffect } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import QueryForm from './QueryForm';
import ScrambleResults from './components/ScrambleResults';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

function App() {
  const [results, setResults] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Apply pastel-blue-8 theme on component mount
    const theme = {
      primary: '#14539E',
      secondary: '#2B66A9',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '20, 83, 158',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    };

    Object.entries(theme).forEach(([key, value]) => {
      document.documentElement.style.setProperty(`--bs-${key}`, value);
    });

    document.body.style.backgroundColor = theme['body-bg'];
    document.body.style.color = theme['body-color'];

    const headers = document.querySelectorAll('h1, h2, h3, h4, h5, h6');
    headers.forEach(header => {
      header.style.color = theme['header-color'];
    });
  }, []);

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
    <Container className="py-4">
      <Row>
        <Col>
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