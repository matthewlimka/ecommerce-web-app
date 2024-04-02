import { useState } from "react";
import Address from "../models/Address";
import User from "../models/User";
import ShippingAddressModal from "./ShippingAddressModal";

interface TabProps {
    user: User;
}

const AddressTab: React.FC<TabProps> = ({ user }) => {
    
    const [showModal, setShowModal] = useState(false);
    const shippingAddress: Address = user!.shippingAddress!;

    const closeModal = () => {
        setShowModal(false);
    };

    return (
        <div id="address" className="account-page-main-section">
            <div id="address" className="account-page-header-section">
                <h1 id="address" className="account-page-header">My Address</h1>
                <button id="address" className="account-page-header-button" onClick={() => setShowModal(true)}>Change</button>
            </div>
            {showModal && <div className="account-page-modal-backdrop" />}
            <ShippingAddressModal showModal={showModal} closeModal={closeModal} shippingAddress={shippingAddress!} />
            <div id="address" className="account-page-line">
                <h3 className="account-page-label">Address</h3>
                <span className="account-page-value">{shippingAddress!.streetAddress ? shippingAddress!.streetAddress?.toString() : 'NA'}</span>
            </div>
            <div id="address" className="account-page-line">
                <h3 className="account-page-label">City</h3>
                <span className="account-page-value">{shippingAddress!.city ? shippingAddress!.city?.toString() : 'NA'}</span>
            </div>
            <div id="address" className="account-page-line">
                <h3 className="account-page-label">State</h3>
                <span className="account-page-value">{shippingAddress!.state ? shippingAddress!.state?.toString() : 'NA'}</span>
            </div>
            <div id="address" className="account-page-line">
                <h3 className="account-page-label">Postal Code</h3>
                <span className="account-page-value">{shippingAddress!.postalCode ? shippingAddress!.postalCode?.toString() : 'NA'}</span>
            </div>
            <div id="address" className="account-page-line">
                <h3 className="account-page-label">Country</h3>
                <span className="account-page-value">{shippingAddress!.country ? shippingAddress!.country?.toString() : 'NA'}</span>
            </div>
        </div>
    );
};

export default AddressTab;