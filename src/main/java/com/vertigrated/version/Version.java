package com.vertigrated.version;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.vertigrated.version.Version.Qualifier.RELEASE;
import static com.vertigrated.version.Version.Qualifier.SNAPSHOT;
import static java.lang.String.format;

public class Version implements Comparable<Version>
{
    private static final Ordering<Version> NATURAL;
    private static final Pattern MAVEN_VERSION;

    static
    {
        NATURAL = new Ordering<Version>() {
            @Override public int compare(@Nullable final Version left, @Nullable final Version right)
            {
                return checkNotNull(left).major.compareTo(checkNotNull(right).major);
            }
        }.compound(new Ordering<Version>() {
            @Override public int compare(@Nullable final Version left, @Nullable final Version right)
            {
                return checkNotNull(left).minor.compareTo(checkNotNull(right).minor);
            }
        }).compound(new Ordering<Version>() {
            @Override public int compare(@Nullable final Version left, @Nullable final Version right)
            {
                return checkNotNull(left).patch.compareTo(checkNotNull(right).patch);
            }
        }).compound(new Ordering<Version>() {
            @Override public int compare(@Nullable final Version left, @Nullable final Version right)
            {
                return checkNotNull(left).qualifier.compareTo(checkNotNull(right).qualifier);
            }
        });

        MAVEN_VERSION = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(-SNAPSHOT)?$");
    }

    public enum Qualifier
    {
        SNAPSHOT,
        RELEASE;

        @Override public String toString()
        {
            return super.toString().equals("SNAPSHOT") ? "SNAPSHOT" : "";
        }
    }

    public final Integer major;
    public final Integer minor;
    public final Integer patch;
    public final Qualifier qualifier;

    public Version(@Nonnull final String versionString)
    {
        final Matcher m = MAVEN_VERSION.matcher(versionString);
        if (m.matches())
        {
            this.major = Ints.tryParse(m.group(1));
            this.minor = Ints.tryParse(m.group(2));
            this.patch = Ints.tryParse(m.group(3));
            this.qualifier = m.groupCount() == 4 ? SNAPSHOT : RELEASE;
        }
        else
        {
            throw new IllegalArgumentException(format("%s does not match %s", versionString, MAVEN_VERSION.toString()));
        }
    }

    public Version(final int major, final int minor, final int patch, @Nonnull final Qualifier qualifier)
    {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.qualifier = qualifier;
    }

    @Override public int compareTo(@Nonnull final Version o)
    {
        return NATURAL.compare(this,o);
    }

    @Override public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Version version = (Version) o;
        return Objects.equal(major, version.major) &&
               Objects.equal(minor, version.minor) &&
               Objects.equal(patch, version.patch) &&
               qualifier == version.qualifier;
    }

    @Override public int hashCode()
    {
        return Objects.hashCode(major, minor, patch, qualifier);
    }

    @Override public String toString()
    {
        return format("%d.%d.%d%s", this.major, this.minor, this.patch, this.qualifier.toString().isEmpty() ? "" : format("-%s", this.qualifier));
    }
}
