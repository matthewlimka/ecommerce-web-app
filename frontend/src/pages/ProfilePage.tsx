import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import Navbar from '../components/Navbar';

const ProfilePage: React.FC = () => {
    return (
        <div>
            <Navbar />
            <h1>Profile Page</h1>
        </div>
    )
}

export default ProfilePage;