import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import Searchbar from './Searchbar';

const Navbar: React.FC = () => {

    const navigate = useNavigate();
    const { jwt, logout } = useAuth();

    const handleLogin = () => {
        navigate('/login')
    }

    const handleLogout = () => {
        logout()
    }

    return (
        <div className="navBar">
            <div className="topRow">
                <div className="loginLogoutButton">
                    {jwt !== null ? (
                        <button onClick={handleLogin}>Logout</button>
                    ) : (
                        <button onClick={handleLogin}>Login</button>
                    )
                    }
                </div>
            </div>
            <div className="bottomRow">
                <div className="homeButton">
                    <button onClick={() => navigate('/home')}>Home</button>
                </div>
                <div className="searchBar">
                    <Searchbar />
                </div>
                <div className="cartButton">
                    <button onClick={() => navigate('/about')}>About</button>
                </div>
            </div>
        </div>
    )
}

export default Navbar;