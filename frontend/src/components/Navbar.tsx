import "../styles/Navbar.css";
import { useEffect } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import Searchbar from "./Searchbar";

const Navbar: React.FC = () => {

    const location = useLocation();
    const { jwt, logout } = useAuth();
    const { cart, user, getUser, logoutUser } = useAPI();
    const cartRoute: string = jwt !== null ? `/cart/${user?.userId}` : "/login";
    const numOfCartItems: number = cart?.cartItems?.length ?? 0;
    const navigate = useNavigate();

    useEffect(() => {
        if (jwt !== null) {
            getUser(jwt);
        } else {
            logoutUser();
        }
    }, [jwt]);

    const getActiveClass = (path: string) => {
        return location.pathname === path ? "active" : "";
    };

    const handleLogout = () => {
        logout();
        navigate('/home');
    };

    return (
        <div className="nav-bar">
            <div className="nav-bar-left-container">
                <div className="nav-bar-button">
                    <Link to="/home" className={getActiveClass("/home")}>Home</Link>
                </div>
                <div className="nav-bar-button">
                    <Link to="/products" className={getActiveClass("/products")}>Browse</Link>
                </div>
                <Searchbar />
                <Link to={cartRoute} id="cart-icon" className={getActiveClass(`/cart/${user?.userId}`)}>
                    <img className="nav-bar-cart-icon" src="/cart-icon.svg" alt=""/>
                    {numOfCartItems > 0 && <p className="number-of-cart-items">{numOfCartItems}</p>}
                    </Link>
            </div>
            <div className="nav-bar-right-container">
                {jwt !== null ? (
                    <div className="nav-bar-logged-in-container">
                        <div className="nav-bar-account-container">
                            <div className="nav-bar-button">
                                <Link to={`/profile/${user?.userId}`}>Account</Link>
                            </div>
                            <div className="nav-bar-account-dropdown">
                                <div className="nav-bar-dropdown-button">
                                    <Link to={`/profile/${user?.userId}`}>Profile</Link>
                                </div>
                                <div className="nav-bar-dropdown-button">
                                    <Link to={`/orders/${user?.userId}`}>Orders</Link>
                                </div>
                                <div className="nav-bar-dropdown-button">
                                    <p onClick={handleLogout}>Logout</p>
                                </div>
                            </div>
                        </div>
                    </div>
                ) : (
                    <div className="nav-bar-not-logged-in-container">
                        <div className="nav-bar-button">
                            <Link to="/signup">Signup</Link>
                        </div>
                        <div className="nav-bar-button">
                            <Link to="/login">Login</Link>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Navbar;