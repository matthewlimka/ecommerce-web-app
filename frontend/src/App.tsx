import React from 'react';
import './App.css';
import { Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { APIProvider } from './contexts/APIContext';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import OAuthCallbackPage from './pages/OAuthCallbackPage';
import ProductPage from './pages/ProductPage';
import OrderHistoryPage from './pages/OrderHistoryPage';
import ResultPage from './pages/ResultPage';
import CartPage from './pages/CartPage';
import ProfilePage from './pages/ProfilePage';
import CheckoutPage from './pages/CheckoutPage';
import CheckoutSuccessPage from './pages/CheckoutSuccessPage';

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
            <Route path="/product/:productId" element={<ProductPage />} />
            <Route path="/orders/:userId" element={<OrderHistoryPage />} />
            <Route path="/result" element={<ResultPage />} />
            <Route path="/cart/:userId" element={<CartPage />} />
            <Route path="/profile/:userId" element={<ProfilePage />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/checkout/success" element={<CheckoutSuccessPage />} />
          </Routes>
        </div>
      </APIProvider>
    </AuthProvider>
  );
}

export default App;