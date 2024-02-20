import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import Navbar from '../components/Navbar';

const ResultPage: React.FC = () => {

    const { jwt } = useAuth();
    const { queryResults, getQueryResults } = useAPI();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const query = queryParams.get('q');

    useEffect(() => {
        getQueryResults(query);
    }, [])

    return (
        <div>
            <Navbar />
            <h1>Result Page</h1>
            <p>You searched for: {query}</p>
            <ul>
                {queryResults.map((result, index) => (
                    <li key={index}>{result}</li>
                ))}
            </ul>
        </div>
    )
}

export default ResultPage;