package kz.greetgo.gbatis.modelreader;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Класс заглушка для {@link ContentHandler}, чтобы постоянно не реализовывать все методы
 * интерфейса, только те которые нужны в данный момент
 * 
 * @author pompei
 */
public abstract class AbstractContentHandler implements ContentHandler {
  
  @Override
  public void setDocumentLocator(Locator locator) {}
  
  @Override
  public void startDocument() throws SAXException {}
  
  @Override
  public void endDocument() throws SAXException {}
  
  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException {}
  
  @Override
  public void endPrefixMapping(String prefix) throws SAXException {}
  
  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
  
  @Override
  public void processingInstruction(String target, String data) throws SAXException {}
  
  @Override
  public void skippedEntity(String name) throws SAXException {}
  
}
