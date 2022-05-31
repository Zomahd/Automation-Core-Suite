package com.automation.core.suite;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AutomationCoreTestTest extends AutomationCoreTest {

    @Test
    public void testInheritance() {
        AutomationCoreTest test = new AutomationCoreTest();
        Assert.assertTrue(test instanceof AutomationCoreBase,
                "Verify AutomationCoreTest extends AutomationCoreBase");
    }

}
