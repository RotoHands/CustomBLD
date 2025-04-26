import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';

const AdditionalSettings = ({ formData, handleChange }) => {
  const handleScrambleCountChange = (value) => {
    const count = Math.min(500, Math.max(1, parseInt(value) || 1));
    handleChange({
      target: {
        name: 'scramble_count',
        value: count
      }
    });
  };

  const handleSolutionsChange = (value) => {
    handleChange({
      target: {
        name: 'generate_solutions',
        value
      }
    });
  };

  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Scrambles to Generate</Form.Label>
        <Col sm="9">
          <Form.Control
            type="number"
            min="1"
            max="500"
            value={formData.scramble_count || 1}
            onChange={(e) => handleScrambleCountChange(e.target.value)}
            style={{ width: '100px' }}
          />
        </Col>
      </Form.Group>
    </>
  );
};

export default AdditionalSettings;