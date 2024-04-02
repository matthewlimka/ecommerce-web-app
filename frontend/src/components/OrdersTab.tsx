import Order from '../models/Order';
import OrderStatus from '../enums/OrderStatus';
import OrderCard from "../components/OrderCard";

interface TabProps {
    userOrders: Order[];
}

const OrdersTab: React.FC<TabProps> = ({ userOrders }) => {
    
    const upcomingOrders: Order[] = userOrders?.filter((order) => order!.orderStatus !== OrderStatus.DELIVERED) || [];
    const completedOrders: Order[] = userOrders?.filter((order) => order!.orderStatus === OrderStatus.DELIVERED) || [];

    return (
        <div id="orders" className="account-page-main-section">
            <div className="account-page-upcoming-orders-section">
                <h1 className="account-page-upcoming-orders-header">Upcoming Orders</h1>
                {upcomingOrders?.length ? (
                    upcomingOrders.map((order) => (
                        <OrderCard key={order!.orderId} order={order!} />
                    ))
                ) : (
                    <div className="account-page-upcoming-orders-empty-content">
                        <h2>No upcoming orders</h2>
                    </div>
                )}
            </div>
            <div className="account-page-completed-orders-section">
                <h1 className="account-page-completed-orders-header">Past Orders</h1>
                {completedOrders?.length ? (
                    completedOrders.map((order) => (
                        <OrderCard key={order!.orderId} order={order!} />
                    ))
                ) : (
                    <div className="account-page-completed-orders-empty-content">
                        <h2>No past orders</h2>
                    </div>
                )}
            </div>
        </div>
    )
};

export default OrdersTab;