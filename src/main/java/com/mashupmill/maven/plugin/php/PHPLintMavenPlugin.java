package com.mashupmill.maven.plugin.php;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created 5/29/15 @ 2:42 PM
 *
 * @author brandencash
 */
@Mojo(name = "lint", defaultPhase = LifecyclePhase.COMPILE)
public class PHPLintMavenPlugin extends AbstractMojo {
    /**
     * A list of <code>fileSet</code> rules to select files and directories.
     */
    @Parameter
    private FileSet[] fileSets;

    @Parameter(defaultValue = "php")
    private String phpBin = "php";

    @Parameter(defaultValue = "false")
    private Boolean quiet = false;

    /**
     * The project being built.
     */
    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        String[] files = null;

        if (fileSets == null || fileSets.length == 0) {
            FileSet fileSet = new FileSet();
            fileSet.setDirectory(project.getProperties().getProperty("project.build.outputDirectory"));
            List<String> includes = new ArrayList<String>();
            includes.add("**/*.php");
            fileSet.setIncludes(includes);
            fileSets = new FileSet[]{fileSet};
        }

        FileSetManager fileSetManager = new FileSetManager();

        boolean success = true;
        for (FileSet fileSet : fileSets) {
            files = fileSetManager.getIncludedFiles(fileSet);
            if (files == null) {
                getLog().info("No files found for php lint checking");
                return;
            }


            for(String file : files) {
                success = lintCheck(new File(fileSet.getDirectory(), file)) && success;
            }
        }



        if (!success) {
            throw new RuntimeException("There were some errors while lint checking");
        }
    }

    protected boolean lintCheck(File file) {
        try {
            getLog().debug("Lint checking " + file.getAbsolutePath());
            Process p = Runtime.getRuntime().exec(new String[]{phpBin, "-l", file.getAbsolutePath()});
            int exitCode = p.waitFor();
            boolean success = exitCode == 0;

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (success) {
                    if (!quiet) getLog().info(line);
                } else {
                    getLog().error(line);
                }
            }
            return success;

        } catch (Exception e) {
            getLog().error("Error lint checking " + file.getPath(), e);
            return false;
        }
    }
}
