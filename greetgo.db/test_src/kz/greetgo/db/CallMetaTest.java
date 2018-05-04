package kz.greetgo.db;

import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class CallMetaTest {

  @SuppressWarnings("unused")
  static class ParentThrowable extends Throwable {
  }

  @SuppressWarnings("unused")
  private static class ChildThrowable extends ParentThrowable {
  }

  @Test
  public void needToCommit_001() throws Exception {
    //noinspection unchecked
    final CallMeta callMeta = new CallMeta(null, null, new Class[]{ParentThrowable.class});

    //
    //
    assertThat(callMeta.needToCommit(new ChildThrowable())).isTrue();
    //
    //
  }

  @Test
  public void needToCommit_002() throws Exception {
    //noinspection unchecked
    final CallMeta callMeta = new CallMeta(null, null, new Class[]{ChildThrowable.class});

    //
    //
    assertThat(callMeta.needToCommit(new ParentThrowable())).isFalse();
    //
    //
  }

  @SuppressWarnings("unused")
  private static class LeftThrowable extends Throwable {
  }

  @Test
  public void needToCommit_003() throws Exception {
    //noinspection unchecked
    final CallMeta callMeta = new CallMeta(null, null, new Class[]{ChildThrowable.class});

    //
    //
    assertThat(callMeta.needToCommit(new LeftThrowable())).isFalse();
    //
    //
  }

  @Test
  public void needToCommit_004() throws Exception {
    //noinspection unchecked
    final CallMeta parentCallMeta = new CallMeta(null, null, new Class[]{ParentThrowable.class});

    //noinspection unchecked
    final CallMeta callMeta = new CallMeta(parentCallMeta, null, new Class[]{LeftThrowable.class});

    //
    //
    assertThat(callMeta.needToCommit(new ChildThrowable())).isTrue();
    //
    //
  }

  @Test
  public void needToCommit_005() throws Exception {
    //noinspection unchecked
    final CallMeta parentCallMeta = new CallMeta(null, null, new Class[]{ParentThrowable.class});

    //noinspection unchecked
    final CallMeta callMeta = new CallMeta(parentCallMeta, null, new Class[]{});

    //
    //
    assertThat(callMeta.needToCommit(new ChildThrowable())).isTrue();
    //
    //
  }

  @Test
  public void needToCommit_006() throws Exception {
    //noinspection unchecked
    final CallMeta parentCallMeta = new CallMeta(null, null, new Class[]{ParentThrowable.class});

    //noinspection unchecked
    final CallMeta callMeta = new CallMeta(parentCallMeta, null, null);

    //
    //
    assertThat(callMeta.needToCommit(new ChildThrowable())).isTrue();
    //
    //
  }

  @Test
  public void needToCommit_007() throws Exception {
    //noinspection unchecked
    final CallMeta parentCallMeta = new CallMeta(null, null, null);

    //noinspection unchecked
    final CallMeta callMeta = new CallMeta(parentCallMeta, null, null);

    //
    //
    assertThat(callMeta.needToCommit(new ChildThrowable())).isFalse();
    //
    //
  }
}