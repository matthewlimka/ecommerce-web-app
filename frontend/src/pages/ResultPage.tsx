import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import Navbar from '../components/Navbar';

const ResultPage: React.FC = () => {

    const { jwt } = useAuth();
    const { getUser, queryResults, getQueryResults } = useAPI();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const query = queryParams.get('q');

    useEffect(() => {
        if (jwt !== null) {
            getUser(jwt)
        }
    }, [jwt])

    useEffect(() => {
        if (query !== null) {
            getQueryResults(query);
        } else {
            console.log('No query received');
        }
    }, [query])

    return (
        <div>
            <Navbar />
            <h1>X results for {query}</h1>
            <ul>
                {queryResults.map((result, index) => (
                    <li key={index}>{result}</li>
                ))}
            </ul>
        </div>
    )
}

export default ResultPage;