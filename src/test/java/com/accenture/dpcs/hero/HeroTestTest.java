package com.accenture.dpcs.hero;

import org.testng.Assert;
import org.testng.annotations.Test;

public class HeroTestTest extends HeroTest {

    @Test
    public void testInheritance() {
        HeroTest test = new HeroTest();
        Assert.assertTrue(test instanceof HeroBase,
                "Verify HeroTest extends HeroBase");
    }

}
