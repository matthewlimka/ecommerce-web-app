import { useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import Address from "../models/Address";
import User from "../models/User";

interface TabProps {
    user: User;
}

interface UserForm {
    userId: number;
    username: string;
    email: string;
    firstName?: string;
    lastName?: string;
    registeredPaymentMethods: string[];
    shippingAddress?: Address;
}

const ProfileTab: React.FC<TabProps> = ({ user }) => {

    const { jwt } = useAuth();
    const { updateProfile } = useAPI();
    const [userFormData, setUserFormData] = useState<UserForm>({
        userId: user!.userId,
        username: user!.username,
        email: user!.email,
        firstName: user!.firstName,
        lastName: user!.lastName,
        shippingAddress: user!.shippingAddress,
        registeredPaymentMethods: user!.registeredPaymentMethods
    });

    const [updateSuccess, setUpdateSuccess] = useState(false);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setUserFormData({ ...userFormData, [name]: value });
    };

    const handleUpdateProfile = async (event: any) => {
        event.preventDefault();
        updateProfile(jwt!, userFormData.firstName!, userFormData.lastName!);
        setUpdateSuccess(true);
    };

    return (
        <div id="profile" className="account-page-main-section">
            <h1 id="profile" className="account-page-header">My Profile</h1>
            <form className="account-page-form" onSubmit={handleUpdateProfile}>
                <div id="username" className="account-page-line">
                    <h3 id="username" className="account-page-label">Username</h3>
                    <span id="username" className="account-page-value">{user?.username}</span>
                </div>
                <div id="email" className="account-page-line">
                    <h3 id="email" className="account-page-label">Email</h3>
                    <span id="email" className="account-page-value">{user?.email}</span>
                </div>
                <div id="first-name" className="account-page-line">
                    <h3 id="first-name" className="account-page-label">First Name</h3>
                    <input
                        type="text"
                        className="account-page-input"
                        name="firstName"
                        value={userFormData.firstName}
                        onChange={handleChange}
                        placeholder="First Name"
                        required
                    />
                </div>
                <div id="last-name" className="account-page-line">
                    <h3 id="last-name" className="account-page-label">Last Name</h3>
                    <input
                        type="text"
                        className="account-page-input"
                        name="lastName"
                        value={userFormData.lastName}
                        onChange={handleChange}
                        placeholder="Last Name"
                        required
                    />
                </div>
                {updateSuccess && <p className="account-page-success-message">Profile updated successfully</p>}
                <button className="account-page-button">Save</button>
            </form>
        </div>
    );
};

export default ProfileTab;