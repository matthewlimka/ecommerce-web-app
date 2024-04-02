import { useState } from "react";
import User from "../models/User";
import PaymentMethod from "../enums/PaymentMethod";
import PaymentMethodModal from "./PaymentMethodModal";

interface TabProps {
    user: User;
}

const PaymentMethodsTab: React.FC<TabProps> = ({ user }) => {

    const [showModal, setShowModal] = useState(false);
    const paymentMethods: PaymentMethod[] = user!.registeredPaymentMethods;
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

    const closeModal = () => {
        setShowModal(false);
    };

    return (
        <div id="payment" className="account-page-main-section">
            <div id="payment" className="account-page-header-section">
                <h1 id="payment" className="account-page-header">My Payment Methods</h1>
                <button id="payment" className="account-page-header-button" onClick={() => setShowModal(true)}>Add Payment Method</button>
            </div>
            {showModal && <div className="account-page-modal-backdrop" />}
            <PaymentMethodModal showModal={showModal} closeModal={closeModal} paymentMethods={paymentMethods} />
            {user?.registeredPaymentMethods.length === 0 && <p className="account-page-no-payment-methods-message">No payment methods registered</p>}
            <div id="payment" className="account-page-payment-methods-section">
                {user?.registeredPaymentMethods.map((paymentMethod, index) => (
                    <img key={index} id="payment" className="account-page-payment-method-logo" src={paymentMethodToIcon(paymentMethod.toString())} />
                ))}
            </div>
        </div>
    );
};

export default PaymentMethodsTab;