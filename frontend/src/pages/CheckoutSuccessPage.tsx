import Navbar from "../components/Navbar";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import { useEffect } from "react";

const CheckoutSuccessPage: React.FC = () => {

    const { jwt } = useAuth();
    const { placedOrder, getPlacedOrder } = useAPI();
    const navigate = useNavigate();

    useEffect(() => {
        getPlacedOrder(jwt!);
    }, [placedOrder]);

    return (
        <div className="page">
            <Navbar />
            <div className="checkout-success-page-content">
                <h1 className="checkout-success-page-header">Order Placed</h1>
                <p>Thank you for your purchase! Your order has been placed successfully.</p>
                <div className="checkout-success-page-summary">
                    <h2>Summary</h2>
                    <h3>Order ID: {placedOrder?.orderId}</h3>
                    <h3>Order Date: {placedOrder?.orderDate.toDateString()}</h3>
                    <h3>Total: ${placedOrder?.totalAmount.toFixed(2)}</h3>
                </div>
                <div className="checkout-sucesss-page-actions">
                    <button>View Order</button>
                    <button onClick={() => navigate('/home')}>Continue Shopping</button>
                </div>
            </div>
        </div>
    );
}

export default CheckoutSuccessPage;