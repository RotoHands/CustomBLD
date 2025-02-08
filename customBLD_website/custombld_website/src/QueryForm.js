import React, { useState } from 'react';
import { Form, Button, Row, Col } from 'react-bootstrap';

const scrambleTypes = [
  '3bld', '3bld_edges', '3bld_corners', '4bld', '4bld_centers', '4bld_wings', '5bld', '5bld_edge'
];

const QueryForm = ({ onSubmit }) => {
  const [formData, setFormData] = useState({
    scramble_type: '',
    edge_buffer: '',
    edges: '',
    edge_length: '',
    edges_cycle_breaks: '',
    edges_flipped: '',
    edges_solved: '',
    flips: '',
    first_edges: '',
    corner_buffer: '',
    corners: '',
    corner_length: '',
    corners_cycle_breaks: '',
    twist_clockwise: '',
    twist_counterclockwise: '',
    corners_twisted: '',
    corners_solved: '',
    corner_parity: '',
    first_corners: '',
    wing_buffer: '',
    wings: '',
    wings_length: '',
    wings_cycle_breaks: '',
    wings_solved: '',
    wing_parity: '',
    first_wings: '',
    xcenter_buffer: '',
    xcenters: '',
    xcenter_length: '',
    xcenters_cycle_breaks: '',
    xcenters_solved: '',
    xcenter_parity: '',
    first_xcenters: '',
    tcenter_buffer: '',
    tcenters: '',
    tcenter_length: '',
    tcenters_cycle_breaks: '',
    tcenters_solved: '',
    tcenter_parity: '',
    first_tcenters: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="2">
          Scramble Type:
        </Form.Label>
        <Col sm="10">
          <Form.Control
            as="select"
            name="scramble_type"
            value={formData.scramble_type}
            onChange={handleChange}
          >
            {/* <option value="">Select Scramble Type</option> */}
            {scrambleTypes.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </Form.Control>
        </Col>
      </Form.Group>
      {Object.keys(formData).filter(key => key !== 'scramble_type').map((key) => (
        <Form.Group as={Row} key={key} className="mb-3">
          <Form.Label column sm="2">
            {key.replace(/_/g, ' ')}:
          </Form.Label>
          <Col sm="10">
            <Form.Control
              type="text"
              name={key}
              value={formData[key]}
              onChange={handleChange}
            />
          </Col>
        </Form.Group>
      ))}
      <Button variant="primary" type="submit">
        Submit
      </Button>
    </Form>
  );
};

export default QueryForm;