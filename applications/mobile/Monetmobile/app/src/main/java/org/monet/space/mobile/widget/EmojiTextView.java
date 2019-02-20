package org.monet.space.mobile.widget;

import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.AttributeSet;
import android.widget.TextView;

import org.monet.space.mobile.R;

public class EmojiTextView extends TextView {

  public final int[]                      images             = new int[] { R.drawable.emo_im_happy, R.drawable.emo_im_sad, R.drawable.emo_im_winking, R.drawable.emo_im_tongue_sticking_out, R.drawable.emo_im_surprised, R.drawable.emo_im_kissing, R.drawable.emo_im_yelling, R.drawable.emo_im_cool, R.drawable.emo_im_money_mouth, R.drawable.emo_im_foot_in_mouth, R.drawable.emo_im_embarrassed, R.drawable.emo_im_angel, R.drawable.emo_im_undecided, R.drawable.emo_im_crying, R.drawable.emo_im_lips_are_sealed, R.drawable.emo_im_laughing, R.drawable.emo_im_wtf };
  public final String[]                   _strEmoticonLabels = new String[] { "Happy", "Sad", "Winking", "Tongue sticking out", "Surprised", "Kissing", "Yelling", "Cool", "Money Mouth", "Foot in mouth", "Embarrased", "Angel", "Undecided", "Crying", "Lips are sealed", "Laughing", "Confused" };
  public final String[]                   emojis             = new String[] { ":-)", ":-(", ";-)", ":-P", "=-O", ":-*", ":O", "B-)", ":-$", ":-!", ":-[", "O:-|", ":-\\", ":'(", ":-X", ":-D", "o_O" };

  private static HashMap<String, Integer> emojiMap;

  public EmojiTextView(Context context) {
    super(context);
    init();
  }

  public EmojiTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public EmojiTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private synchronized void init() {
    if(emojiMap == null) {
      emojiMap = new HashMap<String, Integer>();
      for (int i = 0; i < images.length; i++) {
        emojiMap.put(emojis[i], images[i]);
      }
    }
  }

  public void setEmojiText(String text) {
    String emojiText = text != null ? convertTag(text) : "";
    CharSequence spanned = Html.fromHtml(emojiText, emojiGetter, null);
    setText(spanned);
  }

  public static String convertTag(String str) {
    return str.replaceAll("<", "&lt;").replaceAll("O:-\\)", "<img src=\"O:-|\"/>").replaceAll(":-\\)", "<img src=\":-)\"/>").replaceAll(":-\\(", "<img src=\":-(\"/>").replaceAll(";-\\)", "<img src=\";-)\"/>").replaceAll(":-P", "<img src=\":-P\"/>").replaceAll("=-O", "<img src=\"=-O\"/>").replaceAll(":-\\*", "<img src=\":-*\"/>").replaceAll(":O", "<img src=\":O\"/>").replaceAll("B-\\)", "<img src=\"B-)\"/>").replaceAll(":-\\$", "<img src=\":-&#36;\"/>").replaceAll(":-!", "<img src=\":-!\"/>").replaceAll(":-\\[", "<img src=\":-[\"/>").replaceAll(":-\\\\", "<img src=\":-&#92;\"/>").replaceAll(":'\\(", "<img src=\":'(\"/>").replaceAll(":-X", "<img src=\":-X\"/>").replaceAll(":-D", "<img src=\":-D\"/>").replaceAll("o_O", "<img src=\"o_O\"/>");
  }

  private ImageGetter emojiGetter = new ImageGetter() {
                                    public Drawable getDrawable(String source) {
                                      int id = emojiMap.get(source);

                                      Drawable emoji = getResources().getDrawable(id);
                                      int w = (int) (emoji.getIntrinsicWidth() * 1.25);
                                      int h = (int) (emoji.getIntrinsicHeight() * 1.25);
                                      emoji.setBounds(0, 0, w, h);
                                      return emoji;
                                    }
                                  };
}
