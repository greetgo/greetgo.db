package kz.greetgo.db;

/**
 * Indicates transaction isolation level
 */
public enum IsolationLevel {
  /**
   * Using database default level
   */
  DEFAULT,

  READ_UNCOMMITTED,

  READ_COMMITTED,

  REPEATABLE_READ,

  SERIALIZABLE
}
