package org.monet.editor.diffmatcpatch.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

import org.monet.editor.diffmatcpatch.helper.DiffHelper;

public class Patch {
    public LinkedList<Diff> diffs;
    public int start1;
    public int start2;
    public int length1;
    public int length2;

    /**
     * Constructor.  Initializes with an empty list of diffs.
     */
    public Patch() {
      this.diffs = new LinkedList<Diff>();
    }

    /**
     * Emmulate GNU diff's format.
     * Header: @@ -382,8 +481,9 @@
     * Indicies are printed as 1-based, not 0-based.
     * @return The GNU diff string.
     */
    public String toString() {
      String coords1, coords2;
      if (this.length1 == 0) {
        coords1 = this.start1 + ",0";
      } else if (this.length1 == 1) {
        coords1 = Integer.toString(this.start1 + 1);
      } else {
        coords1 = (this.start1 + 1) + "," + this.length1;
      }
      if (this.length2 == 0) {
        coords2 = this.start2 + ",0";
      } else if (this.length2 == 1) {
        coords2 = Integer.toString(this.start2 + 1);
      } else {
        coords2 = (this.start2 + 1) + "," + this.length2;
      }
      StringBuilder text = new StringBuilder();
      text.append("@@ -").append(coords1).append(" +").append(coords2)
          .append(" @@\n");
      // Escape the body of the patch with %xx notation.
      for (Diff aDiff : this.diffs) {
        switch (aDiff.operation) {
        case INSERT:
          text.append('+');
          break;
        case DELETE:
          text.append('-');
          break;
        case EQUAL:
          text.append(' ');
          break;
        }
        try {
          text.append(URLEncoder.encode(aDiff.text, "UTF-8").replace('+', ' '))
              .append("\n");
        } catch (UnsupportedEncodingException e) {
          // Not likely on modern system.
          throw new Error("This system does not support UTF-8.", e);
	        }
	      }
	      return DiffHelper.unescapeForEncodeUriCompatability(text.toString());
    }
}
