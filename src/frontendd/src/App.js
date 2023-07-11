import * as React from 'react'
import './App.css';
import Payments from './Payments';
import { Container, CssBaseline, Typography } from '@mui/material';

function App() {
  return (
    <>
      <CssBaseline />
      <Container>
        <Typography variant="h2" gutterBottom>
          Spring Integration - Mail
        </Typography>

        <Typography variant="subtitle1" gutterBottom>
          Payments received
        </Typography>

        <hr />

        <Payments />
      </Container>
    </>
  ) 
  ;
}

export default App;
