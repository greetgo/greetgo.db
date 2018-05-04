package kz.greetgo.sqlmanager.gen;

import kz.greetgo.sqlmanager.model.JavaType;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kz.greetgo.util.ServerUtil.firstUpper;

/**
 * Сбор данных для генерации исходников класса. Предназначен для удобства формирования текста класса
 *
 * @author pompei
 */
public class ClassOuter {
  /**
   * Пакет класса
   */
  public final String packageName;
  /**
   * Имя класса
   */
  public final String className;

  /**
   * Список импортов класса
   */
  public final Map<String, String> imports = new HashMap<>();

  /**
   * Внутреннее содержимое класса, разбитое на строки: каждый элемент - строка
   */
  public final List<StringBuilder> content = new ArrayList<>();

  /**
   * Список полей класса
   */
  public final List<FieldOuter> fields = new ArrayList<>();

  /**
   * Текущая строка формирования класса
   */
  private StringBuilder line = null;

  /**
   * Допечатать в текущую строку. Если несколько строк, то обработать их корректно
   *
   * @param part часть содержимого (может состоять из нескольких строк)
   */
  public void print(String part) {
    for (String aline : part.split("\n")) {
      put(aline);
    }
  }

  /**
   * Пополнение текущей строки
   *
   * @param part часть строки
   */
  private void put(String part) {
    if (line == null) {
      line = new StringBuilder();
      content.add(line);
    }
    line.append(part);
  }

  /**
   * Печать с переходом на новую строку
   *
   * @param part часть строки или содержимого (всё будет обрабатываться корректно)
   */
  public void println(String part) {
    print(part);
    println();
  }

  /**
   * Переход на новую строку
   */
  public void println() {
    if (line != null) {
      line = null;
    } else {
      content.add(new StringBuilder());
    }
  }

  /**
   * Основной конструктор
   *
   * @param packageName пакет класса
   * @param pre         префикс к имени класса
   * @param className   имя класса без префикса
   */
  public ClassOuter(String packageName, String pre, String className) {
    this.packageName = packageName;
    this.className = pre + firstUpper(className);
  }

  /**
   * Фабричный метод: добавление нового поля.
   *
   * @param javaType тип поля
   * @param name     имя поля
   * @return посредник пост обработки
   */
  public FieldOuter addField(JavaType javaType, String name) {
    FieldOuter ret = new FieldOuter(javaType, name);
    fields.add(ret);
    return ret;
  }

  /**
   * Формирует короткое имя объекта, путём формирования импорта. Если такое короткое имя уже есть
   * среди импортов, то возвращается исходное (длинное)
   *
   * @param fullName длинное имя объекта
   * @return короткое имя объекта (если добавили в импорты), или исходное (длинное) имя объекта
   * (иначе)
   */
  public String i(String fullName) {
    int idx = fullName.lastIndexOf('.');
    if (idx < 0) return fullName;

    String name = fullName.substring(idx + 1);

    String imFullName = imports.get(name);

    if (imFullName == null) {
      imports.put(name, fullName);
      return name;
    }

    if (imFullName.equals(fullName)) return name;

    return fullName;
  }

  /**
   * Генерирует файла класса, из накопленных в этом экземпляре данных
   *
   * @param toDir директория, куда нужно сгенерировать исходники. Внутри этой директории, будут
   *              автоматически созданы поддиректории пакета
   */
  public void generateTo(String toDir) {
    File file = new File(toDir + "/" + packageName.replaceAll("\\.", "/") + "/" + className
        + ".java");
    file.getParentFile().mkdirs();
    try {
      PrintStream out = new PrintStream(file, "UTF-8");
      out.println("package " + packageName + ";");

      List<String> importValues = new ArrayList<>();

      for (String im : imports.values()) {
        if (anotherPackage(im)) importValues.add(im);
      }

      if (importValues.size() > 0) out.println();

      for (String im : importValues) {
        out.println("import " + im + ";");
      }

      for (StringBuilder sb : content) {
        out.println(sb);
      }

      out.println("}");
      out.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private boolean anotherPackage(String im) {
    if (!im.startsWith(packageName)) return true;
    String rest = im.substring(packageName.length() + 1);
    return rest.indexOf('.') >= 0;
  }

  /**
   * Получает полное имя класса
   *
   * @return полное имя класса
   */
  public String name() {
    return packageName + "." + className;
  }
}
