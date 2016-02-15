package ${package.version.package};

import com.vertigrated.version.Versioned;
import com.vertigrated.version.Version;

/**
 * Automatically generated from PackageVersion.java.in during
 * packageVersion-generate execution of maven-replacer-plugin in
 * pom.xml.
 */
public final class PackageVersion implements Versioned {

    private final static Version VERSION = new Version( "${project.version}");

    @Override
    public Version version() { return VERSION; }
}