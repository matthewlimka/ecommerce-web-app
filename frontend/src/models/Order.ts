import OrderStatus from '../enums/OrderStatus';
import User from './User';
import OrderItem from './OrderItem';
import Payment from './Payment';

interface Order {
    orderId: number;
    orderDate: Date;
    totalAmount: number;
    orderStatus: OrderStatus;
    user: User;
    orderItems: OrderItem[];
    payment: Payment;
}

export default Order;