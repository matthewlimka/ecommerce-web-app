package com.matthewlim.ecommercewebapp;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.matthewlim.ecommercewebapp.controllers.integration.AddressControllerIntegrationTest;
import com.matthewlim.ecommercewebapp.controllers.integration.CartControllerIntegrationTest;
import com.matthewlim.ecommercewebapp.controllers.integration.CartItemControllerIntegrationTest;
import com.matthewlim.ecommercewebapp.controllers.integration.OrderControllerIntegrationTest;
import com.matthewlim.ecommercewebapp.controllers.integration.OrderItemControllerIntegrationTest;
import com.matthewlim.ecommercewebapp.controllers.integration.PaymentControllerIntegrationTest;
import com.matthewlim.ecommercewebapp.controllers.integration.ProductControllerIntegrationTest;
import com.matthewlim.ecommercewebapp.controllers.integration.UserControllerIntegrationTest;

@Suite
@SelectClasses({
    AddressControllerIntegrationTest.class,
    CartControllerIntegrationTest.class,
    CartItemControllerIntegrationTest.class,
    OrderControllerIntegrationTest.class,
    OrderItemControllerIntegrationTest.class,
    PaymentControllerIntegrationTest.class,
    ProductControllerIntegrationTest.class,
    UserControllerIntegrationTest.class
})
public class IntegrationTestSuite {

}
