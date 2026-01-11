package ci553.shoppingcenter;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test Suite for Shopping Center Application
 * Runs all unit tests and integration tests
 */
@Suite
@SuiteDisplayName("Shopping Center Test Suite")
@SelectPackages({"ci553.shoppingcenter.service", "ci553.shoppingcenter.integration"})
public class AllTestsSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}

