import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';

const AdditionalSettings = ({ formData, handleChange }) => {
  // Generate options for the dropdown
  const options = [];
  for (let i = 1; i <= 1000; i++) {
    options.push(
      <option key={i} value={i}>{i}</option>
    );
  }
  
  return (
    <Form.Group as={Row} className="mb-3">
      <Form.Label column sm="3" className="fw-bold">Number of Scrambles</Form.Label>
      <Col sm="9">
        <Form.Select
          name="scramble_count"
          value={formData.scramble_count || 15}
          onChange={handleChange}
          style={{ width: '200px' }}
        >
          {options}
        </Form.Select>
      </Col>
    </Form.Group>
  );
};

export default AdditionalSettings;