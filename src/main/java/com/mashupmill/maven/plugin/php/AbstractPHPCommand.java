package com.mashupmill.maven.plugin.php;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * Created 6/2/15 @ 4:24 PM
 *
 * @author brandencash
 */
public abstract class AbstractPHPCommand extends AbstractCommand {
    @Parameter(defaultValue = "php", property = "php.tools.bin.php")
    private String phpBin;

    public String getPhpBin() {
        return phpBin;
    }
}
