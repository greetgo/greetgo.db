package kz.greetgo.db.nf36.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SqlLog {
  public final Date happenedAt = new Date();
  @SuppressWarnings("unused")
  public final long happenedAtNano = System.nanoTime();
  public final String sql;
  public final List<Object> params;
  public final Exception executeError;
  public final long nanoDelay;

  public SqlLog(String sql, List<Object> params, Exception executeError, long nanoDelay) {
    this.sql = sql;
    this.params = params;
    this.executeError = executeError;
    this.nanoDelay = nanoDelay;
  }

  public String toStr(boolean showHappenedAt, boolean showDelay, boolean showParams) {
    String happenedAtStr = "";
    if (showHappenedAt) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
      happenedAtStr = sdf.format(happenedAt) + ' ';
    }

    String delayStr = "";
    if (showDelay) {
      delayStr = "delay:" + delayStr() + ' ';
    }

    String paramStr = "";
    if (showParams) {
      paramStr = paramStr();
    }

    return happenedAtStr + delayStr + "EXEC_SQL: " + sql + paramStr;
  }

  private String delayStr() {
    double seconds = nanoDelay / 1_000_000_000.0;
    DecimalFormat df = new DecimalFormat("0.000000000");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setGroupingSeparator('_');
    dfs.setDecimalSeparator('.');
    df.setDecimalFormatSymbols(dfs);
    return df.format(seconds) + "sec";
  }

  public String paramStr() {

    StringBuilder sb = new StringBuilder();
    int index = 1;
    for (Object param : params) {
      sb.append("\n  param ").append(index++).append(" = ").append(param);
    }

    return sb.toString();
  }
}
