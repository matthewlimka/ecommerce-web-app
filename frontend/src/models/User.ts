import Role from '../enums/Role';
import PaymentMethod from '../enums/PaymentMethod';
import Order from './Order';
import Address from './Address';
import Cart from './Cart';

interface User {
    userId: number;
    username: string;
    password: string;
    email: string;
    firstName?: string;
    lastName?: string;
    role: Role;
    registeredPaymentMethods: PaymentMethod[];
    orders: Order[];
    shippingAddress?: Address;
    cart: Cart;
}

export default User;