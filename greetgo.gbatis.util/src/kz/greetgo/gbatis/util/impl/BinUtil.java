package kz.greetgo.gbatis.util.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BinUtil {
  public static int writeInt(OutputStream out, int value) throws IOException {
    out.write(value & 0xFF);
    out.write((value >> 8) & 0xFF);
    out.write((value >> 16) & 0xFF);
    out.write((value >> 24) & 0xFF);
    return 4;
  }

  public static int writeLong(OutputStream out, long value) throws IOException {
    out.write((int) (value & 0xFF));
    out.write((int) ((value >> 8) & 0xFF));
    out.write((int) ((value >> 16) & 0xFF));
    out.write((int) ((value >> 24) & 0xFF));
    out.write((int) ((value >> 32) & 0xFF));
    out.write((int) ((value >> 40) & 0xFF));
    out.write((int) ((value >> 48) & 0xFF));
    out.write((int) ((value >> 56) & 0xFF));
    return 8;
  }

  public static int writeTime(OutputStream out, Date time) throws IOException {
    if (time == null) return writeStrAsUtf8fromLength(out, null);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return writeStrAsUtf8fromLength(out, sdf.format(time));
  }

  public static int writeStrAsUtf8fromLength(OutputStream out, String value) throws IOException {
    if (value == null) {
      writeInt(out, 1);
      out.write(0);
      return 5;
    }
    byte[] bytes = value.getBytes("UTF-8");
    writeInt(out, bytes.length);
    out.write(bytes);
    return bytes.length + 4;
  }

  public static byte b(String hexByte) {
    if (hexByte.length() != 2) throw new IllegalArgumentException();
    int n0 = hexToInt(hexByte.charAt(0));
    int n1 = hexToInt(hexByte.charAt(1));
    return (byte) (16 * n0 + n1);
  }

  public static byte[] bb(String hex) {
    if (hex.length() % 2 != 0) throw new IllegalArgumentException("hex.length must be % 2 == 0");
    int len = hex.length() / 2;
    byte[] ret = new byte[len];
    for (int i = 0; i < len; i++) {
      int n0 = hexToInt(hex.charAt(2 * i));
      int n1 = hexToInt(hex.charAt(2 * i + 1));
      ret[i] = (byte) (16 * n0 + n1);
    }
    return ret;
  }

  public static int hexToInt(char c) {
    if ('0' <= c && c <= '9') return c - '0';
    switch (c) {
      case 'a':
      case 'A':
        return 10;
      case 'b':
      case 'B':
        return 11;
      case 'c':
      case 'C':
        return 12;
      case 'd':
      case 'D':
        return 13;
      case 'e':
      case 'E':
        return 14;
      case 'f':
      case 'F':
        return 15;
      default:
        throw new IllegalArgumentException("c = " + c);
    }
  }

  final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }
}
