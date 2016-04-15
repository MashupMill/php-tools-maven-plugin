package com.mashupmill.maven.plugin.php;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 * Created 4/15/16 @ 7:58 AM
 *
 * @author brandencash
 */
public class PHPLintMavenPluginTest extends AbstractMojoTestCase {

    /** {@inheritDoc} */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /** {@inheritDoc} */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testExecute() throws Exception {
        File pom = getTestFile("src/test/resources/php-lint-test/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        PHPLintMavenPlugin mojo = (PHPLintMavenPlugin) lookupMojo("lint", pom);
        assertNotNull(mojo);
        mojo.execute();
    }
}