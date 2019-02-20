package org.monet.space.mobile.jtm.editors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.monet.space.mobile.fragment.PickPositionFragment;
import org.monet.space.mobile.helpers.OpenGLHelper;
import org.monet.space.mobile.model.schema.Location;
import org.monet.space.mobile.model.schema.Schema;

import java.util.Date;
import java.util.List;

import org.monet.space.mobile.R;

public class EditPositionHolder extends EditHolder<Location> implements OnClickListener {

  private View currentEditor;

  @Override
  protected View createEditor(Location value, ViewGroup container) {
    View editorView = inflater.inflate(R.layout.edit_position, container, false);

    ImageButton pickButton = (ImageButton) editorView.findViewById(R.id.pick_position);
    if(!this.isReadOnly())
      pickButton.setOnClickListener(this);

    this.updateUI(editorView, value);

    return editorView;
  }

  @Override
  protected Location onLoadValue(Schema schema) {
    return schema.getLocation(edit.name);
  }

  @Override
  protected List<Location> onLoadValueArray(Schema schema) {
    return schema.getLocationList(edit.name);
  }

  @Override
  protected void onSaveValue(Schema schema, Location value) {
    if (this.isReadOnly()) return;

    schema.putLocation(edit.name, value);
  }

  @Override
  protected void onSaveValueArray(Schema schema, List<Location> value) {
    if (!isReadOnly())
        schema.putLocationList(edit.name, value);
  }

  @Override
  protected void clearEditor(View editor) {
    if (this.isReadOnly()) return;

    this.updateUI(editor, null);
  }

  @Override
  protected Location onExtractItem(View editor) {
    TextView locationPosition = (TextView) editor.findViewById(R.id.field_location_position);
    return (Location) locationPosition.getTag();
  }

  private void updateUI(View editorView, Location value) {
    TextView locationLabel = (TextView) editorView.findViewById(R.id.field_location_label);
    TextView locationPosition = (TextView) editorView.findViewById(R.id.field_location_position);

    if (value != null) {
      locationLabel.setText(value.label);
      locationPosition.setVisibility(View.VISIBLE);
      locationPosition.setText(locationPosition.getContext().getString(R.string.default_point_description, value.getLatitude(), value.getLongitude()));
    } else {
      locationLabel.setText(getContext().getString(R.string.no_location_label));
      locationPosition.setVisibility(View.GONE);
    }
    locationPosition.setTag(value);
  };

  @Override
  public void onActivityForResult(int requestCode, int resultCode, Intent intent) {
    if (resultCode == Activity.RESULT_OK) {
      Location location = new Location();
      double latitude = intent.getDoubleExtra(PickPositionFragment.LATITUDE, Double.MIN_VALUE);
      double longitude = intent.getDoubleExtra(PickPositionFragment.LONGITUDE, Double.MIN_VALUE);
      location.setLatLon(latitude, longitude, new Date());

      location.label = intent.getStringExtra(PickPositionFragment.LABEL);
      location.timestamp = new Date();
      this.updateUI(this.currentEditor, location);
      this.checkLastEditorIsEmpty();
      this.currentEditor.findViewById(R.id.pick_position).requestFocus();
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.pick_position:
        if (!OpenGLHelper.supportOpenGLES20(this.getContext())) {
          Toast.makeText(this.getContext(), R.string.maps_not_supported, Toast.LENGTH_LONG).show();
          return;
        }
        this.currentEditor = (View) v.getParent();

        Location location = onExtractItem(currentEditor);

        Intent intent = new Intent("monet.action.PICK_POSITION");
        if (location != null) {
          intent.putExtra(PickPositionFragment.LABEL, location.label);
          intent.putExtra(PickPositionFragment.LATITUDE, location.getLatitude());
          intent.putExtra(PickPositionFragment.LONGITUDE, location.getLongitude());
        }
        this.activityManager.startActivityForResult(intent, 0, this);
        break;

      default:
        super.onClick(v);
        break;
    }

  }

  @Override
  public void onRestoreInstanceState(Bundle holderBundle) {
    if(holderBundle.containsKey("EDITOR_ID"))
      this.currentEditor = this.getEditor(holderBundle.getInt("EDITOR_ID"));
  }

  @Override
  public void onSaveInstanceState(Bundle holderBundle) {
    if(this.currentEditor != null) {
      int id = ((LinearLayout)this.currentEditor.getParent()).indexOfChild((View)this.currentEditor);
      holderBundle.putInt("EDITOR_ID", id);
    }
  }
}
