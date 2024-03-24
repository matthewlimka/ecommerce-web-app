import '../styles/ProfilePage.css';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import Navbar from '../components/Navbar';

const ProfilePage: React.FC = () => {

    const { jwt } = useAuth();
    const { user, getUser } = useAPI();
    const shippingAddress: string = user?.shippingAddress?.streetAddress !== null ? `${user?.shippingAddress?.streetAddress}, Singapore ${user?.shippingAddress?.postalCode}` : 'No shipping address';

    return (
        <div className="page">
            <Navbar />
            <div className="profile-page-content">
                <h1 className="profile-page-header">My Profile</h1>
                <div className="profile-page-user-info">
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
                </div>
            </div>
        </div>
    )
}

export default ProfilePage;