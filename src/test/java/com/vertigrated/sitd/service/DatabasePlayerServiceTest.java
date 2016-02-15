package com.vertigrated.sitd.service;

public class DatabasePlayerServiceTest
{
/*    private static final Provider<DataSource> DATA_SOURCE_PROVIDER;
    private static final Path SITD_TEST = Paths.get(System.getProperty("user.dir"), "SITD_TEST");*/

    static
    {
/*        DATA_SOURCE_PROVIDER = new Provider<Connection>()
        {
            @Override
            public Connection get()
            {
                final Properties p = new Properties();
                p.put("user", "admin");
                p.put("password", "1qaz2wsx");
                try
                {
                    return DriverManager.getConnection("jdbc:derby:SITD_TEST;create=true", p);
                }
                catch (final SQLException e)
                {
                    throw new RuntimeException(e);
                }
            }
        };*/
    }

/*    @BeforeClass
    public static void beforeClass() throws Exception
    {
        if (Files.exists(SITD_TEST))
        {
            Files.walkFileTree(SITD_TEST, new RecursiveDeleteFileVisitor());
        }
        assertThat(true, is(Files.notExists(SITD_TEST, LinkOption.NOFOLLOW_LINKS)));

        final DSLContext dsl = DSL.using(DATA_SOURCE_PROVIDER.get(), DERBY);
        final String[] commands = Resources.toString(Resources.getResource("derby/create_tables.sql").toURI().toURL(), UTF_8).split(";\\n?");
        for (final String c : commands)
        {
            dsl.execute(c);
        }
    }

    @AfterClass
    public static void afterClass() throws Exception
    {
        final DSLContext dsl = DSL.using(DATA_SOURCE_PROVIDER.get(), DERBY);

        final String[] commands = Resources.toString(Resources.getResource("derby/drop_tables.sql").toURI().toURL(), UTF_8).split(";\\n?");
        for (final String c : commands)
        {
            System.out.println(c);
            dsl.execute(c);
        }
        if (Files.exists(SITD_TEST))
        {
            Files.walkFileTree(SITD_TEST, new RecursiveDeleteFileVisitor());
        }
        assertThat(true, is(Files.notExists(SITD_TEST, LinkOption.NOFOLLOW_LINKS)));
    }

    @Test
    public void testCreate()
    {
        final PlayerService ps = new DatabasePlayerService(DATA_SOURCE_PROVIDER);
        final Player p = ps.create("Jarrod Roberson");
        assertThat(p, is(equalTo(ps.retrieve(p.id))));
        assertThat(p, is(equalTo(ps.retrieve(p.name))));
    }*/
}
