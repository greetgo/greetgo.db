package nf36_example_with_depinject.generated.impl.oracle.save.transaction;

import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.transaction.OnlyIdsSave;

public class OnlyIdsSaveImpl implements OnlyIdsSave {
  private final Nf36Saver saver13;

  public OnlyIdsSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("only_ids");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id1");
    saver.addIdName("id2");
  }

  @Override
  public void save(Object objectWithData) {
    saver13.putUpdateToNow("mod_at");
    saver13.save(objectWithData);
  }
}
