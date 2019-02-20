package org.monet.space.mobile.helpers;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontUtils {

  public static interface FontTypes {
    public static String REGULAR      = "Regular";
    public static String ITALIC       = "Italic";
    public static String BOLD         = "Bold";
    public static String BOLD_ITALIC  = "BoldItalic";
    public static String LIGHT        = "Light";
    public static String LIGHT_ITALIC = "LightItalic";
    public static String THIN         = "Thin";
    public static String THIN_ITALIC  = "ThinItalic";
  }

  /**
   * map of font types to font paths in assets
   */

  private static Map<String, String>   fontMap       = new HashMap<String, String>();

  static {
    fontMap.put(FontTypes.REGULAR, "fonts/Roboto-Regular.ttf");
    fontMap.put(FontTypes.ITALIC, "fonts/Roboto-Italic.ttf");
    fontMap.put(FontTypes.BOLD, "fonts/Roboto-Bold.ttf");
    fontMap.put(FontTypes.BOLD_ITALIC, "fonts/Roboto-BoldItalic.ttf");
    fontMap.put(FontTypes.LIGHT, "fonts/Roboto-Light.ttf");
    fontMap.put(FontTypes.LIGHT_ITALIC, "fonts/Roboto-LightItalic.ttf");
    fontMap.put(FontTypes.THIN, "fonts/Roboto-Thin.ttf");
    fontMap.put(FontTypes.THIN_ITALIC, "fonts/Roboto-ThinItalic.ttf");
  }

  /* cache for loaded Roboto typefaces */

  private static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();

  /**
   * Creates Roboto typeface and puts it into cache
   * 
   * @param context
   * @param fontType
   * @return
   */

  private static Typeface getRobotoTypeface(Context context, String fontType) {
    String fontPath = fontMap.get(fontType);

    if (!typefaceCache.containsKey(fontType)) {
      typefaceCache.put(fontType, Typeface.createFromAsset(context.getAssets(), fontPath));
    }

    return typefaceCache.get(fontType);
  }

  /**
   * Gets roboto typeface according to passed typeface style settings. Will get
   * Roboto-Bold for Typeface.BOLD etc
   * 
   * @param context
   * @param typefaceStyle
   * @return
   */

  private static Typeface getRobotoTypeface(Context context, String fontName, Typeface originalTypeface) {
    String robotoFontType = fontName != null ? fontName : ""; // default Regular
                                                              // Roboto font
    if (originalTypeface != null) {
      int style = originalTypeface.getStyle();
      switch (style) {
        case Typeface.BOLD:
          robotoFontType += FontTypes.BOLD;
          break;
        case Typeface.ITALIC:
          robotoFontType += FontTypes.ITALIC;
          break;
        case Typeface.BOLD_ITALIC:
          robotoFontType += FontTypes.BOLD_ITALIC;
          break;
        case Typeface.NORMAL:
          if (robotoFontType.length() == 0)
            robotoFontType = FontTypes.REGULAR;
          break;
      }
    } else if (robotoFontType.length() == 0) {
      robotoFontType = FontTypes.REGULAR;
    }

    return getRobotoTypeface(context, robotoFontType);
  }

  /**
   * Walks ViewGroups, finds TextViews and applies Typefaces taking styling in
   * consideration
   * 
   * @param context
   *          - to reach assets
   * @param view
   *          - root view to apply typeface to
   */
  public static void setRobotoFont(Context context, View view) {
    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        setRobotoFont(context, ((ViewGroup) view).getChildAt(i));
      }
    } else if (view instanceof TextView) {
      Typeface currentTypeface = ((TextView) view).getTypeface();
      String fontName = view.getTag() instanceof String ? (String) view.getTag() : null;
      ((TextView) view).setTypeface(getRobotoTypeface(context, fontName, currentTypeface));
    }
  }
}
