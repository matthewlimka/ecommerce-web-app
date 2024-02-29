import User from './User';

interface Address {
    addressId: number;
    street?: string;
    city?: string;
    state?: string;
    postalCode?: string;
    country?: string;
    user: User;
}

export default Address;