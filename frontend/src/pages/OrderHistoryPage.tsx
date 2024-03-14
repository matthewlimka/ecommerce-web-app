import "../styles/OrderHistoryPage.css";
import Navbar from '../components/Navbar';
import { useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import Order from '../models/Order';
import OrderStatus from '../enums/OrderStatus';
import OrderCard from "../components/OrderCard";

const OrderHistoryPage: React.FC = () => {

    const { jwt } = useAuth();
    const { userOrders, getUserOrders } = useAPI();
    const upcomingOrders: Order[] = userOrders?.filter((order) => order!.orderStatus !== OrderStatus.DELIVERED) || [];
    const completedOrders: Order[] = userOrders?.filter((order) => order!.orderStatus === OrderStatus.DELIVERED) || [];

    useEffect(() => {
        getUserOrders(jwt!);
    }, [jwt]);

    return (
        <div className="page">
            <Navbar />
            <div className="order-history-page-content">
                <div className="order-history-page-upcoming-orders-section">
                    <h1 className="order-history-page-upcoming-orders-header">Upcoming Orders</h1>
                    {upcomingOrders?.length ? (
                        upcomingOrders.map((order) => (
                            <OrderCard key={order!.orderId} order={order!} />
                        ))
                    ) : (
                        <div className="order-history-page-upcoming-orders-empty-content">
                            <h2>No upcoming orders</h2>
                        </div>
                    )}
                </div>
                <div className="order-history-page-completed-orders-section">
                    <h1 className="order-history-page-completed-orders-header">Past Orders</h1>
                    {completedOrders?.length ? (
                        completedOrders.map((order) => (
                            <OrderCard key={order!.orderId} order={order!} />
                        ))
                    ) : (
                        <div className="order-history-page-completed-orders-empty-content">
                            <h2>No past orders</h2>
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}

export default OrderHistoryPage;