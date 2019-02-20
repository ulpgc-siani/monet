package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class SectionFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(SectionFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            
                      BooleanFieldDeclarationSemanticChecks booleanFieldDeclaration = new BooleanFieldDeclarationSemanticChecks();
      booleanFieldDeclaration.setProblems(this.getProblems());
      booleanFieldDeclaration.setModule(this.getModule());
      for(BooleanFieldDeclaration include : metaitem.getBooleanFieldDeclarationList()) {
        booleanFieldDeclaration.check(include);
      }
                  CheckFieldDeclarationSemanticChecks checkFieldDeclaration = new CheckFieldDeclarationSemanticChecks();
      checkFieldDeclaration.setProblems(this.getProblems());
      checkFieldDeclaration.setModule(this.getModule());
      for(CheckFieldDeclaration include : metaitem.getCheckFieldDeclarationList()) {
        checkFieldDeclaration.check(include);
      }
                    DateFieldDeclarationSemanticChecks dateFieldDeclaration = new DateFieldDeclarationSemanticChecks();
      dateFieldDeclaration.setProblems(this.getProblems());
      dateFieldDeclaration.setModule(this.getModule());
      for(DateFieldDeclaration include : metaitem.getDateFieldDeclarationList()) {
        dateFieldDeclaration.check(include);
      }
                  FileFieldDeclarationSemanticChecks fileFieldDeclaration = new FileFieldDeclarationSemanticChecks();
      fileFieldDeclaration.setProblems(this.getProblems());
      fileFieldDeclaration.setModule(this.getModule());
      for(FileFieldDeclaration include : metaitem.getFileFieldDeclarationList()) {
        fileFieldDeclaration.check(include);
      }
                  LinkFieldDeclarationSemanticChecks linkFieldDeclaration = new LinkFieldDeclarationSemanticChecks();
      linkFieldDeclaration.setProblems(this.getProblems());
      linkFieldDeclaration.setModule(this.getModule());
      for(LinkFieldDeclaration include : metaitem.getLinkFieldDeclarationList()) {
        linkFieldDeclaration.check(include);
      }
                  LocationFieldDeclarationSemanticChecks locationFieldDeclaration = new LocationFieldDeclarationSemanticChecks();
      locationFieldDeclaration.setProblems(this.getProblems());
      locationFieldDeclaration.setModule(this.getModule());
      for(LocationFieldDeclaration include : metaitem.getLocationFieldDeclarationList()) {
        locationFieldDeclaration.check(include);
      }
                  MemoFieldDeclarationSemanticChecks memoFieldDeclaration = new MemoFieldDeclarationSemanticChecks();
      memoFieldDeclaration.setProblems(this.getProblems());
      memoFieldDeclaration.setModule(this.getModule());
      for(MemoFieldDeclaration include : metaitem.getMemoFieldDeclarationList()) {
        memoFieldDeclaration.check(include);
      }
                  NodeFieldDeclarationSemanticChecks nodeFieldDeclaration = new NodeFieldDeclarationSemanticChecks();
      nodeFieldDeclaration.setProblems(this.getProblems());
      nodeFieldDeclaration.setModule(this.getModule());
      for(NodeFieldDeclaration include : metaitem.getNodeFieldDeclarationList()) {
        nodeFieldDeclaration.check(include);
      }
                  NumberFieldDeclarationSemanticChecks numberFieldDeclaration = new NumberFieldDeclarationSemanticChecks();
      numberFieldDeclaration.setProblems(this.getProblems());
      numberFieldDeclaration.setModule(this.getModule());
      for(NumberFieldDeclaration include : metaitem.getNumberFieldDeclarationList()) {
        numberFieldDeclaration.check(include);
      }
                  PatternFieldDeclarationSemanticChecks patternFieldDeclaration = new PatternFieldDeclarationSemanticChecks();
      patternFieldDeclaration.setProblems(this.getProblems());
      patternFieldDeclaration.setModule(this.getModule());
      for(PatternFieldDeclaration include : metaitem.getPatternFieldDeclarationList()) {
        patternFieldDeclaration.check(include);
      }
                  PictureFieldDeclarationSemanticChecks pictureFieldDeclaration = new PictureFieldDeclarationSemanticChecks();
      pictureFieldDeclaration.setProblems(this.getProblems());
      pictureFieldDeclaration.setModule(this.getModule());
      for(PictureFieldDeclaration include : metaitem.getPictureFieldDeclarationList()) {
        pictureFieldDeclaration.check(include);
      }
                  SectionFieldDeclarationSemanticChecks sectionFieldDeclaration = new SectionFieldDeclarationSemanticChecks();
      sectionFieldDeclaration.setProblems(this.getProblems());
      sectionFieldDeclaration.setModule(this.getModule());
      for(SectionFieldDeclaration include : metaitem.getSectionFieldDeclarationList()) {
        sectionFieldDeclaration.check(include);
      }
                  SelectFieldDeclarationSemanticChecks selectFieldDeclaration = new SelectFieldDeclarationSemanticChecks();
      selectFieldDeclaration.setProblems(this.getProblems());
      selectFieldDeclaration.setModule(this.getModule());
      for(SelectFieldDeclaration include : metaitem.getSelectFieldDeclarationList()) {
        selectFieldDeclaration.check(include);
      }
                  SerialFieldDeclarationSemanticChecks serialFieldDeclaration = new SerialFieldDeclarationSemanticChecks();
      serialFieldDeclaration.setProblems(this.getProblems());
      serialFieldDeclaration.setModule(this.getModule());
      for(SerialFieldDeclaration include : metaitem.getSerialFieldDeclarationList()) {
        serialFieldDeclaration.check(include);
      }
                  TextFieldDeclarationSemanticChecks textFieldDeclaration = new TextFieldDeclarationSemanticChecks();
      textFieldDeclaration.setProblems(this.getProblems());
      textFieldDeclaration.setModule(this.getModule());
      for(TextFieldDeclaration include : metaitem.getTextFieldDeclarationList()) {
        textFieldDeclaration.check(include);
      }
                  SectionFieldViewDeclarationSemanticChecks sectionFieldViewDeclaration = new SectionFieldViewDeclarationSemanticChecks();
      sectionFieldViewDeclaration.setProblems(this.getProblems());
      sectionFieldViewDeclaration.setModule(this.getModule());
      sectionFieldViewDeclaration.check(metaitem.getSectionFieldViewDeclaration());
        }
}


















