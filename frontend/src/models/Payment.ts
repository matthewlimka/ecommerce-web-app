import PaymentMethod from "../enums/PaymentMethod";
import Order from "./Order";

interface Payment {
    paymentId?: number;
    paymentDate: Date;
    transactionId?: string;
    amount: number;
    paymentMethod: PaymentMethod;
    order?: Order;
}

export default Payment;