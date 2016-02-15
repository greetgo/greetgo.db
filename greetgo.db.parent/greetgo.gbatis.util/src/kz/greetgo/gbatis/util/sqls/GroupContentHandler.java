package kz.greetgo.gbatis.util.sqls;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import kz.greetgo.gbatis.modelreader.AbstractContentHandler;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class GroupContentHandler extends AbstractContentHandler {
  
  public final Map<String, String> groupContent = new HashMap<>();
  
  private StringBuilder text = null;
  
  private String text() {
    if (text == null) return "";
    return text.toString();
  }
  
  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (text == null) text = new StringBuilder();
    text.append(ch, start, length);
  }
  
  private String id;
  
  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts)
      throws SAXException {
    id = atts.getValue("id");
  }
  
  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if ("sql".equals(localName)) {
      if (id == null) throw new RuntimeException("No id");
      if (groupContent.containsKey(id)) throw new RuntimeException("Id = " + id + " already exists");
      groupContent.put(id, text());
    }
    
    text = null;
    id = null;
  }
  
  public void readFrom(InputStream in) {
    try {
      XMLReader reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(this);
      reader.parse(new InputSource(in));
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        in.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
