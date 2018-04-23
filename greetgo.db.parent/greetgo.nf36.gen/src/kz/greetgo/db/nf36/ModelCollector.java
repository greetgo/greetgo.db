package kz.greetgo.db.nf36;

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

  String nf3prefix;
  String nf6prefix;
  int enumLength = 0;

  private ModelCollector() {}

  public static ModelCollector newCollector() {
    return new ModelCollector();
  }

  public ModelCollector setEnumLength(int enumLength) {
    this.enumLength = enumLength;
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
    }
  }

}
