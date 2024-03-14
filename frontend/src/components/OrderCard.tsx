import "../styles/OrderCard.css";
import { useState } from "react";
import Order from "../models/Order";
import OrderStatus from "../enums/OrderStatus";
import FormatUtils from "../FormatUtils";

const OrderCard: React.FC<{ order: Order }> = ({ order }) => {

    const [expanded, setExpanded] = useState(false);

    const handleToggleExpand = () => {
        setExpanded(!expanded);
    }

    return (
        <div className={`order-card ${expanded ? 'expanded' : 'collapsed'}`} onClick={handleToggleExpand}>
            <div className="order-card-top-section">
                <div className="order-card-order-details">
                    <div className="order-card-detail-line">
                        <h3 className="order-card-detail-label">Order ID:</h3>
                        <span>{order?.orderId}</span>
                    </div>
                    <div className="order-card-detail-line">
                        <h3 className="order-card-detail-label">Order Date:</h3>
                        <span>{order?.orderDate.toDateString()}</span>
                    </div>
                    <div className="order-card-detail-line">
                        <h3 className="order-card-detail-label">Total:</h3>
                        <span>${order?.totalAmount.toFixed(2)}</span>
                    </div>
                </div>
                <div className="order-card-payment-details">
                    <div className="order-card-detail-line">
                        <h3 className="order-card-detail-label">Payment Method:</h3>
                        <span>{FormatUtils.formatPaymentMethod(order?.payment?.paymentMethod!)}</span>
                    </div>
                    <div className="order-card-detail-line">
                        <h3 className="order-card-detail-label">Payment ID:</h3>
                        <span>{order?.payment?.paymentId}</span>
                    </div>
                    <div className="order-card-detail-line">
                        <h3 className="order-card-detail-label">Transaction ID:</h3>
                        <span>{order?.payment?.transactionId}</span>
                    </div>
                </div>
            </div>
            <div className="order-card-status-section">
                {Object.values(OrderStatus).map(status => (
                    <div key={status} className={`order-card-status ${status === order.orderStatus ? 'current' : ''}`}>
                        <div className="order-card-status-circle"></div>
                        <span>{status}</span>
                    </div>
                ))}
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