import Navbar from "../components/Navbar";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import OrderItemCard from "../components/OrderItemCard";

const CheckoutPage: React.FC = () => {

    const { jwt } = useAuth();
    const { checkoutOrder, placeOrder, clearCart } = useAPI();
    const navigate = useNavigate();

    useEffect(() => {
        if (jwt == null) {
            navigate('/home');
        }
    }, [jwt]);


    const handlePlaceOrder = (event: any) => {
        event.preventDefault();
        placeOrder(jwt!);
        clearCart(jwt!);
        navigate('/payment');
    }

    return (
        <div className="page">
            <Navbar />
            <div className="checkout-page-content">
                <h1>Checkout</h1>
                <div className="checkout-items-section">
                    <h2>Products Ordered</h2>
                    <div className="cards-container">
                        <div className="checkout-item-cards">
                            {checkoutOrder?.orderItems.map((orderItem) => {
                                return (
                                    <OrderItemCard
                                        orderItemId={orderItem.orderItemId}
                                        quantity={orderItem.quantity}
                                        subtotal={orderItem.subtotal}
                                        orderId={orderItem.orderId}
                                        product={orderItem.product}
                                    />
                                );
                            })}
                        </div>
                    </div>
                </div>
                <div className="checkout-summary-section">
                    <h2>Summary</h2>
                    <h3>Total: ${checkoutOrder?.totalAmount}</h3>
                    <button onClick={handlePlaceOrder} className="cart-page-action-button">Place Order</button>
                </div>
            </div>
        </div>
    );
}

export default CheckoutPage;