import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { cornerBufferOptions, parityOptions } from '../constants/Constants';

const CornerSection = ({ formData, handleChange, renderNumberSelect }) => {
  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Corner Buffer</Form.Label>
        <Col sm="9">
          <Form.Select name="corner_buffer" value={formData.corner_buffer} onChange={handleChange}>
            <option value="">Select Buffer</option>
            {cornerBufferOptions.map(buffer => <option key={buffer}>{buffer}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>
      {renderNumberSelect('corner_length', 0, 20, 'Corner Length')}
      {renderNumberSelect('corners_cycle_breaks', 0, 10, 'Cycle Breaks')}
      {renderNumberSelect('twist_clockwise', 0, 8, 'Clockwise Twists')}
      {renderNumberSelect('twist_counterclockwise', 0, 8, 'Counter-Clockwise Twists')}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Corner Parity</Form.Label>
        <Col sm="9">
          <Form.Select name="corner_parity" value={formData.corner_parity} onChange={handleChange}>
            {parityOptions.map(option => <option key={option}>{option}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>
    </>
  );
};

export default CornerSection;