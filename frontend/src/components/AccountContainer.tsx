import { useEffect, useState } from 'react';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import AccountPage from '../pages/AccountPage';

const AccountContainer: React.FC = () => {

    const [selectedTab, setSelectedTab] = useState<string>('profile');
    const { userId } = useParams();
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const pathname = location.pathname;
        const pathParts = pathname.split('/');
        const tab = pathParts[pathParts.length - 2];
        setSelectedTab(tab);
    }, [location.pathname]);

    const handleTabClick = (tab: string) => {
        setSelectedTab(tab);
        navigate(`/account/${tab}/${userId}`);
    };

    return (
        <AccountPage selectedTab={selectedTab} onTabClick={handleTabClick} />
    )
}

export default AccountContainer;