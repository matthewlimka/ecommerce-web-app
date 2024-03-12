import User from './User';

interface Address {
    addressId: number;
    streetAddress?: string;
    city?: string;
    state?: string;
    postalCode?: string;
    country?: string;
    user: User;
}

export default Address;