package kz.greetgo.sqlmanager.gen;

/**
 * Абстрактный класс для вынесения общей логики.
 * 
 * <p>
 * Смотрите {@link ViewFormer}
 * </p>
 * 
 * @author pompei
 * 
 */
public abstract class ViewFormerAbstract implements ViewFormer {
  
  protected Conf conf;
  
  /**
   * Конструктор для полчения конфигурации
   * 
   * @param conf
   *          конфигурация
   */
  protected ViewFormerAbstract(Conf conf) {
    this.conf = conf;
  }
  
  /**
   * Подготавливает отступ нужного размера
   * 
   * @param tabSize
   *          размер одного шага
   * @param tabs
   *          количество шагов
   * @return отступ
   */
  protected String space(int tabSize, int tabs) {
    StringBuilder sb = new StringBuilder(tabSize * tabs);
    for (int i = 0, C = tabSize * tabs; i < C; i++) {
      sb.append(' ');
    }
    return sb.toString();
  }
}
