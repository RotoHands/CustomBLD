import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { edgeBufferOptions } from '../constants/Constants';

const EdgeSection = ({ formData, handleChange, renderNumberSelect }) => {
  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Edge Buffer</Form.Label>
        <Col sm="9">
          <Form.Select name="edge_buffer" value={formData.edge_buffer} onChange={handleChange}>
            <option value="">Select Buffer</option>
            {edgeBufferOptions.map(buffer => <option key={buffer}>{buffer}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>
      {renderNumberSelect('edge_length', 0, 20, 'Edge Length')}
      {renderNumberSelect('edges_cycle_breaks', 0, 10, 'Cycle Breaks')}
      {renderNumberSelect('edges_flipped', 0, 12, 'Flipped Edges')}
      {renderNumberSelect('edges_solved', 0, 12, 'Solved Edges')}
    </>
  );
};

export default EdgeSection;