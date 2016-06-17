package com.example.pavan.sunshine;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by pavan on 6/5/2016.
 */
public class FullTestSuite extends TestSuite {
    public FullTestSuite() {
        super();
    }

    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }
}
