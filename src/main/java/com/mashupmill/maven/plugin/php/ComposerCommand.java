package com.mashupmill.maven.plugin.php;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


/**
 * Created 5/29/15 @ 2:42 PM
 *
 * @author brandencash
 */
@Mojo(name = "composer", defaultPhase = LifecyclePhase.NONE)
public class ComposerCommand extends AbstractPHPCommand {
    @Parameter()
    private String composerBin;

    @Override
    protected String[] getCommand() {
        return new String[]{getPhpBin(), composerBin == null ? getResourceCommand("composer.phar") : composerBin};
    }

    @Override
    protected String[] getDefaultArguments() {
        return new String[]{"install"};
    }
}
