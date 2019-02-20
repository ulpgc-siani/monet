package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class SectionFieldDeclarationSyncDDBB extends MultipleableFieldDeclarationSyncDDBB {

  public void sync(SectionFieldDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

              BooleanFieldDeclarationSyncDDBB booleanFieldDeclaration = new BooleanFieldDeclarationSyncDDBB();
      booleanFieldDeclaration.setModule(this.getModule());
      for(BooleanFieldDeclaration include : metaitem.getBooleanFieldDeclarationList()) {
        booleanFieldDeclaration.sync(include);
      }
                  CheckFieldDeclarationSyncDDBB checkFieldDeclaration = new CheckFieldDeclarationSyncDDBB();
      checkFieldDeclaration.setModule(this.getModule());
      for(CheckFieldDeclaration include : metaitem.getCheckFieldDeclarationList()) {
        checkFieldDeclaration.sync(include);
      }
                    DateFieldDeclarationSyncDDBB dateFieldDeclaration = new DateFieldDeclarationSyncDDBB();
      dateFieldDeclaration.setModule(this.getModule());
      for(DateFieldDeclaration include : metaitem.getDateFieldDeclarationList()) {
        dateFieldDeclaration.sync(include);
      }
                  FileFieldDeclarationSyncDDBB fileFieldDeclaration = new FileFieldDeclarationSyncDDBB();
      fileFieldDeclaration.setModule(this.getModule());
      for(FileFieldDeclaration include : metaitem.getFileFieldDeclarationList()) {
        fileFieldDeclaration.sync(include);
      }
                  LinkFieldDeclarationSyncDDBB linkFieldDeclaration = new LinkFieldDeclarationSyncDDBB();
      linkFieldDeclaration.setModule(this.getModule());
      for(LinkFieldDeclaration include : metaitem.getLinkFieldDeclarationList()) {
        linkFieldDeclaration.sync(include);
      }
                  LocationFieldDeclarationSyncDDBB locationFieldDeclaration = new LocationFieldDeclarationSyncDDBB();
      locationFieldDeclaration.setModule(this.getModule());
      for(LocationFieldDeclaration include : metaitem.getLocationFieldDeclarationList()) {
        locationFieldDeclaration.sync(include);
      }
                  MemoFieldDeclarationSyncDDBB memoFieldDeclaration = new MemoFieldDeclarationSyncDDBB();
      memoFieldDeclaration.setModule(this.getModule());
      for(MemoFieldDeclaration include : metaitem.getMemoFieldDeclarationList()) {
        memoFieldDeclaration.sync(include);
      }
                  NodeFieldDeclarationSyncDDBB nodeFieldDeclaration = new NodeFieldDeclarationSyncDDBB();
      nodeFieldDeclaration.setModule(this.getModule());
      for(NodeFieldDeclaration include : metaitem.getNodeFieldDeclarationList()) {
        nodeFieldDeclaration.sync(include);
      }
                  NumberFieldDeclarationSyncDDBB numberFieldDeclaration = new NumberFieldDeclarationSyncDDBB();
      numberFieldDeclaration.setModule(this.getModule());
      for(NumberFieldDeclaration include : metaitem.getNumberFieldDeclarationList()) {
        numberFieldDeclaration.sync(include);
      }
                  PatternFieldDeclarationSyncDDBB patternFieldDeclaration = new PatternFieldDeclarationSyncDDBB();
      patternFieldDeclaration.setModule(this.getModule());
      for(PatternFieldDeclaration include : metaitem.getPatternFieldDeclarationList()) {
        patternFieldDeclaration.sync(include);
      }
                  PictureFieldDeclarationSyncDDBB pictureFieldDeclaration = new PictureFieldDeclarationSyncDDBB();
      pictureFieldDeclaration.setModule(this.getModule());
      for(PictureFieldDeclaration include : metaitem.getPictureFieldDeclarationList()) {
        pictureFieldDeclaration.sync(include);
      }
                  SectionFieldDeclarationSyncDDBB sectionFieldDeclaration = new SectionFieldDeclarationSyncDDBB();
      sectionFieldDeclaration.setModule(this.getModule());
      for(SectionFieldDeclaration include : metaitem.getSectionFieldDeclarationList()) {
        sectionFieldDeclaration.sync(include);
      }
                  SelectFieldDeclarationSyncDDBB selectFieldDeclaration = new SelectFieldDeclarationSyncDDBB();
      selectFieldDeclaration.setModule(this.getModule());
      for(SelectFieldDeclaration include : metaitem.getSelectFieldDeclarationList()) {
        selectFieldDeclaration.sync(include);
      }
                  SerialFieldDeclarationSyncDDBB serialFieldDeclaration = new SerialFieldDeclarationSyncDDBB();
      serialFieldDeclaration.setModule(this.getModule());
      for(SerialFieldDeclaration include : metaitem.getSerialFieldDeclarationList()) {
        serialFieldDeclaration.sync(include);
      }
                  TextFieldDeclarationSyncDDBB textFieldDeclaration = new TextFieldDeclarationSyncDDBB();
      textFieldDeclaration.setModule(this.getModule());
      for(TextFieldDeclaration include : metaitem.getTextFieldDeclarationList()) {
        textFieldDeclaration.sync(include);
      }
                  SectionFieldViewDeclarationSyncDDBB sectionFieldViewDeclaration = new SectionFieldViewDeclarationSyncDDBB();
      sectionFieldViewDeclaration.setModule(this.getModule());
      sectionFieldViewDeclaration.sync(metaitem.getSectionFieldViewDeclaration());
        }
}


















