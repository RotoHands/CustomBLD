import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { xCenterBufferOptions, parityOptions } from '../constants/Constants';
import { xCenterPositions } from './LetterScheme';

const XCenterSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange }) => {
  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">X-Center Buffer</Form.Label>
        <Col sm="9">
          <Form.Select name="xcenter_buffer" value={formData.xcenter_buffer} onChange={handleChange}>
            <option value="">Select Buffer</option>
            {xCenterBufferOptions.map(buffer => <option key={buffer}>{buffer}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>
      {renderNumberSelect('xcenter_length', 0, 30, 'X-Center Length')}
      {renderNumberSelect('xcenters_cycle_breaks', 0, 10, 'Cycle Breaks')}
      {renderNumberSelect('xcenters_solved', 0, 24, 'Solved X-Centers')}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">X-Center Parity</Form.Label>
        <Col sm="9">
          <Form.Select name="xcenter_parity" value={formData.xcenter_parity} onChange={handleChange}>
            {parityOptions.map(option => <option key={option}>{option}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>

     
    </>
  );
};

export default XCenterSection;