package org.monet.docservice.docprocessor.templates.openxml.partsextractor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.monet.docservice.docprocessor.templates.common.Attributes;

public class BlockProcessor extends BaseXmlProcessor {

  String blockName;
  private int depth;
  
  public BlockProcessor(XMLStreamReader reader) throws XMLStreamException, FactoryConfigurationError {
    super(reader, null, null);
    underlayingOutputStream = new ByteArrayOutputStream();
    writer = XMLOutputFactory.newInstance().createXMLStreamWriter(underlayingOutputStream, "UTF-8");
    writer.writeStartElement("_8E03AB25A2E342ea84854A32DEA84BBC");
  }
  
  @Override
  public void setNamespceContext(Map<String, Namespace> context) throws XMLStreamException {
    super.setNamespceContext(context);
    for(Namespace n : this.getNamespaceContext().values())
      writer.writeNamespace(n.Prefix, n.URI);
    writer.writeCharacters("");
    writer.flush();
  }
  
  public void setBlockName(String blockName) {
    this.blockName = blockName;
  }
  
  @Override
  protected void finalize() throws Throwable {
    super.finalize();
  }
  
  @Override
  protected boolean handleStartElement(String localName, Attributes attributes) throws IOException, XMLStreamException {
    if(localName.equals("tbl")) {
      depth++;
    }
    return false;
  }
  
  @Override
  protected boolean handleEndElement(String localName) throws XMLStreamException, FactoryConfigurationError, IOException {
    if(localName.equals("tbl") && depth > 0) {
      depth--;
      if(depth == 0) {
        this.writer.writeEndElement();
        this.writer.writeEndDocument();
        this.writer.flush();
        
        ByteArrayOutputStream memoryOutput = (ByteArrayOutputStream)this.underlayingOutputStream;
        XMLStreamWriter memoryWriter = this.writer;
        this.underlayingOutputStream = null;
        this.writer = null;
        
        ByteArrayInputStream tempInputStream = new ByteArrayInputStream(memoryOutput.toByteArray());
        
        this.repository.addTemplatePart(this.templateId, this.blockName, tempInputStream);
        //Process the template part for sub-parts
        tempInputStream.reset();
        XMLStreamReader onMemoryReader = XMLInputFactory.newInstance().createXMLStreamReader(tempInputStream);
        RootProcessor proc = new RootProcessor(onMemoryReader, this.writer, underlayingOutputStream);
        proc.setPartial(true);
        proc.setNamespceContext(this.getNamespaceContext());
        proc.setRepository(this.repository);
        proc.setTemplateId(this.templateId);
        proc.Start();
        
        tempInputStream.close();
        memoryOutput.close();
        memoryWriter.close();
        
        Stop();
        return true;
      }
    }
    return false;
  }

  @Override
  protected boolean handleContent(String content) {
    return false;
  }
  
}
