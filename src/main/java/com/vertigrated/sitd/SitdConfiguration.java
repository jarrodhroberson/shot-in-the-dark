package com.vertigrated.sitd;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Nonnull;
import javax.validation.Valid;

public class SitdConfiguration extends Configuration
{
    @Valid
    @Nonnull
    @JsonProperty
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @Nonnull
    public DataSourceFactory getDataSourceFactory()
    {
        return dataSourceFactory;
    }

    @NotEmpty
    private String pathToProctorCredentials = System.getProperty("user.dir") + "/proctor.credentials";

    @JsonProperty
    public String getPathToProctorCredentials()
    {
        return pathToProctorCredentials;
    }

    @JsonProperty
    public void setPathToProctorCredentials(final String pathToProctorCredentials)
    {
        this.pathToProctorCredentials = pathToProctorCredentials;
    }
}