import Navbar from '../components/Navbar';
import { useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';

const HomePage: React.FC = () => {

    const { jwt } = useAuth();
    const { user, getUser } = useAPI();

    useEffect(() => {
        if (jwt !== null) {
            getUser(jwt)
        }
    }, [jwt])

    return (
        <div>
            <Navbar />
            <h1>Welcome to ShoppersGate {user?.username}!</h1>
        </div>
    )
}

export default HomePage;