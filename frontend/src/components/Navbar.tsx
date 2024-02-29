import '../styles/Navbar.css';
import { useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import Searchbar from './Searchbar';

const Navbar: React.FC = () => {
    
    const location = useLocation();
    const { jwt, logout } = useAuth();
    const { user, getUser, logoutUser } = useAPI();

    useEffect(() => {
        if (jwt !== null && user === null) {
            getUser(jwt)
        } else {
            logoutUser()
        }
    }, [jwt])

    const getActiveClass = (path: string) => {
        return location.pathname === path ? 'active' : '';
    };

    const handleLogout = () => {
        logout();
    };

    return (
        <div className='navBar'>
            <div className='leftContainer'>
                <div className='homeContainer'>
                    <Link to='/home' className={getActiveClass('/home')}>
                        Home
                    </Link>
                </div>
                <div className='productContainer'>
                    <Link
                        to='/products'
                        className={getActiveClass('/products')}
                    >
                        Products
                    </Link>
                </div>
                <div className='orderContainer'>
                    <Link to='/orders' className={getActiveClass('/orders')}>
                        Orders
                    </Link>
                </div>
                <Searchbar />
            </div>
            <div className='rightContainer'>
                {jwt !== null ? (
                    <div className='loggedInContainer'>
                        <div className='cartButton'>
                            <Link to='/cart'>Cart</Link>
                        </div>
                        <div className='userProfile'>{user?.username}</div>
                        <div className='profileButton'>
                            <Link to='/profile'>Profile</Link>
                        </div>
                        <div className='logoutButton'>
                            <p onClick={handleLogout}>Logout</p>
                        </div>
                    </div>
                ) : (
                    <div className='notLoggedInContainer'>
                        <div className='signupButton'>
                            <Link to='/signup'>Signup</Link>
                        </div>
                        <div className='loginButton'>
                            <Link to='/login'>Login</Link>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Navbar;
