package kz.greetgo.gbatis.modelreader;

import kz.greetgo.gbatis.model.RequestType;
import kz.greetgo.gbatis.model.WithView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Обработчик xml-а запроса
 * 
 * <p>
 * Используется для получения {@link XmlRequest} из xml-а запроса посредством SAX-сканированием
 * </p>
 * 
 * @author pompei
 */
public class XmlRequestSaxHandler extends AbstractContentHandler {
  
  private XmlRequestAcceptor acceptor;
  
  public XmlRequestSaxHandler(XmlRequestAcceptor acceptor) {
    this.acceptor = acceptor;
  }
  
  private StringBuilder text = null;
  private XmlRequest xmlRequest = null;
  private WithView withView = null;
  
  private String text() {
    if (text == null) return "";
    return text.toString();
  }
  
  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (text == null) text = new StringBuilder();
    text.append(ch, start, length);
  }
  
  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts)
      throws SAXException {
    text = null;
    if ("request".equals(localName)) {
      if (xmlRequest != null) throw new SAXException("Left open tag: <request>");
      xmlRequest = new XmlRequest();
      xmlRequest.id = atts.getValue("id");
      xmlRequest.type = RequestType.valueOf(atts.getValue("type"));
      return;
    }
    
    if ("with".equals(localName)) {
      if (withView != null) throw new SAXException("Left open tag: <with>");
      withView = new WithView();
      xmlRequest.withViewList.add(withView);
      withView.table = atts.getValue("value");
      withView.view = atts.getValue("name");
      String fields = atts.getValue("fields");
      if (fields != null) for (String field : fields.split(",")) {
        String tmp = field.trim();
        if (tmp.length() > 0) withView.fields.add(tmp);
      }
    }
  }
  
  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if ("request".equals(localName)) {
      if (xmlRequest == null) throw new SAXException("Left close tag: </request>");
      if (xmlRequest.sql == null) throw new SAXException("No sql read");
      acceptor.accept(xmlRequest);
      xmlRequest = null;
      return;
    }
    if ("sql".equals(localName)) {
      xmlRequest.sql = text();
      return;
    }
    if ("with".equals(localName)) {
      if (withView == null) throw new SAXException("Left close tag: </with>");
      for (String field : text().split(",")) {
        String tmp = field.trim();
        if (tmp.length() > 0) withView.fields.add(tmp);
      }
      withView = null;
      return;
    }
  }
}
