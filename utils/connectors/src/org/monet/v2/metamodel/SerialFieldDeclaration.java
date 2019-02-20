package org.monet.v2.metamodel;


import org.simpleframework.xml.core.Commit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// SerialFieldDeclaration
// Declaración que se utiliza para modelar un	campo serial

public class SerialFieldDeclaration extends SerialFieldDeclarationBase {

  private String  REGEXP_SEQUENCE = "#S:([^#]*)#";
  private String  REGEXP_YEAR     = "#Y:([^#]*)#";

  private Matcher sequenceMatcher;
  private Matcher yearMatcher;

  @Commit
  public void commit() {
    this.parseFormat();
  }

  private void parseFormat() {
    Matcher sequenceMatcher = Pattern.compile(REGEXP_SEQUENCE).matcher(this._format._pattern);
    Matcher yearMatcher = Pattern.compile(REGEXP_YEAR).matcher(this._format._pattern);

    if (sequenceMatcher.find())
      this.sequenceMatcher = sequenceMatcher;
    else
      this.sequenceMatcher = null;
    if (yearMatcher.find())
      this.yearMatcher = yearMatcher;
    else
      this.yearMatcher = null;
  }

}