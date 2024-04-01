import '../styles/ProfilePage.css';
import { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import Navbar from '../components/Navbar';
import Address from '../models/Address';

const ProfilePage: React.FC = () => {

    const { jwt } = useAuth();
    const { user, getUser, updateShippingAddress } = useAPI();
    const [selectedTab, setSelectedTab] = useState('profile');
    const [addressFormData, setAddressFormData] = useState<Address>({
        addressId: user!.shippingAddress!.addressId,
        streetAddress: user!.shippingAddress!.streetAddress ?? '',
        city: user!.shippingAddress!.city ?? '',
        state: user!.shippingAddress!.state ?? '',
        postalCode: user!.shippingAddress!.postalCode ?? '',
        country: user!.shippingAddress!.country ?? '',
        user: user!
    });
    const shippingAddress: string = user?.shippingAddress?.streetAddress !== null ? `${user?.shippingAddress?.streetAddress}, Singapore ${user?.shippingAddress?.postalCode}` : 'No shipping address';

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        if (selectedTab === 'address') {
            setAddressFormData({ ...addressFormData, [name]: value });
        } else if (selectedTab === 'profile') {
            // setProfileFormData({ ...profileFormData, [name]: value });
        }
    };

    const handleUpdateProfile = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        // const formData = new FormData(event.currentTarget);
        // const firstName = formData.get('firstName') as string;
        // const lastName = formData.get('lastName') as string;
        // await getUser(jwt!);
    }
    
    const handleUpdatePaymentMethods = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        // const formData = new FormData(event.currentTarget);
        // const cardNumber = formData.get('cardNumber') as string;
        // const expiryDate = formData.get('expiryDate') as string;
        // await getUser(jwt!);
    }

    const handleUpdateShippingAddress = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        await updateShippingAddress(jwt!, addressFormData);
    }

    return (
        <div className="page">
            <Navbar />
            <div className="profile-page-content">
                <div className="profile-page-sidebar">
                    <p id="profile" className="profile-page-sidebar-tab" onClick={() => setSelectedTab('profile')}>Profile</p>
                    <p id="payment-methods" className="profile-page-sidebar-tab" onClick={() => setSelectedTab('payment-methods')}>Payment Methods</p>
                    <p id="address" className="profile-page-sidebar-tab" onClick={() => setSelectedTab('address')}>Address</p>
                </div>
                <div className="profile-page-main">
                    {selectedTab === 'profile' && (
                        <div className="profile-page-user-info">
                            <h1 id="profile" className="profile-page-header">My Profile</h1>
                            <form className="profile-page-form">
                                <div id="username" className="profile-page-line">
                                    <h3 id="username" className="profile-page-label">Username</h3>
                                    <span id="username" className="profile-page-value">{user?.username}</span>
                                </div>
                                <div id="email" className="profile-page-line">
                                    <h3 id="email" className="profile-page-label">Email</h3>
                                    <span id="email" className="profile-page-value">{user?.email}</span>
                                </div>
                                <div id="first-name" className="profile-page-line">
                                    <h3 id="first-name" className="profile-page-label">First Name</h3>
                                    <span id="first-name" className="profile-page-value">{user?.firstName}</span>
                                </div>
                                <div id="last-name" className="profile-page-line">
                                    <h3 id="last-name" className="profile-page-label">Last Name</h3>
                                    <span id="last-name" className="profile-page-value">{user?.lastName}</span>
                                </div>
                                <div id="address" className="profile-page-line">
                                    <h3 id="address" className="profile-page-label">Address</h3>
                                    <span id="address" className="profile-page-value">{shippingAddress}</span>
                                </div>
                                <button className="profile-page-button">Save</button>
                            </form>
                        </div>
                    )}
                    {selectedTab === 'payment-methods' && (
                        <div className="profile-page-payment-methods">
                            <h1 id="payment-methods" className="profile-page-header">My Payment Methods</h1>
                            {user?.registeredPaymentMethods.map((paymentMethod, index) => (
                                <div key={index} id="payment-methods" className="profile-page-line">
                                    <h3 className="profile-page-label">Type</h3>
                                    <span className="profile-page-value">{paymentMethod.toString()}</span>
                                </div>
                            ))}
                        </div>
                    )}
                    {selectedTab === 'address' && (
                        <div className="profile-page-address">
                            <h1 id="address" className="profile-page-header">My Address</h1>
                            <form className="profile-page-form">
                                <div className="profile-page-line">
                                    <h3 className="profile-page-label">Address</h3>
                                    <input
                                        type="text"
                                        className="profile-page-input"
                                        name="streetAddress"
                                        value={addressFormData.streetAddress}
                                        onChange={handleChange}
                                        placeholder="Street Address"
                                        required
                                    />
                                </div>
                                <div className="profile-page-line">
                                    <h3 className="profile-page-label">City</h3>
                                    <input
                                        type="text"
                                        className="profile-page-input"
                                        name="city"
                                        value={addressFormData.city}
                                        onChange={handleChange}
                                        placeholder="City"
                                        required
                                    />
                                </div>
                                <div className="profile-page-line">
                                    <h3 className="profile-page-label">State</h3>
                                    <input
                                        type="text"
                                        className="profile-page-input"
                                        name="state"
                                        value={addressFormData.state}
                                        onChange={handleChange}
                                        placeholder="State"
                                        required
                                    />
                                </div>
                                <div className="profile-page-line">
                                    <h3 className="profile-page-label">Postal Code</h3>
                                    <input
                                        type="text"
                                        className="profile-page-input"
                                        name="postalCode"
                                        value={addressFormData.postalCode}
                                        onChange={handleChange}
                                        placeholder="Postal Code"
                                        required
                                    />
                                </div>
                                <div className="profile-page-line">
                                    <h3 className="profile-page-label">Country</h3>
                                    <input
                                        type="text"
                                        className="profile-page-input"
                                        name="country"
                                        value={addressFormData.country}
                                        onChange={handleChange}
                                        placeholder="Country"
                                        required
                                    />
                                </div>
                                <button className="profile-page-button">Save</button>
                            </form>
                    </div>
                    )}
                </div>
            </div>
        </div>
    )
}

export default ProfilePage;