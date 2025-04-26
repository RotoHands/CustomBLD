import React from 'react';
import { Form, Card } from 'react-bootstrap';

const ThemeSwitcher = () => {
  // Function to calculate color brightness
  const getBrightness = (hex) => {
    // Remove the # if present
    hex = hex.replace('#', '');
    
    // Convert to RGB
    const r = parseInt(hex.substr(0, 2), 16);
    const g = parseInt(hex.substr(2, 2), 16);
    const b = parseInt(hex.substr(4, 2), 16);
    
    // Calculate brightness using the formula: (0.299*R + 0.587*G + 0.114*B)
    return (0.299 * r + 0.587 * g + 0.114 * b) / 255;
  };

  // Function to determine text color based on background brightness
  const getTextColor = (bgColor) => {
    return getBrightness(bgColor) > 0.5 ? '#2C3E50' : '#FFFFFF';
  };

  const themes = {
    'pastel': {
      primary: '#B5EAD7',
      secondary: '#C1E1C1',
      success: '#98FB98',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#E0FFFF',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': 'var(--bs-light)',
      'accordion-header-color': 'var(--bs-dark)',
      'primary-rgb': '181, 234, 215',
      'header-color': '#000000',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-dark': {
      primary: '#B5EAD7',
      secondary: '#C1E1C1',
      success: '#98FB98',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#E0FFFF',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': 'var(--bs-dark)',
      'accordion-header-color': 'var(--bs-light)',
      'primary-rgb': '181, 234, 215',
      'header-color': '#FFFFFF',
      'body-bg': '#212529',
      'body-color': '#F8F9FA'
    },
    'pastel-blue': {
      primary: '#A7C7E7',
      secondary: '#B8C6DB',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '167, 199, 231',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-blue-1': {
      primary: '#B5D8EB',
      secondary: '#C5E1F0',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '181, 216, 235',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-blue-2': {
      primary: '#9EC5E0',
      secondary: '#B0D4E8',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '158, 197, 224',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-blue-3': {
      primary: '#87B2D5',
      secondary: '#9EC5E0',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '135, 178, 213',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-blue-4': {
      primary: '#709FCA',
      secondary: '#87B2D5',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '112, 159, 202',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-blue-5': {
      primary: '#598CBF',
      secondary: '#709FCA',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '89, 140, 191',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-blue-6': {
      primary: '#4279B4',
      secondary: '#598CBF',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '66, 121, 180',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-blue-7': {
      primary: '#2B66A9',
      secondary: '#4279B4',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '43, 102, 169',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-blue-8': {
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
    },
    'pastel-blue-9': {
      primary: '#004093',
      secondary: '#14539E',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '0, 64, 147',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    },
    'pastel-pink': {
      primary: '#FFB6C1',
      secondary: '#FFC0CB',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#FFF0F5',
      dark: '#343A40',
      'accordion-header-bg': '#FFF0F5',
      'accordion-header-color': getTextColor('#FFF0F5'),
      'primary-rgb': '255, 182, 193'
    },
    'pastel-green': {
      primary: '#98FB98',
      secondary: '#C1E1C1',
      success: '#90EE90',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F0FFF0',
      dark: '#343A40',
      'accordion-header-bg': '#F0FFF0',
      'accordion-header-color': getTextColor('#F0FFF0'),
      'primary-rgb': '152, 251, 152'
    },
    'pastel-purple': {
      primary: '#E6E6FA',
      secondary: '#D8BFD8',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F0FF',
      dark: '#343A40',
      'accordion-header-bg': '#F8F0FF',
      'accordion-header-color': getTextColor('#F8F0FF'),
      'primary-rgb': '230, 230, 250'
    },
    'pastel-yellow': {
      primary: '#FFFACD',
      secondary: '#FFE5B4',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#FFFFF0',
      dark: '#343A40',
      'accordion-header-bg': '#FFFFF0',
      'accordion-header-color': getTextColor('#FFFFF0'),
      'primary-rgb': '255, 250, 205'
    },
    'pastel-orange': {
      primary: '#FFDAB9',
      secondary: '#FFE5B4',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#FFF5EE',
      dark: '#343A40',
      'accordion-header-bg': '#FFF5EE',
      'accordion-header-color': getTextColor('#FFF5EE'),
      'primary-rgb': '255, 218, 185'
    },
    'pastel-mint': {
      primary: '#B5EAD7',
      secondary: '#C1E1C1',
      success: '#98FB98',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#E0FFFF',
      light: '#F0FFFF',
      dark: '#343A40',
      'accordion-header-bg': '#F0FFFF',
      'accordion-header-color': getTextColor('#F0FFFF'),
      'primary-rgb': '181, 234, 215'
    },
    'pastel-lavender': {
      primary: '#E6E6FA',
      secondary: '#D8BFD8',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F0FF',
      dark: '#343A40',
      'accordion-header-bg': '#F8F0FF',
      'accordion-header-color': getTextColor('#F8F0FF'),
      'primary-rgb': '230, 230, 250'
    },
    'pastel-peach': {
      primary: '#FFDAB9',
      secondary: '#FFE5B4',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#FFF5EE',
      dark: '#343A40',
      'accordion-header-bg': '#FFF5EE',
      'accordion-header-color': getTextColor('#FFF5EE'),
      'primary-rgb': '255, 218, 185'
    },
    'pastel-teal': {
      primary: '#B5EAD7',
      secondary: '#C1E1C1',
      success: '#98FB98',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#E0FFFF',
      light: '#F0FFFF',
      dark: '#343A40',
      'accordion-header-bg': '#F0FFFF',
      'accordion-header-color': getTextColor('#F0FFFF'),
      'primary-rgb': '181, 234, 215'
    }
  };

  const applyTheme = (themeName) => {
    const theme = themes[themeName];
    if (!theme) return;

    // Apply theme colors to CSS variables
    Object.entries(theme).forEach(([key, value]) => {
      document.documentElement.style.setProperty(`--bs-${key}`, value);
    });

    // Set body background and text color
    document.body.style.backgroundColor = theme['body-bg'];
    document.body.style.color = theme['body-color'];

    // Set header text color only
    const headers = document.querySelectorAll('h1, h2, h3, h4, h5, h6');
    headers.forEach(header => {
      header.style.color = theme['header-color'];
    });
  };

  const handleThemeChange = (themeName) => {
    applyTheme(themeName);
    localStorage.setItem('selectedTheme', themeName);
  };

  // Load saved theme on component mount
  React.useEffect(() => {
    const savedTheme = localStorage.getItem('selectedTheme') || 'pastel-blue';
    applyTheme(savedTheme);
  }, []);

  return (
    <Card className="p-3 mb-4">
      <Card.Body>
        <h5 className="mb-3">Theme Selection</h5>
        <Form>
          <Form.Group>
            <Form.Select 
              onChange={(e) => handleThemeChange(e.target.value)}
              defaultValue={localStorage.getItem('selectedTheme') || 'pastel-blue'}
            >
              <option value="pastel-blue">Pastel Blue</option>
              <option value="pastel-blue-1">Pastel Blue 1</option>
              <option value="pastel-blue-2">Pastel Blue 2</option>
              <option value="pastel-blue-3">Pastel Blue 3</option>
              <option value="pastel-blue-4">Pastel Blue 4</option>
              <option value="pastel-blue-5">Pastel Blue 5</option>
              <option value="pastel-blue-6">Pastel Blue 6</option>
              <option value="pastel-blue-7">Pastel Blue 7</option>
              <option value="pastel-blue-8">Pastel Blue 8</option>
              <option value="pastel-blue-9">Pastel Blue 9</option>
              <option value="pastel">Pastel</option>
              <option value="pastel-dark">Pastel Dark</option>
              <option value="pastel-pink">Pastel Pink</option>
              <option value="pastel-green">Pastel Green</option>
              <option value="pastel-purple">Pastel Purple</option>
              <option value="pastel-yellow">Pastel Yellow</option>
              <option value="pastel-orange">Pastel Orange</option>
              <option value="pastel-mint">Pastel Mint</option>
              <option value="pastel-lavender">Pastel Lavender</option>
              <option value="pastel-peach">Pastel Peach</option>
              <option value="pastel-teal">Pastel Teal</option>
            </Form.Select>
          </Form.Group>
        </Form>
      </Card.Body>
    </Card>
  );
};

export default ThemeSwitcher; 