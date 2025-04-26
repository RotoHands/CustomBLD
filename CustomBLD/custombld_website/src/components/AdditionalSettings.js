import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';

const AdditionalSettings = ({ formData, handleChange }) => {
  const handleScrambleCountChange = (value) => {
    const count = Math.min(100, Math.max(1, parseInt(value) || 1));
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
            max="100"
            value={formData.scramble_count || 1}
            onChange={(e) => handleScrambleCountChange(e.target.value)}
            style={{ width: '100px' }}
          />
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Generate with Solutions</Form.Label>
        <Col sm="9">
          <div className="d-flex gap-3">
            <Form.Check
              type="radio"
              id="solutions-yes"
              label="yes"
              checked={formData.generate_solutions === 'yes'}
              onChange={() => handleSolutionsChange('yes')}
            />
            <Form.Check
              type="radio"
              id="solutions-no"
              label="no"
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