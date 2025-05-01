import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';

const AdditionalSettings = ({ formData, handleChange }) => {
  const handleScrambleCountChange = (value) => {
    const count = Math.min(2000, Math.max(1, parseInt(value) || 1));
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
      <Form.Group as={Row} className="mb-4">
        <Form.Label column sm="3" className="fw-bold fs-5">Scrambles to Generate</Form.Label>
        <Col sm="9">
          <Form.Control
            type="number"
            min="1"
            max="2000"
            value={formData.scramble_count || 1}
            onChange={(e) => handleScrambleCountChange(e.target.value)}
            className="form-control-lg"
            style={{ width: '200px' }}
          />
          <Form.Text muted>Enter a number between 1 and 2000</Form.Text>
        </Col>
      </Form.Group>
      
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Generate Solutions</Form.Label>
        <Col sm="9">
          <div className="d-flex">
            <Form.Check
              type="radio"
              id="solutions-yes"
              label="Yes"
              checked={formData.generate_solutions === 'yes'}
              onChange={() => handleSolutionsChange('yes')}
              className="me-3"
            />
            <Form.Check
              type="radio"
              id="solutions-no"
              label="No"
              checked={formData.generate_solutions === 'no'}
              onChange={() => handleSolutionsChange('no')}
            />
          </div>
        </Col>
      </Form.Group>
    </>
  );
};

export default AdditionalSettings;