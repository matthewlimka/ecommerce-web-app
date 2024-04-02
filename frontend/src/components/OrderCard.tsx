import "../styles/OrderCard.css";
import { useState } from "react";
import Order from "../models/Order";
import FormatUtils from "../FormatUtils";

const OrderCard: React.FC<{ order: Order }> = ({ order }) => {

    const [expanded, setExpanded] = useState(false);

    const handleToggleExpand = () => {
        setExpanded(!expanded);
    }

    return (
        <div className={`order-card ${expanded ? 'expanded' : 'collapsed'}`} onClick={handleToggleExpand}>
            <div className="order-card-order-details">
                <div className="order-card-order-details-header-section">
                    <h3 id="order" className="order-card-order-details-header">Order</h3>
                    <h3 id="order-date" className="order-card-order-details-header">Date Ordered</h3>
                    <h3 id="payment-method" className="order-card-order-details-header">Payment Method</h3>
                    <h3 id="delivery-status" className="order-card-order-details-header">Delivery Status</h3>
                    <h3 id="total" className="order-card-order-details-header">Total</h3>
                </div>
                <div className="order-card-order-details-value-section">
                    <span id="order" className="order-card-order-details-value">{order?.orderId}</span>
                    <span id="order-date" className="order-card-order-details-value">{order?.orderDate.toDateString()}</span>
                    <span id="payment-method" className="order-card-order-details-value">{FormatUtils.formatPaymentMethod(order?.payment?.paymentMethod!)}</span>
                    <span id="delivery-status" className="order-card-order-details-value">{order?.orderStatus}</span>                        
                    <span id="total" className="order-card-order-details-value">${order?.totalAmount.toFixed(2)}</span>
                </div>
            </div>
            {expanded && (
                <div className="order-card-order-items">
                    <h2 className="order-card-order-items-header">Ordered Items</h2>
                    <div className="order-card-order-items-section-headers">
                        <p className="order-card-order-items-section-header-quantity">Quantity</p>
                        <p className="order-card-order-items-section-header-product-name">Product Name</p>
                        <p className="order-card-order-items-section-header-subtotal">Subtotal</p>
                    </div>
                    {order.orderItems.map(orderItem => (
                        <div key={orderItem.orderItemId} className="order-card-order-item">
                            <p className="order-card-order-item-quantity">{orderItem.quantity}x</p>
                            <p className="order-card-order-item-product-name">{orderItem.product.productName}</p>
                            <p className="order-card-order-item-subtotal">${orderItem.subtotal.toFixed(2)}</p>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default OrderCard;