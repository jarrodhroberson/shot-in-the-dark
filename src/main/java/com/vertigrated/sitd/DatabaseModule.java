package com.vertigrated.sitd;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseModule extends AbstractModule
{
    @Override protected void configure()
    {
        final String userDir = System.getProperty("user.dir");
        bind(Connection.class).toProvider(new Provider<Connection>() {
            @Override
            public Connection get()
            {
                final Properties p = new Properties();
                p.put("user", "admin");
                p.put("password", "1qaz2wsx");
                try
                {
                    Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                    return DriverManager.getConnection(String.format("jdbc:derby:%s:sitd;create=true", userDir), p);
                }
                catch (final SQLException | ClassNotFoundException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }).in(Scopes.SINGLETON);
    }
}
