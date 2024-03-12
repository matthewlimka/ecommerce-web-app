import "../styles/CheckoutPage.css";
import Navbar from "../components/Navbar";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import OrderItemCard from "../components/OrderItemCard";
import PaymentMethod from "../enums/PaymentMethod";

const CheckoutPage: React.FC = () => {

    const { jwt } = useAuth();
    const { user, checkoutOrder, placeOrder, clearCart } = useAPI();
    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState<PaymentMethod>();
    const shippingAddress: string = user?.shippingAddress?.streetAddress !== null ? `${user?.shippingAddress?.streetAddress}, Singapore ${user?.shippingAddress?.postalCode}` : 'No shipping address';
    const navigate = useNavigate();

    useEffect(() => {
        if (jwt == null) {
            navigate('/home');
        }
    }, [jwt]);

    const handlePlaceOrder = (event: any) => {
        event.preventDefault();
        placeOrder(jwt!, selectedPaymentMethod!);
        clearCart(jwt!);
        navigate('/checkout/success');
    }

    return (
        <div className="page">
            <Navbar />
            <div className="checkout-page-content">
                <h1 className="checkout-page-header">Checkout</h1>
                <div className="checkout-page-shipping-section">
                    <h2>Shipping Address</h2>
                    <p>{shippingAddress.toUpperCase()}</p>
                </div>
                <div className="checkout-page-items-section">
                    <h2 className="checkout-items-section-header">Products Ordered</h2>
                    {checkoutOrder?.orderItems.map((orderItem) => (
                        <OrderItemCard
                            orderItemId={orderItem.orderItemId}
                            quantity={orderItem.quantity}
                            subtotal={orderItem.subtotal}
                            orderId={orderItem.orderId}
                            product={orderItem.product}
                        />
                    ))}
                </div>
                <div className="checkout-page-payment-section">
                    <h2>Payment Method</h2>
                    <div className="checkout-page-payment-options">
                        <button
                            className={`checkout-page-payment-option ${selectedPaymentMethod === PaymentMethod.PAYNOW ? 'selected' : ''}`}
                            onClick={() => setSelectedPaymentMethod(PaymentMethod.PAYNOW)}
                        >
                            PayNow
                        </button>
                        <button
                            className={`checkout-page-payment-option ${selectedPaymentMethod === PaymentMethod.CREDIT_CARD_MASTERCARD ? 'selected' : ''}`}
                            onClick={() => setSelectedPaymentMethod(PaymentMethod.CREDIT_CARD_MASTERCARD)}
                        >
                            Mastercard
                        </button>
                        <button
                            className={`checkout-page-payment-option ${selectedPaymentMethod === PaymentMethod.CREDIT_CARD_VISA ? 'selected' : ''}`}
                            onClick={() => setSelectedPaymentMethod(PaymentMethod.CREDIT_CARD_VISA)}
                        >
                            Visa
                        </button>
                    </div>
                </div>
                <div className="checkout-page-summary-section">
                    <h2>Summary</h2>
                    <h3>Total ({checkoutOrder?.orderItems.length} item{checkoutOrder?.orderItems.length === 1 ? '' : 's'}): ${checkoutOrder?.totalAmount}</h3>
                    <button onClick={handlePlaceOrder} className="cart-page-action-button">Place Order</button>
                </div>
            </div>
        </div>
    );
}

export default CheckoutPage;