package com.matthewlim.ecommercewebapp.suite;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"com.matthewlim.ecommercewebapp.integration"})
public class IntegrationTestSuite {}