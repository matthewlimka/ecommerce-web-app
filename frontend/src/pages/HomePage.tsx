import { useAuth } from '../contexts/AuthContext';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const HomePage: React.FC = () => {

    const { jwt, login } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (jwt === null) {
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }
    }, [jwt])

    return (
        <div>
            <h1>Welcome to the home page!</h1>
            <p>Your JWT is {jwt}</p>
        </div>
    )
}

export default HomePage;