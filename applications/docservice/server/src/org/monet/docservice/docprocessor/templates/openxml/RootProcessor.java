package org.monet.docservice.docprocessor.templates.openxml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.monet.docservice.core.Key;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.templates.common.Attributes;
import org.monet.docservice.docprocessor.templates.common.BaseXmlProcessor;
import org.monet.docservice.docprocessor.templates.openxml.constants.OpenXMLFieldType;
import org.monet.docservice.guice.InjectorFactory;

public class RootProcessor extends BaseXmlProcessor {

  private static Pattern mergeFieldValue           = Pattern.compile("MERGEFIELD\\s+\"?([^\\s|\"]*)\"?\\s+\\\\\\*\\s+MERGEFORMAT");
  private static Pattern mergeIFFieldValue         = Pattern.compile("IF\\s+\"?([^\\s|\"]*)\"?\\s+\"?([^\\s|\"]*)\"?\\s+\"?([^\\s|\"]*)\"?\\s+\\\\\\*\\s+MERGEFORMAT");

  private static Pattern tableFieldValue           = Pattern.compile("[Table|table|TABLE]\\(([^\\)]*)\\)");
  private static Pattern blockFieldValue           = Pattern.compile("[Block|block|BLOCK]\\(([^\\)]*)\\)");

  private Collection<?>  currentCollection;
  private boolean        captureTable              = false;

  private boolean        insideField               = false;
  private boolean        insideFieldValue          = false;
  private boolean        insideFieldValueContent   = false;
  private int            fieldType                 = OpenXMLFieldType.UNKNOW;
  private String         ffDataName                = "";

  private boolean        insideInstrText           = false;
  private boolean        valueInserted             = false;
  private String         valueOfField              = "";
  private StringBuilder  fieldIdentifier           = new StringBuilder();
  private String         tableName;
  private boolean        captureBlock;
  private boolean        showBlock;
  private String         blockName;
  private boolean        startBlock                = false;
  private boolean        isInRPRBlock              = false;
  private List<String>   styles;
  private Formatter      formatter;
  private boolean        insideDrawing             = false;
  private Repository     repository;
  private Logger         logger;
  private boolean        finishDrawingInsideSimple = false;

  public RootProcessor(Key documentKey, InputStream documentStream, OutputStream processedDocStream) throws XMLStreamException, FactoryConfigurationError {
    super(documentKey, documentStream, processedDocStream);
    init();
  }

  public RootProcessor(Key documentKey, XMLStreamReader documentStream, XMLStreamWriter processedDocStream, OutputStream underlayingOutputStream) throws XMLStreamException, FactoryConfigurationError {
    super(documentKey, documentStream, processedDocStream, underlayingOutputStream);
    init();
  }

  private void init() {
    this.styles = new ArrayList<String>();
    this.formatter = Formatter.getInstance();
    this.newIdImages = new HashSet<String>();
    this.oldIdImages = new HashSet<String>();
    this.repository = InjectorFactory.get().getInstance(Repository.class);
    this.logger = InjectorFactory.get().getInstance(Logger.class);
  }

  protected boolean handleStartElement(String localName, Attributes attributes) throws IOException, XMLStreamException {

    if (insideFieldValue && localName.equals("rPr")) {
      isInRPRBlock = true;
      this.styles.clear();
    } else if (insideFieldValue && isInRPRBlock) {
      String attrs = "";
      for (int i = 0; i < attributes.getCount(); i++)
        attrs += "w:" + attributes.getName(i) + "=" + "\"" + attributes.getValue(i) + "\"" + "  ";
      styles.add("<w:" + localName + " " + attrs + "/>");
    } else if (insideFieldValue && localName.equals("drawing")) {
      insideDrawing = true;
    }

    if (insideDrawing) {
      if (localName.equals("blip")) {
        if (this.valueOfField != null && !this.valueOfField.isEmpty()) {
          String idImage = attributes.getValue("r:embed");
          String cstate = attributes.getValue("cstate");
          this.newIdImages.add(this.valueOfField);
          this.oldIdImages.add(idImage);
          this.writer.writeStartElement("a:blip");
          this.writer.writeAttribute("r:embed", this.valueOfField);
          if (cstate != null)
            this.writer.writeAttribute("cstate", cstate);
        } else {
          return false;
        }
        return true;
      } else if (localName.equals("extent")) {
        handleTableField();
        if (this.valueOfField != null && !this.valueOfField.isEmpty()) {
          int[] dimension = this.repository.getImageDimension(Key.from(documentKey.getSpace(), this.valueOfField));
          int imageWidth = dimension[0];
          int imageHeight = dimension[1];
          if (imageWidth > imageHeight) {
            imageWidth = Integer.parseInt(attributes.getValue("cx")) / (914400 / 96);
            imageHeight = (dimension[1] * imageWidth) / dimension[0];
          } else {
            imageHeight = Integer.parseInt(attributes.getValue("cy")) / (914400 / 96);
            imageWidth = (dimension[0] * imageHeight) / dimension[1];
          }
          int cx = imageWidth * 914400 / 96;
          int cy = imageHeight * 914400 / 96;
          this.writer.writeStartElement("wp:extent");
          this.writer.writeAttribute("cx", Integer.toString(cx));
          this.writer.writeAttribute("cy", Integer.toString(cy));
        } else {
          return false;
        }
        return true;
      } else if (localName.equals("ext")) {
        if (this.valueOfField != null && !this.valueOfField.isEmpty()) {
          int[] dimension = this.repository.getImageDimension(Key.from(documentKey.getSpace(), this.valueOfField));
          int imageWidth = dimension[0];
          int imageHeight = dimension[1];
          if (imageWidth > imageHeight) {
            imageWidth = Integer.parseInt(attributes.getValue("cx")) / (914400 / 96);
            imageHeight = (dimension[1] * imageWidth) / dimension[0];
          } else {
            imageHeight = Integer.parseInt(attributes.getValue("cy")) / (914400 / 96);
            imageWidth = (dimension[0] * imageHeight) / dimension[1];
          }
          int cx = imageWidth * 914400 / 96;
          int cy = imageHeight * 914400 / 96;
          this.writer.writeStartElement("a:ext");
          this.writer.writeAttribute("cx", Integer.toString(cx));
          this.writer.writeAttribute("cy", Integer.toString(cy));
        } else {
          return false;
        }
        return true;
      }
      return false;
    }

    if (localName.equals("fldChar")) {
      String fldCharType = attributes.getValue("w:fldCharType");

      if (fldCharType.equals("begin")) {
        insideField = true;
        valueInserted = false;
      } else if (fldCharType.equals("separate")) {
        insideFieldValue = true;
        insideInstrText = false;
      } else if (fldCharType.equals("end")) {
        insideFieldValueContent = insideFieldValue = insideField = false;
        valueOfField = "";
      }
    } else if (localName.equals("fldSimple")) {
      fieldIdentifier.append(attributes.getValue("w:instr"));
      insideField = true;
      insideFieldValue = true;
      valueInserted = false;
      insideInstrText = false;

      this.writer.writeCharacters("");
      this.writer.flush();
      this.underlayingOutputStream.write(formatter.getFormatterString(Formatter.FIELD_BEGIN).getBytes("UTF-8"));
      this.underlayingOutputStream.write(fieldIdentifier.toString().getBytes("UTF-8"));
      this.underlayingOutputStream.write(formatter.getFormatterString(Formatter.FIELD_SEPARATE).getBytes("UTF-8"));

      return true;
    } else if (!localName.equals("fldChar") && insideFieldValueContent && valueInserted) {
      return true;
    } else if (localName.equals("name") && insideField) {
      ffDataName = attributes.getValue("w:val");
    } else if (localName.equalsIgnoreCase("checkbox")) {
      fieldType = OpenXMLFieldType.CHECKBOX;
    } else if (localName.equalsIgnoreCase("default") && fieldType == OpenXMLFieldType.CHECKBOX) {
      boolean checked = Boolean.parseBoolean(model.getPropertyAsString(ffDataName));
      String value = (checked) ? "1" : "0";
      if (ffDataName.length() > 0)
        attributes.setValue("w:val", value);
      valueOfField = value;
      valueInserted = true;
    } else if (localName.equals("instrText") && insideField) {
      // field name
      insideInstrText = true;
    } else if (localName.equals("t") && insideFieldValue) {
      // where to put the new value
      insideFieldValueContent = true;
    } else if (localName.equals("tbl")) {
      if (captureBlock && startBlock) {
        if (!showBlock) {
          BlockProcessor blockProcessor = new BlockProcessor(documentKey, this.reader, this.writer, this.underlayingOutputStream);
          blockProcessor.setModel(this.model);
          blockProcessor.setPartial(true);
          blockProcessor.setRepository(repository);
          blockProcessor.setDocumentKey(documentKey);
          blockProcessor.showBlock(showBlock);
          blockProcessor.setNamespceContext(this.getNamespaceContext());
          blockProcessor.setNewIdImages(newIdImages);
          blockProcessor.setOldIdImages(oldIdImages);
          blockProcessor.start();
          startBlock = showBlock = captureBlock = false;
          return true;
        }
        startBlock = showBlock = captureBlock = false;
        return false;
      } else if (captureTable) {
        TableProcessor tableProcessor = new TableProcessor(documentKey, this.reader, this.writer, this.underlayingOutputStream);
        tableProcessor.setCollectionModel(currentCollection);
        tableProcessor.setPartial(true);
        tableProcessor.setRepository(repository);
        tableProcessor.setDocumentKey(this.documentKey);
        tableProcessor.setTableName(this.tableName);
        tableProcessor.setNamespceContext(this.getNamespaceContext());
        tableProcessor.setNewIdImages(newIdImages);
        tableProcessor.setOldIdImages(oldIdImages);
        tableProcessor.start();
        captureTable = false;
        return true;
      }
      return false;
    } else if (localName.equals("_8E03AB25A2E342ea84854A32DEA84BBC")) {
      return true;
    } else if (captureBlock && startBlock) { // Capture block, but the block
                                             // doesn't exists
      // Insert the block from database
      if (showBlock) {
        BlockProcessor blockProcessor = new BlockProcessor(documentKey, this.reader, this.writer, this.underlayingOutputStream);
        blockProcessor.setModel(this.model);
        blockProcessor.setPartial(true);
        blockProcessor.setRepository(repository);
        blockProcessor.setDocumentKey(documentKey);
        blockProcessor.setBlockName(this.blockName);
        blockProcessor.setNamespceContext(this.getNamespaceContext());
        blockProcessor.setNewIdImages(newIdImages);
        blockProcessor.setOldIdImages(oldIdImages);
        blockProcessor.Create();
      }
      startBlock = showBlock = captureBlock = false;
      return false;
    }
    return false;
  }

  protected boolean handleContent(String content) throws IOException, XMLStreamException {
    if (insideFieldValueContent) {
      if (!valueInserted) {
        if (handleTableField()) {
          this.writer.writeCharacters("");
          this.writer.flush();
          this.underlayingOutputStream.write(Formatter.formatString(this.valueOfField, this.styles).asString().getBytes("UTF-8"));
          this.writer.writeEndElement();
          return true;
        } else
          return false;
      } else {
        return true;
      }
    } else {
      if (insideInstrText) {
        fieldIdentifier.append(content);
      }
      return false;
    }
  }

  private boolean handleTableField() {
    String fieldIdentifier = this.fieldIdentifier.toString();
    this.fieldIdentifier = new StringBuilder();

    Matcher mergeFieldValueMatcher = mergeFieldValue.matcher(fieldIdentifier);

    if (mergeFieldValueMatcher.find()) {
      String mergeFieldValueKey = mergeFieldValueMatcher.group(1);
      Matcher tableFieldValueMatcher = tableFieldValue.matcher(mergeFieldValueKey);
      Matcher blockFieldValueMatcher = blockFieldValue.matcher(mergeFieldValueKey);

      if (tableFieldValueMatcher.find()) {
        String key = tableFieldValueMatcher.group(1);
        if (model.isPropertyACollection(key)) {
          currentCollection = model.getPropertyAsCollection(key);
          valueOfField = ""; // Visible value of Table field is empty
          captureTable = true;
          this.tableName = key;
        } else {
          valueOfField = "";
          this.logger.error(String.format("Field %s not found in document.", key));
          captureTable = false;
        }
      } else if (blockFieldValueMatcher.find()) {
        String key = blockFieldValueMatcher.group(1);
        this.blockName = key;
        valueOfField = "";
        captureBlock = true;
        showBlock = false;
        try {
          showBlock = Boolean.parseBoolean(model.getPropertyAsString(key));
        } catch (Exception e) {
        }
      } else if (model.isPropertyAString(mergeFieldValueKey)) {
        valueOfField = model.getPropertyAsString(mergeFieldValueKey);
      } else {
        valueOfField = "";
        this.logger.error(String.format("Field %s not found in document.", mergeFieldValueKey));
      }
      valueInserted = true;
      return true;
    } else {

      Matcher mergeIFFieldValueMatcher = mergeIFFieldValue.matcher(fieldIdentifier);
      if (mergeIFFieldValueMatcher.find() && mergeIFFieldValueMatcher.groupCount() > 2) {
        String field = mergeIFFieldValueMatcher.group(1);
        String valueOfFieldTrue = mergeIFFieldValueMatcher.group(2);
        String valueOfFieldFalse = mergeIFFieldValueMatcher.group(3);

        if (model.isPropertyAString(field)) {
          String opIFSentence = model.getPropertyAsString(field);
          if (opIFSentence.equalsIgnoreCase("true") || opIFSentence.equals("1"))
            valueOfField = valueOfFieldTrue;
          else
            valueOfField = valueOfFieldFalse;
        }
        valueInserted = true;
        return true;
      }
    }

    return false;
  }

  @Override
  protected boolean handleEndElement(String localName) throws XMLStreamException, UnsupportedEncodingException, IOException {
    if (insideFieldValue && localName.equals("rPr")) {
      isInRPRBlock = false;
    } else if (insideDrawing && localName.equals("drawing")) {
      insideDrawing = false;
      finishDrawingInsideSimple = true;
      return false;
    }

    if (localName.equals("fldSimple")) {
      insideFieldValue = insideField = false;
      valueOfField = "";
      if (!finishDrawingInsideSimple)
        this.writer.writeEndElement();
      else
        finishDrawingInsideSimple = false;
      this.writer.flush();
      this.underlayingOutputStream.write(formatter.getFormatterString(Formatter.FIELD_END).getBytes("UTF-8"));
      insideFieldValueContent = false;
      return true;
    }

    if (insideFieldValueContent && valueInserted) {
      return true;
    }

    if (localName.equals("t") && insideFieldValueContent && !valueInserted) {
      handleTableField();
      insideFieldValueContent = false;
    }
    if (localName.equals("t"))
      insideFieldValueContent = false;
    else if (localName.equals("p") && captureBlock)
      startBlock = true;

    if (insideField && fieldType == OpenXMLFieldType.CHECKBOX && valueInserted) {
      fieldType = OpenXMLFieldType.UNKNOW;
      ffDataName = "";
      insideFieldValue = insideField = valueInserted = false;
      valueOfField = "";
    }

    return localName.equals("_8E03AB25A2E342ea84854A32DEA84BBC");
  }

}