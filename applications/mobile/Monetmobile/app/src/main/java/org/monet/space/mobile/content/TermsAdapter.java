package org.monet.space.mobile.content;

import java.util.List;

import org.monet.mobile.model.TaskDefinition.Step.Edit.Term;
import org.monet.space.mobile.R;

import android.content.Context;
import android.widget.ArrayAdapter;

public class TermsAdapter extends ArrayAdapter<Term> {

  public TermsAdapter(Context context, List<Term> objects) {
    super(context, R.layout.simple_list_item, android.R.id.text1, objects);
  }

}
