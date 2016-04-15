package com.mashupmill.maven.plugin.php;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;


/**
 * Created 5/29/15 @ 2:42 PM
 *
 * @author brandencash
 */
@Mojo(name = "phpcs", defaultPhase = LifecyclePhase.COMPILE)
public class PHPCSCommand extends AbstractPHPCommand {

    @Parameter(defaultValue = "${project.build.outputDirectory}")
    private File dir;

    @Parameter
    private String phpcsBin;

    @Parameter(property = "phpcs.arguments")
    private String argStr;

    @Override
    protected String[] getCommand() {
        return new String[]{getPhpBin(), phpcsBin == null ? getResourceCommand("phpcs.phar") : phpcsBin};
    }

    @Override
    protected String[] getDefaultArguments() {
        return new String[]{
                "--runtime-set",
                "ignore_warnings_on_exit",
                "1",
                "--standard=PSR2"
        };
    }

    @Override
    protected String[] buildArguments() {
        String[] arguments = super.buildArguments();
        arguments = (String[]) ArrayUtils.add(arguments, dir.getAbsolutePath());
        return arguments;
    }

    @Override
    public String[] getArguments() {
        return (String[]) ArrayUtils.addAll(parseArgumentString(argStr), super.getArguments());
    }
}
