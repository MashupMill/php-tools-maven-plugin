package com.mashupmill.maven.plugin.php;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created 5/29/15 @ 2:42 PM
 *
 * @author brandencash
 */
public abstract class AbstractCommand extends AbstractMojo {
    /**
     * Where to look for php files
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}")
    private File workingDirectory;

    @Parameter
    private String[] arguments;

    @Parameter
    private boolean skip;

    /**
     * The project being built.
     */
    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            getLog().info("Command is skipped per configuration");
            return;
        }

        if (!runCommand((String[]) ArrayUtils.addAll(getCommand(), buildArguments()), getWorkingDirectory())) {
            throw new RuntimeException("Failed to execute command");
        }
    }

    protected abstract String[] getCommand();

    protected String[] getDefaultArguments() {
        return null;
    }

    protected String[] buildArguments() {
        String[] arguments = getArguments();
        if (arguments == null || arguments.length == 0) {
            arguments = getDefaultArguments();
        }
        return arguments;
    }

    protected String getResourceCommand(String resourceName) {
        InputStream command = null;
        try {
            command = getClass().getClassLoader().getResourceAsStream(resourceName);
            File buildDir = new File(getProject().getBuild().getDirectory());
            File output = new File(buildDir, resourceName);
            FileUtils.writeByteArrayToFile(output, IOUtils.toByteArray(command));
            return output.getAbsolutePath();
        } catch (IOException e) {
            getLog().warn("Could not get command from resource [" + resourceName + "]", e);
            return resourceName;
        } finally {
            IOUtils.closeQuietly(command);
        }
    }

    protected boolean runCommand(String[] cmd, File workingDir) {
        try {
            Process p = Runtime.getRuntime().exec(cmd, null, workingDir);

            int exitCode = p.waitFor();
            boolean success = exitCode == 0;

            BufferedReader normal = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String line;
            while ((line = normal.readLine()) != null) {
                if (success) {
                    getLog().info(line);
                } else {
                    getLog().error(line);
                }
            }
            while ((line = error.readLine()) != null) {
                getLog().error(line);
            }

            return success;

        } catch (Exception e) {
            getLog().error("Error running command", e);
            throw new RuntimeException(e);
        }
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public String[] getArguments() {
        return arguments;
    }

    public boolean isSkip() {
        return skip;
    }

    public MavenProject getProject() {
        return project;
    }

    protected String[] parseArgumentString(String argStr) {
        if (StringUtils.isNotBlank(argStr)) {
            // From: http://stackoverflow.com/a/27608314/1454567
            Pattern regex = Pattern.compile("(([\"']).*?\\2|(?:[^\\\\ ]+\\\\\\s+)+[^\\\\ ]+|\\S+)");
            Matcher matcher = regex.matcher(argStr);
            String[] arguments = null;
            while( matcher.find() ) {
                String arg = matcher.group(0)
                        .replaceAll("\\\\ ", " ")
                        .replaceAll("^\"(.*)\"$", "$1")
                        .replaceAll("^'(.*)'$", "$1");
                arguments = (String[]) ArrayUtils.add(arguments, arg);
            }
            return arguments;
        } return null;
    }
}
