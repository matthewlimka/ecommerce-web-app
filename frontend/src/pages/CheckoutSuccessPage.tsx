import "../styles/CheckoutSuccessPage.css"
import Navbar from "../components/Navbar";
import { useNavigate } from "react-router-dom";
import { useAPI } from "../contexts/APIContext";
import FormatUtils from "../FormatUtils";

const CheckoutSuccessPage: React.FC = () => {

    const { user, placedOrder } = useAPI();
    const navigate = useNavigate();

    return (
        <div className="page">
            <Navbar />
            <div className="checkout-success-page-content">
                <h1 className="checkout-success-page-header">Order Summary</h1>
                <div className="checkout-success-page-order-section">
                    <div className="checkout-success-page-top-section">
                        <div className="checkout-success-page-order-details-section">
                            <div className="checkout-success-page-detail-line">
                                <h3 className="checkout-success-page-order-id">Order ID: </h3><span>{placedOrder?.orderId}</span>
                            </div>
                            <div className="checkout-success-page-detail-line">
                                <h3 className="checkout-success-page-order-date">Order Date: </h3><span>{placedOrder?.orderDate.toDateString()}</span>
                            </div>
                            <div className="checkout-success-page-detail-line">
                                <h3 className="checkout-success-page-total">Total: </h3><span>${placedOrder?.totalAmount.toFixed(2)}</span>
                            </div>
                        </div>
                        <div className="checkout-success-page-payment-details-section">
                            <div className="checkout-success-page-detail-line">
                                <h3 className="checkout-success-page-payment-method">Payment Method: </h3><span>{FormatUtils.formatPaymentMethod(placedOrder?.payment?.paymentMethod!)}</span>
                            </div>
                            <div className="checkout-success-page-detail-line">
                                <h3 className="checkout-success-page-payment-id">Payment ID: </h3><span>{placedOrder?.payment?.paymentId}</span>
                            </div>
                            <div className="checkout-success-page-detail-line">
                                <h3 className="checkout-success-page-transaction-id">Transaction ID: </h3><span>{placedOrder?.payment?.transactionId}</span>
                            </div>
                        </div>
                    </div>
                    <div className="checkout-success-page-order-items-section">
                        <h2 className="checkout-success-page-order-items-header">Ordered Items</h2>
                        <div className="checkout-success-page-order-items-section-headers">
                            <p className="checkout-success-page-order-items-section-header-quantity">Quantity</p>
                            <p className="checkout-success-page-order-items-section-header-product-name">Product Name</p>
                            <p className="checkout-success-page-order-items-section-header-subtotal">Subtotal</p>
                        </div>
                        {placedOrder?.orderItems.map((orderItem) => (
                            <div key={orderItem.orderItemId} className="checkout-success-page-order-item">
                                <p className="checkout-success-page-order-item-quantity">{orderItem.quantity}x</p>
                                <p className="checkout-success-page-order-item-product-name">{orderItem.product.productName}</p>
                                <p className="checkout-success-page-order-item-subtotal">${orderItem.subtotal.toFixed(2)}</p>
                            </div>
                        ))}
                    </div>
                </div>
                <h2 className="checkout-success-page-message">Thank you for your purchase!<br/>Your order has been placed successfully.</h2>
                <div className="checkout-success-page-actions">
                    <button onClick={() => navigate(`/orders/${user?.userId}`)}>View Order</button>
                    <button onClick={() => navigate('/home')}>Continue Shopping</button>
                </div>
            </div>
        </div>
    );
}

export default CheckoutSuccessPage;