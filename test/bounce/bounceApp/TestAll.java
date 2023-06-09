package bounce.bounceApp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The TestAll class declaration is annotated using JUnit's @RunWith and @Suite
 * annotations. The effect of these annotations is to define a test suite (a
 * collection of named unit test classes). When a test suite is run by the JUnit
 * TestRunner, all @Test methods in all named test classes are executed.
 *
 * @author Ian Warren
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({bounce.TestNestingShape.class,
        bounce.views.TestTask1.class,
        bounce.views.TestTask2.class,
        bounce.forms.TestImageShapeFormHandler.class})
public class TestAll {
}

