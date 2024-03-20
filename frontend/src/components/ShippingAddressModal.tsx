import '../styles/ShippingAddressModal.css';
import { useRef, useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import Address from '../models/Address';

interface ModalProps {
    showModal: boolean;
    closeModal: () => void;
    shippingAddress: Address;
}

const ShippingAddressModal: React.FC<ModalProps> = ({ showModal, closeModal, shippingAddress }) => {
    
    const dialogRef = useRef<HTMLDialogElement>(null);
    const { jwt } = useAuth();
    const { user, updateShippingAddress } = useAPI();
    const [formData, setFormData] = useState<Address>({
        addressId: shippingAddress.addressId,
        streetAddress: shippingAddress.streetAddress ?? '',
        city: shippingAddress.city ?? '',
        state: shippingAddress.state ?? '',
        postalCode: shippingAddress.postalCode ?? '',
        country: shippingAddress.country ?? '',
        user: user!
    });
    const [updateSuccess, setUpdateSuccess] = useState(false);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleUpdateShippingAddress = (event: any) => {
        event.preventDefault();
        updateShippingAddress(jwt!, formData);
        setUpdateSuccess(true);
    }

    const handleCloseModal = (event: any) => {
        event.preventDefault();
        closeModal();
        setUpdateSuccess(false);
    }

    return (
        <dialog className="shipping-address-modal" ref={dialogRef} open={showModal}>
            <p className="shipping-address-modal-close-button" onClick={handleCloseModal}>X</p>
            {updateSuccess ? (
                <div className="shipping-address-modal-after-update">
                    <h2 className="shipping-address-modal-header">Success!</h2>
                    <p className="shipping-address-modal-success-message">Shipping address has been updated</p>
                </div>
            ) : (
                <div className="shipping-address-modal-before-update">
                    <h2 className="shipping-address-modal-header">Update Shipping Address</h2>
                    <form className="shipping-address-modal-form" onSubmit={handleUpdateShippingAddress}>
                        <input
                            type="text"
                            className="shipping-address-modal-street-address"
                            name="streetAddress"
                            value={formData.streetAddress}
                            onChange={handleChange}
                            placeholder="Street Address"
                            required
                        />
                        <input
                            type="text"
                            className="shipping-address-modal-city"
                            name="city"
                            value={formData.city}
                            onChange={handleChange}
                            placeholder="City"
                            required
                        />
                        <input
                            type="text"
                            className="shipping-address-modal-state"
                            name="state"
                            value={formData.state}
                            onChange={handleChange}
                            placeholder="State"
                            required
                        />
                        <input
                            type="text"
                            className="shipping-address-modal-postal-code"
                            name="postalCode"
                            value={formData.postalCode}
                            onChange={handleChange}
                            placeholder="Postal Code"
                            required
                        />
                        <input
                            type="text"
                            className="shipping-address-modal-country"
                            name="country"
                            value={formData.country}
                            onChange={handleChange}
                            placeholder="Country"
                            required
                        />
                        <button className="shipping-address-modal-submit-button" type="submit">Update</button>
                    </form>
                </div>
            )}
        </dialog>
    );
};

export default ShippingAddressModal;