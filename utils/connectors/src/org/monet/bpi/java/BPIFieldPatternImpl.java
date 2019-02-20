package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Indicator;
import org.monet.bpi.BPIFieldPattern;
import org.monet.v2.metamodel.PatternDeclaration;
import org.monet.v2.metamodel.PatternFieldDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BPIFieldPatternImpl extends BPIFieldImpl<String> implements BPIFieldPattern {

  @Override
  public String get() {
    return this.attribute.getIndicatorValue(Indicator.VALUE);
  }

  @Override
  public void set(String value) {
    int pos = 0;
    boolean matchPattern = false;
    PatternFieldDeclaration fieldDeclaration = (PatternFieldDeclaration)this.fieldDeclaration;
    ArrayList<PatternDeclaration> patternDeclarationList = fieldDeclaration.getPatternDeclarationList();

    while ((!matchPattern) && (pos < patternDeclarationList.size())) {
      PatternDeclaration patternDeclaration = patternDeclarationList.get(pos);
      ArrayList<PatternDeclaration.Meta> metaList = patternDeclaration.getMetaList();
      Pattern pattern = Pattern.compile(patternDeclaration.getRegexp());
      Matcher matcher = pattern.matcher(value);
      
      if (matcher.matches()) {
        for (int jPos=1; jPos<=matcher.groupCount(); jPos++) {
          PatternDeclaration.Meta meta = metaList.get(jPos-1);
          this.setIndicatorValue(meta.getIndicator(), matcher.group(jPos));
          matchPattern = true;
        }
      }
      
      pos++;
    }

    this.setIndicatorValue(Indicator.VALUE, value);
  }

  @Override
  public boolean equals(Object value) {
    if(value instanceof String)
      return this.get().equals(value);
    else
      return false;
  }

  @Override
  public void clear() {
    this.set("");
  }
  
  @Override
  public String getGroup(String name) {
    return this.getIndicatorValue(name);
  }

  @Override
  public String getGroup(int index) {
    PatternFieldDeclaration fieldDeclaration = (PatternFieldDeclaration) this.fieldDeclaration;

    for (PatternDeclaration patternDefinition : fieldDeclaration.getPatternDeclarationList()) {
      PatternDeclaration.Meta metaDefinition = patternDefinition.getMetaList().get(index);
      if (metaDefinition != null)
        return this.getIndicatorValue(metaDefinition.getIndicator());
    }

    return "";
  }

  @Override
  public Map<String, String> getGroups() {
    Map<String, String> result = new HashMap<>();
    PatternFieldDeclaration patternFieldDeclaration = (PatternFieldDeclaration) this.fieldDeclaration;

    for (PatternDeclaration patternDefinition : patternFieldDeclaration.getPatternDeclarationList())
      fillPatternGroups(patternDefinition, result);

    return result;
  }

  private void fillPatternGroups(PatternDeclaration patternDefinition, Map result) {
    for (PatternDeclaration.Meta metaDefinition : patternDefinition.getMetaList())
      fillPatternGroup(metaDefinition, result);
  }

  private void fillPatternGroup(PatternDeclaration.Meta metaDefinition, Map result) {
    result.put(metaDefinition.getIndicator(), this.getIndicatorValue(metaDefinition.getIndicator()));
  }

  @Override
  public boolean isValid() {
    PatternFieldDeclaration fieldDeclaration = (PatternFieldDeclaration) this.fieldDeclaration;
    ArrayList<PatternDeclaration> patternDefinitionList = fieldDeclaration.getPatternDeclarationList();

    if (patternDefinitionList == null || patternDefinitionList.size() <= 0)
      return true;

    return checker(patternDefinitionList).check(get());
  }

  private Checker checker(final ArrayList<PatternDeclaration> patternDefinitionList) {
    return new Checker() {
      @Override
      public boolean check(String value) {

        for (PatternDeclaration patternDefinition : patternDefinitionList) {
          if (value.matches(patternDefinition.getRegexp()))
            return true;
        }

        return false;
      }
    };
  }

  private interface Checker {
    boolean check(String value);
  }

}