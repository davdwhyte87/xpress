package com.example.xpress;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthControllerTest.class,
        AirtimeControllerTest.class,
})
public class Process1 {

}
