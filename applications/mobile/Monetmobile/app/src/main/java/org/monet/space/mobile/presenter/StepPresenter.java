package org.monet.space.mobile.presenter;

import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.zxing.integration.android.IntentIntegrator;

import org.monet.mobile.model.TaskDefinition;
import org.monet.mobile.model.TaskDefinition.Step;
import org.monet.mobile.model.TaskDefinition.Step.Edit;
import org.monet.space.mobile.R;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.events.CapturingDataFinishedEvent;
import org.monet.space.mobile.events.CapturingDataStartedEvent;
import org.monet.space.mobile.events.FinishLoadingEvent;
import org.monet.space.mobile.events.StartLoadingEvent;
import org.monet.space.mobile.fragment.StepFragment;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.helpers.LocationHelper;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.helpers.RouterHelper;
import org.monet.space.mobile.jtm.State;
import org.monet.space.mobile.jtm.editors.EditHolder;
import org.monet.space.mobile.jtm.editors.EditHolder.EditorValueManager;
import org.monet.space.mobile.jtm.editors.EditorFactory;
import org.monet.space.mobile.model.Coordinate;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.model.schema.Location;
import org.monet.space.mobile.model.schema.Schema;
import org.monet.space.mobile.model.schema.Term;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.mvp.content.SimpleDataLoader;
import org.monet.space.mobile.view.StepView;
import org.monet.space.mobile.view.validator.FormValidator;
import roboguice.util.SafeAsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StepPresenter extends Presenter<StepView, Void> implements LoaderCallbacks<State>, EditorValueManager {

    private final List<EditHolder<?>> editHolders = new ArrayList<>();

    @Inject
    Repository repository;
    @Inject
    private EditorFactory editorFactory;

    @Inject
    private FormValidator validator;

    private Handler handler = new Handler();
    private long taskId;
    private boolean readOnly;
    private State state = new State();
    private State previousState;

    public void initialize(Bundle savedInstanceState) {
        Bundle args = getArgs();
        taskId = args.getLong(RouterHelper.ID);
        readOnly = args.getBoolean(RouterHelper.READONLY);
        if (args.getBoolean(RouterHelper.RESET, false))
            repository.updateTaskStep(this.taskId, 0, 0);

        getLoaderManager().restartLoader(0, savedInstanceState, this);
    }

    public void destroy() {
        for (EditHolder<?> holder : view.getEditHolders().values())
            holder.onDestroy();
    }

    public void pause() {
        if (readOnly) return;

        saveToSchema();

        handler.post(new Runnable() {

            @Override
            public void run() {
                File file = LocalStorage.getTaskResultSchemaFile(context, String.valueOf(taskId));
                updateTaskLabel();

                Schema.toFile(state.Schema, state.Definition, file);

                MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
            }
        });
    }

    private void saveToSchema() {
        for (EditHolder<?> holder : this.view.getEditHolders().values()) {

            holder.onSave(this.state.CurrentStepSchema);
        }
    }

    private void updateTaskLabel() {
        TaskDetails task = this.repository.getTaskDetails(this.taskId);
        long definitionId = this.repository.getDefinitionId(task.sourceId, task.code);
        TaskDefinition definition = this.repository.getDefinition(definitionId);
        String label = definition.label;

        String[] parts = label.split("\\|");
        if (parts.length <= 1) return;

        String format = parts[1];

        String[] values = new String[parts.length - 2];
        for (int i = 2; i < parts.length; i++) {
            String field = parts[i];
            values[i - 2] = this.getFieldValue(field);
        }

        if (areAllEmpty(values))
            return;

        String taskLabel = String.format(format, (Object[]) values);
        this.repository.updateTaskLabel(this.taskId, taskLabel);
    }

    public String getFieldValue(String name) {
        String result = this.state.Schema.calculateValue(name);

        if ((result == null) || (result == "[]"))
            return "";
        return result;
    }

    public boolean areAllEmpty(String[] values) {
        for (String value : values)
            if (!value.equals("")) return false;
        return true;
    }

    public void previous() {
        state.goBack();
        repository.updateTaskStep(taskId, state.CurrentStepIndex, state.MultipleStepIteration);
        routerHelper.goToTaskStep(taskId, readOnly);
        finish();
    }

    public void next() {
        state.goNext();
        repository.updateTaskStep(taskId, state.CurrentStepIndex, state.MultipleStepIteration);
        routerHelper.goToTaskStep(taskId, readOnly);
        finish();
    }


    public void finalizeTask() {

        if (this.readOnly) {
            finish();
        } else {
            new SafeAsyncTask<Void>() {
                @Override
                public Void call() throws Exception {
                    repository.updateTaskToFinishState(taskId, state.CurrentStepIndex + 1, new Date());
                    return null;
                }

                @Override
                protected void onSuccess(Void t) throws Exception {
                    super.onSuccess(t);
                    Schema schema= Schema.fromFile(LocalStorage.getTaskResultSchemaFile(context, String.valueOf(taskId)), state.Definition);
                    finish();

                }
            }.execute();
        }
    }

    public void saveQrValue(EditHolder holder, Term value){
        state.CurrentStepSchema.putTerm(holder.getName(), value);
        updateValues(holder);

    }

    public void saveQrValue(EditHolder holder, String value){
        state.CurrentStepSchema.putText(holder.getName(), value);
        updateValues(holder);
    }

    public void saveQrValue(EditHolder holder, Boolean value){
        state.CurrentStepSchema.putBoolean(holder.getName(), value);
        updateValues(holder);
    }

    private void updateValues(EditHolder holder){
        holder.loadQrMode(state.CurrentStepSchema);
        saveToSchema();
    }

    @Override
    public Loader<State> onCreateLoader(int id, Bundle args) {
        BusProvider.get().post(new StartLoadingEvent());
        return new SimpleDataLoader<State>(this.context, args) {

            private boolean isMultipleEmpty(Step.Edit.Type type, Schema currentSchema, String name){
                if (type.name().equals("TEXT") && currentSchema.getTextList(name).size() == 0) return true;
                if (type.name().equals("DATE") && currentSchema.getDateList(name).size() == 0) return true;
                if (type.name().equals("NUMBER") && currentSchema.getNumberList(name).size() == 0) return true;
                if (type.name().equals("MEMO") && currentSchema.getTextList(name).size() == 0) return true;
                if (type.name().equals("BOOLEAN") && currentSchema.getBooleanList(name).size() == 0) return true;
                //if (type.name().equals("CHECK") && currentSchema.getList(name).size() == 0) state.goBack();
                if (type.name().equals("SELECT") && currentSchema.getTermList(name).size() == 0) return true;
                if (type.name().equals("PICTURE") && currentSchema.getPictureList(name).size() == 0) return true;
                if (type.name().equals("PICTURE_HAND") && currentSchema.getPictureList(name).size() == 0) return true;
                if (type.name().equals("VIDEO") && currentSchema.getVideoList(name).size() == 0) return true;
                return false;
            }


            @Override
            public State loadInBackground() {
                try {
                    TaskDetails task = repository.getTaskDetails(taskId);
                    state.taskLabel = task.label;
                    state.SourceId = task.sourceId;
                    state.TaskContext = task.context;
                    state.DefinitionId = repository.getDefinitionId(task.sourceId, task.code);
                    state.Definition = repository.getDefinition(state.DefinitionId);
                    state.Context = task.context;
                    state.CurrentStepIndex = task.step;
                    state.MultipleStepIteration = task.stepIteration;
                    state.Schema = Schema.fromFile(LocalStorage.getTaskResultSchemaFile(context, String.valueOf(taskId)), state.Definition);
                    //state.Schema = Schema.fromFile(LocalStorage.getTaskDefaultSchemaFile(context, String.valueOf(taskId)), state.Definition);

                    int index = state.CurrentStepIndex;
                    if (index > 0) index--;
                    Step previousStep = state.Definition.stepList.get(index);
                    for (Step.Edit field :previousStep.edits){
                        if (field.isRequired){
                            Step.Edit.Type type = field.type;
                            //reviousStep.edits.indexOf(field.name);
                            Schema currentSchema = state.Schema.getSchema(previousStep.name);
                            if (currentSchema != null){
                                Object shField = currentSchema.get(field.name);
                                if (field.isMultiple){
                                    if (isMultipleEmpty(type, currentSchema, field.name)){
                                        state.goBack();
                                        break;
                                    }
                                }else{
                                    if (shField== null){
                                        state.goBack();
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    Step currentStep = state.getCurrentStep();
                    if (currentStep.isMultiple) {
                        List<Schema> stepSchemas = state.Schema.getSchemaList(currentStep.name);
                        if (stepSchemas.size() == 0){
                            stepSchemas.add(new Schema());
                            //TODO ADD 9 SCHEMAS PLUS
                            for (int i=0;i<9;i++)
                                stepSchemas.add(new Schema());
                        }

                        state.CurrentStepSchema = stepSchemas.get(state.MultipleStepIteration);
                        int minIndex = 0;
                        for (int i = 0;i<state.Schema.getSchemaList(currentStep.name).size(); i++){
                            if (state.Schema.getSchemaList(currentStep.name).get(i).getBoolean("IsLastStep" + currentStep.name) || state.Schema.getSchemaList(currentStep.name).get(i).getBoolean("IsLastStep")){
                                minIndex=i;
                                break;
                            }
                        }

                        if ((state.Schema.getSchemaList(currentStep.name).get(minIndex).getBoolean("IsLastStep" +currentStep.name) || state.Schema.getSchemaList(currentStep.name).get(minIndex).getBoolean("IsLastStep")  ) && minIndex + 1 == state.MultipleStepIteration){
                            state.MultipleStepIteration = 0;
                            if (state.CurrentStepIndex < (state.Definition.stepList.size() - 1)) {
                                state.CurrentStepIndex++;
                                Step nextStep = state.Definition.stepList.get(state.CurrentStepIndex);
                                state.CurrentStepSchema = state.Schema.getSchema(nextStep.name);
                            }
                        }
                    } else
                        state.CurrentStepSchema = state.Schema.getSchema(currentStep.name);
                } catch (Exception e) {
                    Log.error(e);
                    finish();
                }
                return state;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<State> loader, State state) {
        this.state = state;

        Step currentStep = state.getCurrentStep();
        view.setTitle(currentStep.label);
        view.setStepLabel(state.taskLabel);
        if (currentStep.textToShow != null && currentStep.textToShow.length() > 0)
            this.view.setStepDescription(currentStep.textToShow);
        view.previousButton(this.state.canGoBack());
        view.nextButton(this.state.canGoNext());
        view.finishButtonVisibility(!this.state.canGoNext());
        view.buttonsVisibility(true);

        if (currentStep.captureDate != null || currentStep.capturePosition != null) {
            BusProvider.get().post(new CapturingDataStartedEvent());
            new CaptureTask().execute();
        }

        Bundle savedInstanceState = ((SimpleDataLoader<?>) loader).getArgs();
        for (Edit edit : currentStep.edits) {
            EditHolder<?> holder = editorFactory.build(edit.type, context);
            editHolders.add(holder);
            holder.init(this.context, this.state.SourceId, edit, currentStep.name, this.taskId, state.TaskContext, this.view, this, this.validator, this.readOnly);
            view.addEditor(holder);
            holder.load(this.state.CurrentStepSchema);
            if (savedInstanceState == null) continue;
            Bundle holderBundle = savedInstanceState.getBundle("EDIT_HOLDER$" + edit.name);
            if (holderBundle != null)
                holder.onRestoreInstanceState(holderBundle);
        }
        if (editHolders.size() > 0)
            editHolders.get(0).requestFocus();

        view.dispatchPendingResults();

        BusProvider.get().post(new FinishLoadingEvent());
    }

    @Override
    public void onLoaderReset(Loader<State> loader) {

    }

    private class CaptureTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Step currentStep = state.getCurrentStep();
            if (currentStep.captureDate != null) {
                Date date = state.CurrentStepSchema.getDate(currentStep.captureDate);
                if (date == null)
                    state.CurrentStepSchema.putDate(currentStep.captureDate, new Date());
            }
            if (currentStep.capturePosition == null) return null;

            Location location = state.CurrentStepSchema.getLocation(currentStep.capturePosition);
            if (location == null) {
                Coordinate coord = LocationHelper.getLastKnownLocation(context);
                location = new Location();
                location.setLatLon(coord.getLat(), coord.getLon(), new Date());
                state.CurrentStepSchema.putLocation(currentStep.capturePosition, location);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            BusProvider.get().post(new CapturingDataFinishedEvent());
        }
    }

    @Override
    public String getEditorValue(String editorName) {
        this.saveToSchema();
        String[] pathElements = editorName.split("\\.");
        Schema schema = state.Schema;
        for (String pathElement : pathElements) {
            if (!schema.containsSchema(pathElement))
                break;
            schema = schema.getSchema(pathElement);
        }
        Object value = schema.get(pathElements[pathElements.length - 1]);
        if (value == null) return null;
        if (value instanceof Term)
            return ((Term) value).code;
        return value.toString();
    }

}
