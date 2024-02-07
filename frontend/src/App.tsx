import React from 'react';
import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import './App.css';
import { AuthProvider } from './contexts/AuthContext';
import OAuthCallbackPage from './pages/OAuthCallbackPage';

function App() {
  return (
    <AuthProvider>
      <div className="App">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/oauth/callback" element={<OAuthCallbackPage />} />
        </Routes>
      </div>
    </AuthProvider>
  );
}

export default App;
