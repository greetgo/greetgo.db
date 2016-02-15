package kz.greetgo.gbatis.modelreader;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.InputStream;

import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlRequestSaxHandlerTest {
  @Test
  public void handlerTest() throws Exception {
    
    XmlRequestAcceptor acceptor = new XmlRequestAcceptor() {
      @Override
      public void accept(XmlRequest xmlRequest) {
        System.out.println("Accepted " + xmlRequest);
      }
    };
    
    XMLReader reader = XMLReaderFactory.createXMLReader();
    reader.setContentHandler(new XmlRequestSaxHandler(acceptor));
    
    InputStream inputStream = getClass().getResourceAsStream("XmlRequestSaxHandlerTest.xml");
    reader.parse(new InputSource(inputStream));
    inputStream.close();
    
    assertThat(1);
  }
}
