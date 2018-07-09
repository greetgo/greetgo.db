package kz.greetgo.db.nf36.gen;

import kz.greetgo.class_scanner.ClassScanner;
import kz.greetgo.class_scanner.ClassScannerDef;
import kz.greetgo.db.nf36.core.Nf3Entity;
import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModelCollector {

  int idLength;
  String nf3prefix;
  String nf6prefix;
  int enumLength = 0;
  String nf6TableSeparator = "_";
  String nf6timeField = "ts";
  String nf3CreatedAtField = null;
  String nf3ModifiedAtField = null;
  int defaultLength;
  int shortLength;
  int longLength;
  private String moreMethodName = null;
  private String commitMethodName = null;
  private String sequencePrefix = null;
  private ClassScanner classScanner = new ClassScannerDef();
  String sourceBasePackage;

  public ModelCollector setSourceBasePackage(String sourceBasePackage) {
    this.sourceBasePackage = sourceBasePackage;
    return this;
  }

  @SuppressWarnings("unused")
  public ModelCollector setClassScanner(ClassScanner classScanner) {
    if (classScanner == null) throw new IllegalArgumentException("classScanner == null");
    this.classScanner = classScanner;
    return this;
  }

  public String sequencePrefix() {
    if (sequencePrefix == null) throw new RuntimeException("sequencePrefix is null, please call method" +
      " setSequencePrefix of ModelCollector to define it");
    return sequencePrefix;
  }

  public ModelCollector setSequencePrefix(String sequencePrefix) {
    this.sequencePrefix = sequencePrefix;
    return this;
  }

  public String commitMethodName() {
    if (commitMethodName == null) throw new NullPointerException("commitMethodName == null");
    return commitMethodName;
  }

  public ModelCollector setCommitMethodName(String commitMethodName) {
    this.commitMethodName = commitMethodName;
    return this;
  }

  public String moreMethodName() {
    if (moreMethodName == null) throw new NullPointerException("moreMethodName == null");
    return moreMethodName;
  }

  public ModelCollector setMoreMethodName(String moreMethodName) {
    this.moreMethodName = moreMethodName;
    return this;
  }

  private ModelCollector() {}

  public static ModelCollector newCollector() {
    return new ModelCollector();
  }

  public ModelCollector setNf3CreatedAtField(String nf3CreatedAtField) {
    this.nf3CreatedAtField = nf3CreatedAtField;
    return this;
  }

  @SuppressWarnings("unused")
  public ModelCollector setNf6timeField(String nf6timeField) {
    this.nf6timeField = nf6timeField;
    return this;
  }

  public ModelCollector setEnumLength(int enumLength) {
    this.enumLength = enumLength;
    return this;
  }

  public ModelCollector setNf3ModifiedAtField(String nf3ModifiedAtField) {
    this.nf3ModifiedAtField = nf3ModifiedAtField;
    return this;
  }

  public String getNf6TableName(Nf3Table nf3Table, Nf3Field field) {
    return nf3Table.nf6prefix() + nf3Table.tableName() + nf6TableSeparator + field.rootField().dbName();
  }

  @SuppressWarnings("unused")
  public ModelCollector setNf6TableSeparator(String nf6TableSeparator) {
    this.nf6TableSeparator = nf6TableSeparator;
    return this;
  }

  public ModelCollector setNf3Prefix(String nf3prefix) {
    this.nf3prefix = nf3prefix;
    return this;
  }

  public ModelCollector setNf6Prefix(String nf6prefix) {
    this.nf6prefix = nf6prefix;
    return this;
  }

  final List<Object> registeredObjects = new ArrayList<>();


  public ModelCollector register(Object object) {
    registeredObjects.add(object);
    return this;
  }

  private List<Nf3TableImpl> collectedList = null;

  private Map<Class<?>, Nf3TableImpl> collectedMap = null;


  public List<Nf3Table> collect() {
    if (collectedList != null) return Collections.unmodifiableList(collectedList);

    collectedList = new ArrayList<>();

    for (Object object : registeredObjects) {
      collectedList.add(new Nf3TableImpl(this, object));
    }

    collectedMap = collectedList.stream().collect(Collectors.toMap(Nf3TableImpl::source, Function.identity()));

    collectedList.sort(Comparator.comparing(t -> t.source().getSimpleName()));

    collectedList.forEach(this::fillReferencesFor);

    collectedMap = null;

    return Collections.unmodifiableList(collectedList);
  }

  private void fillReferencesFor(Nf3TableImpl nf3Table) {

    Set<String> allReferences = nf3Table.fields().stream()
      .filter(Nf3Field::isReference)
      .map(Nf3Field::javaName)
      .collect(Collectors.toSet());

    Set<String> allNextParts = nf3Table.fields().stream()
      .filter(Nf3Field::hasNextPart)
      .map(Nf3Field::nextPart)
      .collect(Collectors.toSet());

    Set<String> roots = new HashSet<>(allReferences);
    roots.removeAll(allNextParts);

    Map<String, String> nextPartMap = nf3Table.fields().stream()
      .filter(Nf3Field::hasNextPart).
        collect(Collectors.toMap(Nf3Field::javaName, Nf3Field::nextPart));

    for (String root : roots) {

      Nf3FieldImpl rootField = (Nf3FieldImpl) nf3Table.getByJavaName(root);

      rootField.referenceTo = collectedMap.get(rootField.referenceToClass());

      if (rootField.referenceTo == null) {
        throw new RuntimeException("Broken reference: class " + rootField.referenceToClass().getSimpleName()
          + " is not registered. Error in " + nf3Table.source().getSimpleName()
          + ". You need register " + rootField.referenceToClass());
      }

      List<String> referenceJavaNames = new ArrayList<>();
      {
        String currentJavaName = root;
        while (currentJavaName != null) {

          Nf3Field field = nf3Table.getByJavaName(root);
          if (!field.isReference()) {
            throw new RuntimeException("Field " + nf3Table.source().getSimpleName() + "." + field.javaName()
              + " is not reference part, but there is a link to it"
            );
          }
          if (rootField.referenceToClass() != field.referenceToClass()) {
            throw new RuntimeException("Field " + nf3Table.source().getSimpleName() + "." + field.javaName()
              + " must has reference to " + rootField.referenceToClass().getSimpleName()
            );
          }

          referenceJavaNames.add(currentJavaName);
          currentJavaName = nextPartMap.remove(currentJavaName);
        }
      }
      rootField.isRootReference = true;
      rootField.referenceFields = referenceJavaNames.stream()
        .map(nf3Table::getByJavaName)
        .collect(Collectors.toList());
      for (Nf3Field referenceField : rootField.referenceFields) {
        ((Nf3FieldImpl) referenceField).rootField = rootField;
      }
    }
  }

  public ModelCollector setIdLength(int idLength) {
    this.idLength = idLength;
    return this;
  }

  public ModelCollector setDefaultLength(int defaultLength) {
    this.defaultLength = defaultLength;
    return this;
  }

  public ModelCollector setShortLength(int shortLength) {
    this.shortLength = shortLength;
    return this;
  }

  public ModelCollector setLongLength(int longLength) {
    this.longLength = longLength;
    return this;
  }

  AuthorField nf3CreatedBy = null;
  AuthorField nf3ModifiedBy = null;
  AuthorField nf6InsertedBy = null;

  public ModelCollector setAuthorFields(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy,
                                        AuthorType authorType, int typeLength) {

    if (nf3CreatedBy == null && nf3ModifiedBy == null && nf6InsertedBy == null) {
      this.nf3CreatedBy = null;
      this.nf3ModifiedBy = null;
      this.nf6InsertedBy = null;
      return this;
    }

    if (nf3CreatedBy == null || nf3ModifiedBy == null || nf6InsertedBy == null) {
      throw new IllegalArgumentException("nf3CreatedBy, nf3ModifiedBy, nf6InsertedBy:" +
        " must be all is null, or all is not null");
    }

    this.nf3CreatedBy = new AuthorField(nf3CreatedBy, authorType, typeLength);
    this.nf3ModifiedBy = new AuthorField(nf3ModifiedBy, authorType, typeLength);
    this.nf6InsertedBy = new AuthorField(nf6InsertedBy, authorType, typeLength);
    return this;
  }

  public ModelCollector scanPackageOfClassRecursively(Class<?> classForPackage) {
    for (Class<?> aClass : classScanner.scanPackage(classForPackage.getPackage().getName())) {
      if (aClass.getAnnotation(Nf3Entity.class) != null) {
        try {
          register(aClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return this;
  }
}
