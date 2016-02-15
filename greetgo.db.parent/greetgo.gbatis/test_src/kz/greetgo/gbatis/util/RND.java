package kz.greetgo.gbatis.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class RND {
  public static final Random rnd = new Random();
  
  public static final String CHAR_DIGITS = "012345678";
  public static final String CHAR_SMALL_ENG = "abcdefghijklmnopqrstuvwxyz";
  public static final String CHAR_BIG_ENG = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String CHAR_SMALL_RUS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
  public static final String CHAR_BIG_RUS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
  public static final String ALL;
  static {
    StringBuilder s = new StringBuilder();
    s.append(CHAR_DIGITS);
    s.append(CHAR_SMALL_ENG);
    s.append(CHAR_BIG_ENG);
    s.append(CHAR_SMALL_RUS);
    s.append(CHAR_BIG_RUS);
    ALL = s.toString();
  }
  
  public static final String rndStr(int len) {
    int allLen = ALL.length();
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < len; i++) {
      s.append(ALL.charAt(rnd.nextInt(allLen)));
    }
    return s.toString();
  }
  
  public static String rndStrInt(int len) {
    int allLen = CHAR_DIGITS.length();
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < len; i++) {
      s.append(CHAR_DIGITS.charAt(rnd.nextInt(allLen)));
    }
    return s.toString();
  }
  
  public static double rndDouble(double max, int point) {
    double ret = rnd.nextDouble();
    ret *= (double)max;
    for (int i = 0; i < point; i++) {
      ret *= 10.0;
    }
    ret = Math.floor(ret);
    for (int i = 0; i < point; i++) {
      ret /= 10.0;
    }
    return ret;
  }
  
  @SafeVarargs
  public static <E extends Enum<E>> E rndEnum(E... values) {
    return values[rnd.nextInt(values.length)];
  }
  
  public static int rndInt(int max) {
    return rnd.nextInt(max);
  }
  
  public static long rndLong(long max) {
    long tmp = rnd.nextLong();
    if (tmp < 0) tmp = -tmp;
    return tmp % max;
  }
  
  public static Date rndDate(int fromDaysBeforeNow, int toDayAfterNow) {
    Calendar cal = new GregorianCalendar();
    cal.add(Calendar.DAY_OF_YEAR, fromDaysBeforeNow);
    long from = cal.getTimeInMillis();
    cal.add(Calendar.DAY_OF_YEAR, toDayAfterNow - fromDaysBeforeNow);
    long to = cal.getTimeInMillis();
    long x = rndLong(to - from);
    cal.setTimeInMillis(from + x);
    return cal.getTime();
  }
  
  public static Date rndDate(Date startedAt, int daysAfter) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(startedAt);
    long from = cal.getTimeInMillis();
    cal.add(Calendar.DAY_OF_YEAR, daysAfter);
    long to = cal.getTimeInMillis();
    long x = rndLong(to - from);
    cal.setTimeInMillis(from + x);
    return cal.getTime();
  }
  
  public static boolean rndBool() {
    return rndInt(10) % 2 == 0;
  }
  
  public static BigDecimal rndBD(long max, int afterPoint) {
    BigDecimal ret = new BigDecimal(rndLong(max));
    for (int i = 0; i < afterPoint; i++) {
      ret = ret.multiply(BigDecimal.TEN);
      ret = ret.add(new BigDecimal(rndInt(10)));
    }
    
    for (int i = 0; i < afterPoint; i++) {
      ret = ret.divide(BigDecimal.TEN);
    }
    return ret;
  }
  
  public static void main(String[] args) {
    System.out.println(rndBD(1000000, 2));
  }
  
  public static String rndStr(String prefix, int len) {
    if (prefix == null) return rndStr(len);
    if (prefix.length() > len) return prefix;
    return prefix + rndStr(len - prefix.length());
  }
  
}
