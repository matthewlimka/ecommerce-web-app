import '../styles/UpdateModal.css';
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
        <dialog id="shipping-address" className="modal" ref={dialogRef} open={showModal}>
            <p id="shipping-address" className="modal-close-button" onClick={handleCloseModal}>X</p>
            {updateSuccess ? (
                <div id="shipping-address" className="modal-after-update">
                    <h2 id="shipping-address" className="modal-header">Success!</h2>
                    <p id="shipping-address" className="modal-success-message">Shipping address has been updated</p>
                </div>
            ) : (
                <div id="shipping-address" className="modal-before-update">
                    <h2 id="shipping-address" className="modal-header">Update Shipping Address</h2>
                    <form id="shipping-address" className="modal-form" onSubmit={handleUpdateShippingAddress}>
                        <input
                            type="text"
                            id="shipping-address"
                            className="modal-form-input"
                            name="streetAddress"
                            value={formData.streetAddress}
                            onChange={handleChange}
                            placeholder="Street Address"
                            required
                        />
                        <input
                            type="text"
                            id="shipping-address"
                            className="modal-form-input"
                            name="city"
                            value={formData.city}
                            onChange={handleChange}
                            placeholder="City"
                            required
                        />
                        <input
                            type="text"
                            id="shipping-address"
                            className="modal-form-input"
                            name="state"
                            value={formData.state}
                            onChange={handleChange}
                            placeholder="State"
                            required
                        />
                        <input
                            type="text"
                            id="shipping-address"
                            className="modal-form-input"
                            name="postalCode"
                            value={formData.postalCode}
                            onChange={handleChange}
                            placeholder="Postal Code"
                            required
                        />
                        <input
                            type="text"
                            id="shipping-address"
                            className="modal-form-input"
                            name="country"
                            value={formData.country}
                            onChange={handleChange}
                            placeholder="Country"
                            required
                        />
                        <button id="shipping-address" className="modal-submit-button" type="submit">Update</button>
                    </form>
                </div>
            )}
        </dialog>
    );
};

export default ShippingAddressModal;