import Navbar from "../components/Navbar"
import { useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { useAuth } from "../contexts/AuthContext"
import { useAPI } from "../contexts/APIContext"
import OrderItemCard from "../components/OrderItemCard"

const PaymentPage: React.FC = () => {

    const { jwt } = useAuth();
    const { checkoutOrder } = useAPI();
    const navigate = useNavigate();

    useEffect(() => {
        if (jwt == null) {
            navigate("/home")
        }
    }, [jwt]);

    const handlePayment = (event: any) => {
        event.preventDefault();
        console.log('Making payment...');
    }

    return (
        <div className="page">
            <Navbar />
            <div className="payment-page-content">
                <h1>Payment</h1>
                <div className="payment-items-section">
                    <h2>Products Ordered</h2>
                    <div className="cards-container">
                        <div className="payment-item-cards">
                            {checkoutOrder?.orderItems.map((orderItem, index) => {
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
                <div className="payment-summary-section">
                    <h2>Summary</h2>
                    <h3>Total: ${checkoutOrder?.totalAmount}</h3>
                    <button onClick={handlePayment} className="cart-page-action-button">Pay</button>
                </div>
            </div>
        </div>
    )
}

export default PaymentPage;