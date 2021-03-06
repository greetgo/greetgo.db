package kz.greetgo.gbatis.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import kz.greetgo.conf.sys_params.SysParams;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.gen.Nf6Generator;
import kz.greetgo.sqlmanager.gen.Nf6GeneratorPostgres;
import kz.greetgo.sqlmanager.gen.UserIdFieldType;
import kz.greetgo.sqlmanager.parser.StruShaper;
import org.postgresql.util.PSQLException;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractWithDbTest {
  private static String userid = System.getProperty("user.name") + "_gbatis";

  private int useridIndex = 0;

  private String userid() {
    if (useridIndex == 0) return userid;
    return userid + useridIndex;
  }

  public static void setUserid(String userid) {
    AbstractWithDbTest.userid = userid;
  }

  public static String getUserid() {
    return userid;
  }

  protected Conf conf;
  protected StruShaper sg;

  protected void prepareSG() throws Exception {
    sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(getStruUrl());
  }

  abstract protected URL getStruUrl();

  protected void prepareConf() {
    conf = new Conf();
    conf.separator = ";;";
    conf.mPrefix = "m_";
    conf.seqPrefix = "s_";
    conf.vPrefix = "v_";
    conf.withPrefix = "x_";
    conf.ts = "ts";
    conf.tsTab = "ts";
    conf.createdAt = "createdAt";
    conf.bigQuote = "big_quote";
    conf._ins_ = "ins_";
    conf._p_ = "p_";
    conf._value_ = "__value__";
    conf.daoSuffix = "Dao";
    conf.userIdFieldType = UserIdFieldType.Str;
  }

  public static String changeDb(String url, String userid) {
    int idx = url.lastIndexOf('/');
    if (idx < 0) return url + "/" + userid;
    return url.substring(0, idx + 1) + userid;
  }

  protected void setupJdbcTemplate() throws Exception {
    Class.forName("org.postgresql.Driver");

    prepareDb();

    {
      dataSource = new DataSourceAbstract() {
        @Override
        public Connection getConnection() throws SQLException {
          try {
            Class.forName("org.postgresql.Driver");
          } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
          return DriverManager.getConnection(getUrl(), getUserid(), getUserid());
        }
      };

      jdbc = new JdbcTemplate(dataSource);
    }
  }

  protected void prepareDb() throws Exception {
    {
      try (Connection con = DriverManager.getConnection(SysParams.pgAdminUrl(),
        SysParams.pgAdminUserid(), SysParams.pgAdminPassword())) {

        TRIES:
        for (useridIndex = 0; true; useridIndex++) {

          try (PreparedStatement ps = con.prepareStatement("drop database " + userid())) {
            ps.executeUpdate();
          } catch (PSQLException e) {
            System.err.println(e.getMessage());
          }

          try (PreparedStatement ps = con.prepareStatement("drop user " + userid())) {
            ps.executeUpdate();
          } catch (PSQLException e) {
            System.err.println(e.getMessage());
          }

          try {
            try (PreparedStatement ps = con.prepareStatement("create database " + userid())) {
              ps.executeUpdate();
            }
            break TRIES;
          } catch (SQLException e) {
            e.printStackTrace();
          }

        }//TRIES: for

        try (PreparedStatement ps = con.prepareStatement("create user " + userid()
          + " encrypted password '" + userid() + "'")) {
          ps.executeUpdate();
        }

        try (PreparedStatement ps = con.prepareStatement("grant all on database " + userid() + " to " + userid())) {
          ps.executeUpdate();
        }

      }
    }

    {
      try (Connection con = DriverManager.getConnection(getUrl(), userid(), userid())) {
        Nf6Generator nf6generator = new Nf6GeneratorPostgres(conf, sg);
        {
          ByteArrayOutputStream bout = new ByteArrayOutputStream();
          PrintStream out = new PrintStream(bout, false, "UTF-8");
          nf6generator.printSqls(out);
          nf6generator.printPrograms(out);
          out.flush();

          try (Statement st = con.createStatement()) {
            for (String sql : new String(bout.toByteArray(), UTF_8).split(";;")) {
              st.addBatch(sql);
            }
            st.executeBatch();
          }

        }
      }
    }
  }

  protected String getUrl() {
    return changeDb(SysParams.pgAdminUrl(), userid());
  }

  protected JdbcTemplate jdbc;
  protected DataSource dataSource;

  protected void executeSqls(List<String> sqls) throws SQLException {

    try (Connection con = dataSource.getConnection()) {
      try (Statement st = con.createStatement()) {
        for (String sql : sqls) {
          st.addBatch(sql);
        }
        st.executeBatch();
      }
    }
  }

}
