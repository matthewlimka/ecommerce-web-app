import Role from '../enums/Role';
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
    orders: Order[];
    address?: Address;
    cart: Cart;
}

export default User;