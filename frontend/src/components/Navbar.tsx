import "../styles/Navbar.css";
import { useEffect } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import Searchbar from "./Searchbar";

const Navbar: React.FC = () => {

    const location = useLocation();
    const { jwt, logout } = useAuth();
    const { user, getUser, logoutUser } = useAPI();
    const cartRoute: string = jwt !== null ? `/cart/${user?.userId}` : "/login";
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
        <div className="navBar">
            <div className="leftContainer">
                <div className="homeContainer">
                    <Link to="/home" className={getActiveClass("/home")}>Home</Link>
                </div>
                <div className="productContainer">
                    <Link to="/products" className={getActiveClass("/products")}>Browse</Link>
                </div>
                <Searchbar />
                <div className="cartContainer">
                    <Link to={cartRoute} className={getActiveClass(`/cart/${user?.userId}`)}>Cart</Link>
                </div>
            </div>
            <div className="rightContainer">
                {jwt !== null ? (
                    <div className="loggedInContainer">
                        <div className="accountContainer">
                            <p className="accountButton">Account</p>
                            <div className="accountDropdown">
                                <div className="profileButton">
                                    <Link to={`/profile/${user?.userId}`}>Profile</Link>
                                </div>
                                <div className="ordersButton">
                                    <Link to={`/orders/${user?.userId}`}>Orders</Link>
                                </div>
                                <div className="logoutButton">
                                    <p onClick={handleLogout}>Logout</p>
                                </div>
                            </div>
                        </div>
                    </div>
                ) : (
                    <div className="notLoggedInContainer">
                        <div className="signupButton">
                            <Link to="/signup">Signup</Link>
                        </div>
                        <div className="loginButton">
                            <Link to="/login">Login</Link>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Navbar;