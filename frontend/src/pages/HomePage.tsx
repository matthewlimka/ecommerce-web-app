import Navbar from '../components/Navbar';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';

const HomePage: React.FC = () => {

    const { jwt } = useAuth();
    const { user, getUser } = useAPI();
    const navigate = useNavigate();

    useEffect(() => {
        if (jwt === null) {
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        } else {
            getUser(jwt)
        }
    }, [])

    return (
        <div>
            <Navbar />
            <h1>Home Page</h1>
            <p>Welcome to ShoppersGate {user}!</p>
        </div>
    )
}

export default HomePage;