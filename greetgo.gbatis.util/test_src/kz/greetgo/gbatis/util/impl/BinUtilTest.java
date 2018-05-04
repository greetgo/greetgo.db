package kz.greetgo.gbatis.util.impl;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;

import static kz.greetgo.gbatis.util.impl.BinUtil.b;
import static kz.greetgo.gbatis.util.impl.BinUtil.bb;
import static org.fest.assertions.api.Assertions.assertThat;

public class BinUtilTest {
  @Test
  public void bTest() throws Exception {
    assertThat(b("00")).isEqualTo((byte) 0);
    assertThat(b("01")).isEqualTo((byte) 1);
    assertThat(b("02")).isEqualTo((byte) 2);
    assertThat(b("03")).isEqualTo((byte) 3);
    assertThat(b("0A")).isEqualTo((byte) 10);
    assertThat(b("0B")).isEqualTo((byte) 11);

    assertThat(b("aB")).isEqualTo((byte) 171);
  }

  @Test
  public void bbTest() throws Exception {

    byte[] expected = new byte[7];

    expected[0] = (byte) 0xFA;
    expected[1] = (byte) 0x01;
    expected[2] = (byte) 0xA7;
    expected[3] = (byte) 0x7b;
    expected[4] = (byte) 0xc9;
    expected[5] = (byte) 0x8c;
    expected[6] = (byte) 0xFe;

    assertThat(bb("FA01A77bc98cFe")).isEqualTo(expected);
  }

  @Test
  public void hexToInt() throws Exception {
    assertThat(BinUtil.hexToInt('A')).isEqualTo(10);
    assertThat(BinUtil.hexToInt('0')).isEqualTo(0);
    assertThat(BinUtil.hexToInt('9')).isEqualTo(9);
    assertThat(BinUtil.hexToInt('b')).isEqualTo(11);
    assertThat(BinUtil.hexToInt('c')).isEqualTo(12);
    assertThat(BinUtil.hexToInt('D')).isEqualTo(13);
    assertThat(BinUtil.hexToInt('E')).isEqualTo(14);
    assertThat(BinUtil.hexToInt('f')).isEqualTo(15);
  }

  @Test
  public void writeInt_001() throws Exception {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    BinUtil.writeInt(bout, 123);

    byte[] bytes = bout.toByteArray();

    assertThat(bytes).containsOnly(bb("7B000000"));

  }

  @Test
  public void writeInt_002() throws Exception {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    //
    //
    BinUtil.writeInt(bout, 5432525);
    //
    //

    assertThat(bout.toByteArray()).containsOnly(bb("CDE45200"));

  }

  @Test
  public void writeInt_003() throws Exception {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    //
    //
    BinUtil.writeInt(bout, 837560127);
    //
    //

    assertThat(bout.toByteArray()).containsOnly(bb("3F27ec31"));
  }

  @Test
  public void writeStrAsUtf8fromLength() throws Exception {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    String str = "Привет всем, 您好所有!";

    //
    //
    int len = BinUtil.writeStrAsUtf8fromLength(bout, str);
    //
    //

    byte[] result = bout.toByteArray();
    assertThat(result).isNotNull();
    assertThat(len).isEqualTo(result.length);

    byte[] strBytes = str.getBytes("UTF-8");
    assertThat(len).isEqualTo(strBytes.length + 4);
    
    byte[] actual = new byte[strBytes.length];
    System.arraycopy(result, 4, actual, 0, actual.length);
    
    assertThat(actual).isEqualTo(strBytes);
  }
}
