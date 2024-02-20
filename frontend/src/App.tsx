import React from 'react';
import './App.css';
import { Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { APIProvider } from './contexts/APIContext';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import OAuthCallbackPage from './pages/OAuthCallbackPage';
import ResultPage from './pages/ResultPage';

function App() {
  return (
    <AuthProvider>
      <APIProvider>
        <div className="App">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/home" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/oauth/callback" element={<OAuthCallbackPage />} />
            <Route path="/result" element={<ResultPage />} />
          </Routes>
        </div>
      </APIProvider>
    </AuthProvider>
  );
}

export default App;