# php-tools-maven-plugin

Example Usage

```
<plugin>
    <groupId>com.mashupmill.maven</groupId>
    <artifactId>php-tools-maven-plugin</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <executions>
        <execution>
            <id>default-lint</id>
            <goals>
                <goal>lint</goal>
            </gloals>
            <phase>compile</phase>
            <configuration>
                <quiet>true</quiet>
                <phpBin>php</phpBin>
                <fileSets>
                    <fileSet>
                        <directory>./src</directory>
                        <includes>
                            <include>**/*.php</include>
                        </includes>
                        <excludes>
                            <exclude>**/exclude.php</exclude>
                        </excludes>
                    </fileSet>
                </fileSets>
            </configuration>
        </execution>
    </executions>
</plugin>
```