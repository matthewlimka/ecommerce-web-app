import '../styles/UpdateModal.css';
import { useRef, useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useAPI } from '../contexts/APIContext';
import PaymentMethod from '../enums/PaymentMethod';
import FormatUtils from '../FormatUtils';


interface ModalProps {
    showModal: boolean;
    closeModal: () => void;
    paymentMethods: PaymentMethod[];
}

const PaymentMethodModal: React.FC<ModalProps> = ({ showModal, closeModal, paymentMethods }) => {

    const dialogRef = useRef<HTMLDialogElement>(null);
    const { jwt } = useAuth();
    const { updatePaymentMethods } = useAPI();
    const [formData, setFormData] = useState<PaymentMethod[]>(paymentMethods);
    const [updateSuccess, setUpdateSuccess] = useState(false);
    const availablePaymentMethods = Object.values(PaymentMethod).filter(
        (method) => !formData.includes(method)
    );

    const handleRemovePaymentMethod = (paymentMethod: PaymentMethod) => {
        setFormData(formData.filter((method) => method !== paymentMethod));
    }

    const handleAddPaymentMethod = (event: any) => {
        event.preventDefault();
        const newPaymentMethod = event.currentTarget.paymentMethod.value;
        setFormData([...formData, newPaymentMethod]);
        event.currentTarget.reset();
    }

    const paymentMethodToIcon = (paymentMethod: string) => {
        switch (paymentMethod) {
            case PaymentMethod.PAYNOW:
                return "/paynow-logo.svg";
            case PaymentMethod.CREDIT_CARD_MASTERCARD:
                return "/mastercard-logo.svg";
            case PaymentMethod.CREDIT_CARD_VISA:
                return "/visa-logo.svg";
            default:
                return '';
        }
    };

    const handleUpdatePaymentMethods = (event: any) => {
        event.preventDefault();
        updatePaymentMethods(jwt!, formData);
        setUpdateSuccess(true);
    }

    const handleCloseModal = (event: any) => {
        event.preventDefault();
        closeModal();
        setUpdateSuccess(false);
    }

    return (
        <dialog id="payment-methods" className="modal" ref={dialogRef} open={showModal}>
            <p id="payment-methods" className="modal-close-button" onClick={handleCloseModal}>X</p>
            {updateSuccess ? (
                <div id="payment-methods" className="modal-after-update">
                    <h2 id="payment-methods" className="modal-header">Success!</h2>
                    <p id="payment-methods" className="modal-success-message">Payment methods have been updated</p>
                </div>
            ) : (
                <div id="payment-methods" className="modal-before-update">
                    <h2 id="payment-methods" className="modal-header">Update Payment Methods</h2>
                    <form id="payment-methods" className="modal-form" onSubmit={handleAddPaymentMethod}>
                        <div id="payment-methods" className="add-payment-method">
                            <h3 id="payment-methods" className="add-payment-method-header">Add New Payment Method</h3>
                            <div id="payment-methods" className="add-payment-method-content">
                                <select id="payment-methods" className="add-payment-method-select" name="paymentMethod">
                                    {availablePaymentMethods.length > 0 ? (
                                        availablePaymentMethods.map((paymentMethod, index) => (
                                            <option key={index} id="payment-methods" value={paymentMethod}>
                                                {FormatUtils.formatPaymentMethod(paymentMethod)}
                                            </option>
                                        ))
                                    ) : (
                                        <option disabled>No available payment methods</option>
                                    )}
                                </select>
                                <button id="payment-methods" className="add-payment-method-add-button" type="submit">Add</button>
                            </div>
                        </div>
                    </form>
                    <form id="payment-methods" className="modal-form" onSubmit={handleUpdatePaymentMethods}>
                        <div id="payment-methods" className="current-payment-methods">
                            <h3 id="payment-methods" className="current-payment-methods-header">Current Payment Methods</h3>
                            <ul id="payment-methods" className="current-payment-methods-list">
                                {formData.map((paymentMethod, index) => (
                                    <li key={index} id="payment-methods" className="current-payment-method">
                                        <img className="current-payment-method-logo" src={paymentMethodToIcon(paymentMethod.toString())} />
                                        <button className="current-payment-method-remove-button" type="button" onClick={() => handleRemovePaymentMethod(paymentMethod)}>Remove</button>
                                    </li>
                                ))}
                            </ul>
                        </div>
                        <button id="payment-methods" className="modal-submit-button" type="submit">Save</button>
                    </form>
                </div>
            )}
        </dialog>
    );
};

export default PaymentMethodModal;