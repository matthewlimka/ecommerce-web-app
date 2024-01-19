package com.matthewlim.ecommercewebapp.suite;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"com.matthewlim.ecommercewebapp.unit.services"})
public class ServiceUnitTestSuite {}