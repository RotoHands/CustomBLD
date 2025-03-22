import React, { useState, useEffect } from 'react';
import { Form, Button, Card, Accordion } from 'react-bootstrap';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import EdgeSection from './components/EdgeSection';
import CornerSection from './components/CornerSection';
import WingSection from './components/WingSection';
import XCenterSection from './components/XCenterSection';
import TCenterSection from './components/TCenterSection';
import LetterSchemeSection from './components/LetterSchemeSection';
import { scrambleTypes } from './constants/Constants';
import { defaultLetterScheme } from './components/LetterScheme';
import AdditionalSettings from './components/AdditionalSettings';
import { 
  basePositions,
  cornerPositions, 
  edgePositions, 
  wingPositions, 
  xCenterPositions, 
  tCenterPositions 
} from './components/LetterScheme';


const QueryForm = ({ onSubmit }) => {
  const [formData, setFormData] = useState({
    scramble_type: '',
    edge_buffer: 'UF',  // Set default buffer to UF
    edge_length_type: 'random',
    edge_length: 0,
    edge_length_min: 0,
    edge_length_max: 18,
    edge_cycle_breaks_type: 'random',
    edge_cycle_breaks: 'random',
    edge_cycle_breaks_min: 0,
    edge_cycle_breaks_max: 6,
    edges_flipped_type: 'random',
    edges_flipped: 'random',
    edges_flipped_min: 0,
    edges_flipped_max: 12,
    edges_flipped: 6,   
    edges_solved: 'random',
    first_edges: '',
    corner_buffer: 'UFR',  // Set default buffer to UFR
    corner_length_type: 'random',
    corner_length: 'random',
    corner_length_min: 0,
    corner_length_max: 16,
    corners_cycle_breaks_type: 'random',
    corners_cycle_breaks: 'random',
    corners_cycle_breaks_min: 0,
    corners_cycle_breaks_max: 7,
    corners_cw_twists_type: 'random',
    corners_cw_twists_min: 0,
    corners_cw_twists_max: 7,
    corners_ccw_twists_type: 'random',
    corners_ccw_twists_min: 0,
    corners_ccw_twists_max: 7,
    twist_clockwise: 'random',
    twist_counterclockwise: 'random',
    corner_parity: 'random',
    corners_solved_type: 'random',
    corners_solved_min: 0,
    corners_solved_max: 8,
    first_corners: '',
    wing_buffer: 'UFr',
    wings_length_type: 'random',
    wings_length_min: 0,
    wings_length_max: 40,
    wings_cycle_breaks_type: 'random',
    wings_cycle_breaks_min: 0,
    wings_cycle_breaks_max: 10,
    wings_solved_type: 'random',
    wings_solved_min: 0,
    wings_solved_max: 24,
    wing_parity: 'random',
    first_wings: '',
    xcenter_buffer: 'Ufr',
    x_centers_length_type: 'random',
    x_centers_length_min: 0,
    x_centers_length_max: 24,
    x_centers_cycle_breaks_type: 'random',
    x_centers_cycle_breaks_min: 0,
    x_centers_cycle_breaks_max: 8,
    x_centers_solved_type: 'random',
    x_centers_solved_min: 0,
    x_centers_solved_max: 24,
    xcenter_parity: 'random',
    first_xcenters: '',
    tcenter_buffer: 'Uf',
    t_centers_length_type: 'random',
    t_centers_length_min: 0,
    t_centers_length_max: 30,
    t_centers_cycle_breaks_type: 'random',
    t_centers_cycle_breaks_min: 0,
    t_centers_cycle_breaks_max: 8,
    t_centers_solved_type: 'random',
    t_centers_solved_min: 0,
    t_centers_solved_max: 24,
    tcenter_parity: 'random',
    first_tcenters: '',
    letterScheme: defaultLetterScheme,
    practiceLetters: {
      edges: [],
      corners: [],
      wings: [],
      xCenters: [],
      tCenters: []
    },
    scramble_count: 1,
    generate_solutions: 'yes'
  });

  useEffect(() => {
    const savedForm = localStorage.getItem('scrambleForm');
    if (savedForm) {
      setFormData(JSON.parse(savedForm));
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleLetterChange = (piece, pos, value) => {
    setFormData(prev => {
      const newData = { ...prev };
      
      if (piece === 'base') {
        // Update base scheme
        newData.letterScheme = {
          ...prev.letterScheme,
          base: {
            ...prev.letterScheme.base,
            [pos]: value
          }
        };
      } else {
        // Update specific piece scheme
        newData.letterScheme = {
          ...prev.letterScheme,
          [piece]: {
            ...prev.letterScheme[piece],
            [pos]: value
          }
        };
      }
      
      return newData;
    });
  };

  const handlePracticeLetterChange = (piece, pos, isChecked) => {
    setFormData(prev => ({
      ...prev,
      practiceLetters: {
        ...prev.practiceLetters,
        [piece]: isChecked 
          ? [...prev.practiceLetters[piece], pos]
          : prev.practiceLetters[piece].filter(p => p !== pos)
      }
    }));
  };

  const saveSettings = () => {
    const settingsToSave = {
      // Scramble Type
      scramble_type: formData.scramble_type,
      
      // Edges
      edge_buffer: formData.edge_buffer,
      edge_length_type: formData.edge_length_type,
      edge_length_min: formData.edge_length_min,
      edge_length_max: formData.edge_length_max,
      edge_cycle_breaks_type: formData.edge_cycle_breaks_type,
      edge_cycle_breaks_min: formData.edge_cycle_breaks_min,
      edge_cycle_breaks_max: formData.edge_cycle_breaks_max,
      edges_flipped_type: formData.edges_flipped_type,
      edges_flipped_min: formData.edges_flipped_min,
      edges_flipped_max: formData.edges_flipped_max,
      edge_parity: formData.edge_parity,
      
      // Corners
      corner_buffer: formData.corner_buffer,
      corner_length_type: formData.corner_length_type,
      corner_length_min: formData.corner_length_min,
      corner_length_max: formData.corner_length_max,
      corners_cycle_breaks_type: formData.corners_cycle_breaks_type,
      corners_cycle_breaks_min: formData.corners_cycle_breaks_min,
      corners_cycle_breaks_max: formData.corners_cycle_breaks_max,
      corner_parity: formData.corner_parity,
      
      // Wings
      wing_buffer: formData.wing_buffer,
      wings_length_type: formData.wings_length_type,
      wings_length_min: formData.wings_length_min,
      wings_length_max: formData.wings_length_max,
      wings_cycle_breaks_type: formData.wings_cycle_breaks_type,
      wings_cycle_breaks_min: formData.wings_cycle_breaks_min,
      wings_cycle_breaks_max: formData.wings_cycle_breaks_max,
      wing_parity: formData.wing_parity,
      
      // X-Centers
      xcenter_buffer: formData.xcenter_buffer,
      x_centers_length_type: formData.x_centers_length_type,
      x_centers_length_min: formData.x_centers_length_min,
      x_centers_length_max: formData.x_centers_length_max,
      x_centers_cycle_breaks_type: formData.x_centers_cycle_breaks_type,
      x_centers_cycle_breaks_min: formData.x_centers_cycle_breaks_min,
      x_centers_cycle_breaks_max: formData.x_centers_cycle_breaks_max,
      xcenter_parity: formData.xcenter_parity,
      
      // T-Centers
      tcenter_buffer: formData.tcenter_buffer,
      t_centers_length_type: formData.t_centers_length_type,
      t_centers_length_min: formData.t_centers_length_min,
      t_centers_length_max: formData.t_centers_length_max,
      t_centers_cycle_breaks_type: formData.t_centers_cycle_breaks_type,
      t_centers_cycle_breaks_min: formData.t_centers_cycle_breaks_min,
      t_centers_cycle_breaks_max: formData.t_centers_cycle_breaks_max,
      tcenter_parity: formData.tcenter_parity,
      
      // Letter Scheme
      letterScheme: formData.letterScheme,
      
      // Additional Settings
      scramble_count: formData.scramble_count,
      generate_solutions: formData.generate_solutions
    };
  
    localStorage.setItem('bldSettings', JSON.stringify(settingsToSave));
    toast.success('Settings saved successfully');
  };

  const resetSettings = () => {
    if (window.confirm('Are you sure you want to reset all settings to default?')) {
      localStorage.removeItem('scrambleForm');
      setFormData({
        scramble_type: '',
        edge_buffer: 'UF',  // Set default buffer to UF
        edge_length_type: 'random',
        edge_length: 0,
        edge_length_min: 0,
        edge_length_max: 18,
        edges_cycle_breaks: 'random',
        edges_flipped: 'random',
        edges_solved: 'random',
        first_edges: '',
        corner_buffer: 'UFR',  // Set default buffer to UBL
        corner_length_type: 'random',
        corner_length: 'random',
        corner_length_min: 0,
        corner_length_max: 16,
        corners_cycle_breaks_type: 'random',
        corners_cycle_breaks: 'random',
        corners_cycle_breaks_min: 0,
        corners_cycle_breaks_max: 6,
        corners_cw_twists_type: 'random',
        corners_cw_twists_min: 0,
        corners_cw_twists_max: 7,
        corners_ccw_twists_type: 'random',
        corners_ccw_twists_min: 0,
        corners_ccw_twists_max: 7,
        twist_clockwise: 'random',
        twist_counterclockwise: 'random',
        corner_parity: 'random',
        corners_solved_type: 'random',
        corners_solved_min: 0,
        corners_solved_max: 8,
        first_corners: '',
        wing_buffer: 'UFr',
        wings_length_type: 'random',
        wings_length_min: 0,
        wings_length_max: 40,
        wings_cycle_breaks_type: 'random',
        wings_cycle_breaks_min: 0,
        wings_cycle_breaks_max: 10,
        wings_solved_type: 'random',
        wings_solved_min: 0,
        wings_solved_max: 24,
        wing_parity: 'random',
        first_wings: '',
        xcenter_buffer: 'UFr',
        x_centers_length_type: 'random',
        x_centers_length_min: 0,
        x_centers_length_max: 30,
        x_centers_cycle_breaks_type: 'random',
        x_centers_cycle_breaks_min: 0,
        x_centers_cycle_breaks_max: 8,
        x_centers_solved_type: 'random',
        x_centers_solved_min: 0,
        x_centers_solved_max: 24,
        xcenter_parity: 'random',
        first_xcenters: '',
        tcenter_buffer: 'Uf',
        t_centers_length_type: 'random',
        t_centers_length_min: 0,
        t_centers_length_max: 30,
        t_centers_cycle_breaks_type: 'random',
        t_centers_cycle_breaks_min: 0,
        t_centers_cycle_breaks_max: 8,
        t_centers_solved_type: 'random',
        t_centers_solved_min: 0,
        t_centers_solved_max: 24,
        tcenter_parity: 'random',
        first_tcenters: '',
        letterScheme: defaultLetterScheme,
        practiceLetters: {
          edges: [],
          corners: [],
          wings: [],
          xCenters: [],
          tCenters: []
        },
        scramble_count: 1,
        generate_solutions: 'yes'
      });
      localStorage.removeItem('letterScheme');
      basePositions.forEach((pos, index) => {
        handleLetterChange('base', pos, String.fromCharCode(65 + index));
      });
    }
  };

  const renderNumberSelect = (name, min, max, label) => (
    <Form.Group className="mb-3">
      <Form.Label>{label}</Form.Label>
      <Form.Select name={name} value={formData[name]} onChange={handleChange}>
        <option value="random">random</option>
        {[...Array(max - min + 1)].map((_, i) => (
          <option key={i + min} value={i + min}>{i + min}</option>
        ))}
      </Form.Select>
    </Form.Group>
  );

  return (
    <Card className="p-4 my-4 shadow-sm bg-light">
      <Card.Body>
        <h2 className="text-primary text-center mb-4">BLD Scramble Generator</h2>
        
        <Form onSubmit={async (e) => {
          e.preventDefault();
          
          // Create a filtered payload based on selected options
          const payload = {
            // Always include these fields
            scramble_type: formData.scramble_type,
            letterScheme: formData.letterScheme,
            scramble_count: formData.scramble_count,
            generate_solutions: formData.generate_solutions
          };
          
          // Add edge data if needed (3BLD, 4BLD, 5BLD or specific edge selections)
          if (['3bld', '3bld_edges', '5bld', '5bld_edges'].includes(formData.scramble_type)) {
            payload.edge_buffer = formData.edge_buffer;
            
            // Handle edge length based on selection
            if (formData.edge_length_type === 'random') {
              payload.edge_length_type = 'random';
            } else {
              payload.edge_length_type = 'range';  // Explicitly set type to 'range'
              payload.edge_length_min = formData.edge_length_min;
              payload.edge_length_max = formData.edge_length_max;
            }
            
            // Handle cycle breaks based on selection
            if (formData.edge_cycle_breaks_type === 'random') {
              payload.edge_cycle_breaks_type = 'random';
            } else {
              payload.edge_cycle_breaks_type = 'range';  // Explicitly set type to 'range'
              payload.edge_cycle_breaks_min = formData.edge_cycle_breaks_min;
              payload.edge_cycle_breaks_max = formData.edge_cycle_breaks_max;
            }
            
            // Handle flipped edges based on selection
            if (formData.edges_flipped_type === 'random') {
              payload.edges_flipped_type = 'random';
            } else {
              payload.edges_flipped_type = 'range';  // Explicitly set type to 'range'
              payload.edges_flipped_min = formData.edges_flipped_min;
              payload.edges_flipped_max = formData.edges_flipped_max;
            }
            
            // Add solved edges
            payload.edges_solved = formData.edges_solved;
            
            // Add edge parity
            payload.edge_parity = formData.edge_parity;
          }
          
          // Add corner data if needed (3BLD, 4BLD, 5BLD or specific corner selections)
          if (['3bld', '3bld_corners', '4bld', '5bld'].includes(formData.scramble_type)) {
            payload.corner_buffer = formData.corner_buffer;
            
            // Handle corner length based on selection
            if (formData.corner_length_type === 'random') {
              payload.corner_length_type = 'random';
            } else {
              payload.corner_length_type = 'range';  // Explicitly set type to 'range'
              payload.corner_length_min = formData.corner_length_min;
              payload.corner_length_max = formData.corner_length_max;
            }
            
            // Handle cycle breaks based on selection
            if (formData.corners_cycle_breaks_type === 'random') {
              payload.corners_cycle_breaks_type = 'random';
            } else {
              payload.corners_cycle_breaks_type = 'range';  // Explicitly set type to 'range'
              payload.corners_cycle_breaks_min = formData.corners_cycle_breaks_min;
              payload.corners_cycle_breaks_max = formData.corners_cycle_breaks_max;
            }
            
            // Add solved corners
            payload.corners_solved = formData.corners_solved;
            
            // Add corner parity and twists
            payload.corner_parity = formData.corner_parity;
            
            if (formData.corners_cw_twists_type === 'random') {
              payload.corners_cw_twists_type = 'random';
            } else {
              payload.corners_cw_twists_type = 'range';  // Explicitly set type to 'range'
              payload.corners_cw_twists_min = formData.corners_cw_twists_min;
              payload.corners_cw_twists_max = formData.corners_cw_twists_max;
            }
            
            if (formData.corners_ccw_twists_type === 'random') {
              payload.corners_ccw_twists_type = 'random';
            } else {
              payload.corners_ccw_twists_type = 'range';  // Explicitly set type to 'range'
              payload.corners_ccw_twists_min = formData.corners_ccw_twists_min;
              payload.corners_ccw_twists_max = formData.corners_ccw_twists_max;
            }
          }
          
          // Add wing data if needed (4BLD, 5BLD)
          if (['4bld', '4bld_wings', '5bld'].includes(formData.scramble_type)) {
            payload.wing_buffer = formData.wing_buffer;
            
            // Handle wings length based on selection
            if (formData.wings_length_type === 'random') {
              payload.wings_length_type = 'random';
            } else {
              payload.wings_length_type = 'range';  // Explicitly set type to 'range'
              payload.wings_length_min = formData.wings_length_min;
              payload.wings_length_max = formData.wings_length_max;
            }
            
            // Handle cycle breaks based on selection
            if (formData.wings_cycle_breaks_type === 'random') {
              payload.wings_cycle_breaks_type = 'random';
            } else {
              payload.wings_cycle_breaks_type = 'range';  // Explicitly set type to 'range'
              payload.wings_cycle_breaks_min = formData.wings_cycle_breaks_min;
              payload.wings_cycle_breaks_max = formData.wings_cycle_breaks_max;
            }
            
            // Handle solved wings based on selection
            if (formData.wings_solved_type === 'random') {
              payload.wings_solved_type = 'random';
            } else {
              payload.wings_solved_type = 'range';  // Explicitly set type to 'range'
              payload.wings_solved_min = formData.wings_solved_min;
              payload.wings_solved_max = formData.wings_solved_max;
            }
            
            // Add wing parity
            payload.wing_parity = formData.wing_parity;
          }
          
          // Add x-center data if needed (4BLD, 5BLD)
          if (['4bld', '4bld_centers', '5bld'].includes(formData.scramble_type)) {
            payload.xcenter_buffer = formData.xcenter_buffer;
            
            // Handle x-centers length based on selection
            if (formData.x_centers_length_type === 'random') {
              payload.x_centers_length_type = 'random';
            } else {
              payload.x_centers_length_type = 'range';  // Explicitly set type to 'range'
              payload.x_centers_length_min = formData.x_centers_length_min;
              payload.x_centers_length_max = formData.x_centers_length_max;
            }
            
            // Handle cycle breaks based on selection
            if (formData.x_centers_cycle_breaks_type === 'random') {
              payload.x_centers_cycle_breaks_type = 'random';
            } else {
              payload.x_centers_cycle_breaks_type = 'range';  // Explicitly set type to 'range'
              payload.x_centers_cycle_breaks_min = formData.x_centers_cycle_breaks_min;
              payload.x_centers_cycle_breaks_max = formData.x_centers_cycle_breaks_max;
            }
            
            // Handle solved x-centers based on selection
            if (formData.x_centers_solved_type === 'random') {
              payload.x_centers_solved_type = 'random';
            } else {
              payload.x_centers_solved_type = 'range';  // Explicitly set type to 'range'
              payload.x_centers_solved_min = formData.x_centers_solved_min;
              payload.x_centers_solved_max = formData.x_centers_solved_max;
            }
            
            // Add x-center parity
            payload.xcenter_parity = formData.xcenter_parity;
          }
          
          // Add t-center data if needed (5BLD only)
          if (['5bld'].includes(formData.scramble_type)) {
            payload.tcenter_buffer = formData.tcenter_buffer;
            
            // Handle t-centers length based on selection
            if (formData.t_centers_length_type === 'random') {
              payload.t_centers_length_type = 'random';
            } else {
              payload.t_centers_length_type = 'range';  // Explicitly set type to 'range'
              payload.t_centers_length_min = formData.t_centers_length_min;
              payload.t_centers_length_max = formData.t_centers_length_max;
            }
            
            // Handle cycle breaks based on selection
            if (formData.t_centers_cycle_breaks_type === 'random') {
              payload.t_centers_cycle_breaks_type = 'random';
            } else {
              payload.t_centers_cycle_breaks_type = 'range';  // Explicitly set type to 'range'
              payload.t_centers_cycle_breaks_min = formData.t_centers_cycle_breaks_min;
              payload.t_centers_cycle_breaks_max = formData.t_centers_cycle_breaks_max;
            }
            
            // Handle solved t-centers based on selection
            if (formData.t_centers_solved_type === 'random') {
              payload.t_centers_solved_type = 'random';
            } else {
              payload.t_centers_solved_type = 'range';  // Explicitly set type to 'range'
              payload.t_centers_solved_min = formData.t_centers_solved_min;
              payload.t_centers_solved_max = formData.t_centers_solved_max;
            }
            
            // Add t-center parity
            payload.tcenter_parity = formData.tcenter_parity;
          }
          
          // Add practice letters if any are selected
          if (Object.values(formData.practiceLetters).some(arr => arr.length > 0)) {
            payload.practiceLetters = formData.practiceLetters;
          }
          
          try {
            const response = await fetch('http://localhost:5000/query-scrambles', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(payload)
            });
            
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            
            const data = await response.json();
            onSubmit(data);
          } catch (error) {
            toast.error('Failed to generate scrambles. Please try again.');
            console.error('Error:', error);
          }
        }}>
          <Accordion defaultActiveKey={['0','1','2','6', '7']} alwaysOpen>
            <Accordion.Item eventKey="0">
              <Accordion.Header>Scramble Type</Accordion.Header>
              <Accordion.Body>
                <Form.Group className="mb-3">
                  <Form.Select 
                    name="scramble_type"
                    value={formData.scramble_type}
                    onChange={handleChange}
                  >
                    <option value="">Select Scramble Type</option>
                    {scrambleTypes.map(type => (
                      <option key={type} value={type}>{type}</option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="1">
              <Accordion.Header>Edges</Accordion.Header>
              <Accordion.Body>
                <EdgeSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                  handlePracticeLetterChange={handlePracticeLetterChange}
                  handleLetterChange={handleLetterChange}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="2">
              <Accordion.Header>Corners</Accordion.Header>
              <Accordion.Body>
                <CornerSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                  handlePracticeLetterChange={handlePracticeLetterChange}
                  handleLetterChange={handleLetterChange}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="3">
              <Accordion.Header>Wings</Accordion.Header>
              <Accordion.Body>
                <WingSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                  handlePracticeLetterChange={handlePracticeLetterChange}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="4">
              <Accordion.Header>X-Centers</Accordion.Header>
              <Accordion.Body>
                <XCenterSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                  handlePracticeLetterChange={handlePracticeLetterChange}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="5">
              <Accordion.Header>T-Centers</Accordion.Header>
              <Accordion.Body>
                <TCenterSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                  handlePracticeLetterChange={handlePracticeLetterChange}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="6">
              <Accordion.Header>Letter Scheme</Accordion.Header>
              <Accordion.Body>
                <LetterSchemeSection 
                  formData={formData}
                  handleLetterChange={handleLetterChange}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="7">
              <Accordion.Header>Additional Settings</Accordion.Header>
              <Accordion.Body>
                <AdditionalSettings 
                  formData={formData}
                  handleChange={handleChange}
                />
              </Accordion.Body>
            </Accordion.Item>
          </Accordion>

          <div className="d-flex justify-content-between mt-4">
            <Button 
              variant="primary" 
              onClick={saveSettings}
              type="button"
            >
              Save Settings
            </Button>
            <Button 
              variant="outline-danger" 
              onClick={resetSettings}
              type="button"
            >
              Reset All
            </Button>
          </div>

          <div className="d-grid gap-2 mt-4">
            <Button variant="primary" size="lg" type="submit">
              Generate Custom Scrambles!
            </Button>
          </div>
        </Form>
      </Card.Body>
    </Card>
  );
};

export default QueryForm;