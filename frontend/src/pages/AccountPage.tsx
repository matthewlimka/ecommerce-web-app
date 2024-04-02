import '../styles/AccountPage.css';
import { useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import Navbar from '../components/Navbar';
import ProfileTab from '../components/ProfileTab';
import PaymentMethodsTab from '../components/PaymentMethodsTab';
import AddressTab from '../components/AddressTab';
import OrdersTab from '../components/OrdersTab';

interface AccountPageProps {
    selectedTab: string;
    onTabClick: (tab: string) => void;
}

const AccountPage: React.FC<AccountPageProps> = ({ selectedTab, onTabClick }) => {

    const { jwt } = useAuth();
    const { user, userOrders, getUser, getUserOrders } = useAPI();

    useEffect(() => {   
        if (jwt == null) {
            return;
        }
        getUser(jwt!);
    }, [jwt]);

    useEffect(() => {
        getUserOrders(jwt!);
    }, [selectedTab]);

    const handleUpdatePaymentMethods = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        // const formData = new FormData(event.currentTarget);
        // const cardNumber = formData.get('cardNumber') as string;
        // const expiryDate = formData.get('expiryDate') as string;
        // await getUser(jwt!);
    };

    return (
        <div className="page">
            <Navbar />
            <div className="account-page-content">
                <div className="account-page-sidebar">
                    <p id="profile" className={`account-page-sidebar-tab ${selectedTab === 'profile' ? 'selected' : ''}`} onClick={() => onTabClick('profile')}>Profile</p>
                    <p id="payment" className={`account-page-sidebar-tab ${selectedTab === 'payment' ? 'selected' : ''}`} onClick={() => onTabClick('payment')}>Payment Methods</p>
                    <p id="address" className={`account-page-sidebar-tab ${selectedTab === 'address' ? 'selected' : ''}`} onClick={() => onTabClick('address')}>Address</p>
                    <p id="orders" className={`account-page-sidebar-tab ${selectedTab === 'orders' ? 'selected' : ''}`} onClick={() => onTabClick('orders')}>Orders</p>
                </div>
                <div className="account-page-main">
                    {selectedTab === 'profile' && <ProfileTab user={user!} />}
                    {selectedTab === 'payment' && <PaymentMethodsTab user={user!} />}
                    {selectedTab === 'address' && <AddressTab user={user!} />}
                    {selectedTab === 'orders' && <OrdersTab userOrders={userOrders} />}
                </div>
            </div>
        </div>
    )
};

export default AccountPage;