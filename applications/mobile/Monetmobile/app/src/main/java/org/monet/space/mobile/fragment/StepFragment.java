package org.monet.space.mobile.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.squareup.otto.Subscribe;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.CapturingDataFinishedEvent;
import org.monet.space.mobile.events.CapturingDataStartedEvent;
import org.monet.space.mobile.events.FinishLoadingEvent;
import org.monet.space.mobile.jtm.editors.EditHolder;
import org.monet.space.mobile.model.Preferences;
import org.monet.space.mobile.model.schema.Term;
import org.monet.space.mobile.mvp.fragment.Fragment;
import org.monet.space.mobile.presenter.StepPresenter;
import org.monet.space.mobile.view.StepView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepFragment extends Fragment<StepView, StepPresenter, Void> implements StepView, OnClickListener {

    private static final String KEY_ARTICLE_SCROLL_POSITION = "ARTICLE_SCROLL_POSITION";
    private static final String KEY_REQUESTS = "REQUESTS";
    private static final String KEY_EDIT_HOLDER = "EDIT_HOLDER$";
    private static final int RESULT_FROM_QRSCAN = 666;

    private final List<Result> pendingResults = new ArrayList<>();
    private View buttonsLayout;
    private Button previousBtn;
    private Button nextBtn;
    private Button finishBtn;

    private ScrollView scrollView;
    private TextView labelText;
    private TextView subLabelText;
    private LinearLayout contentLayout;
    private HashMap<Integer, String> activityRequesters;
    private int[] scrollViewPosition;
    private ImageButton qrCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        buttonsLayout = view.findViewById(R.id.buttons_layout);
        previousBtn = (Button) view.findViewById(R.id.button_previous);
        previousBtn.setOnClickListener(this);
        nextBtn = (Button) view.findViewById(R.id.button_next);
        nextBtn.setOnClickListener(this);
        finishBtn = (Button) view.findViewById(R.id.button_finish);
        finishBtn.setOnClickListener(this);
        contentLayout = (LinearLayout) view.findViewById(R.id.step_content);
        labelText = (TextView) view.findViewById(R.id.text_label);
        subLabelText = (TextView) view.findViewById(R.id.text_sub_label);
        qrCode = (ImageButton) view.findViewById(R.id.qr_button);
        qrCode.setOnClickListener(this);
        Preferences preferences = new Preferences(this.getContext());
        if (!preferences.isQrCodeEnabled())
            qrCode.setVisibility(View.GONE);


        return view;
    }

    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            scrollViewPosition = savedInstanceState.getIntArray(KEY_ARTICLE_SCROLL_POSITION);
            activityRequesters = (HashMap<Integer, String>) savedInstanceState.getSerializable(KEY_REQUESTS);
        } else if (activityRequesters == null)
            activityRequesters = new HashMap<>();
        presenter.initialize(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (scrollViewPosition == null) return;
        scrollView.postDelayed(new Runnable() {
            public void run() {
                scrollView.scrollTo(scrollViewPosition[0], scrollViewPosition[1]);
                scrollViewPosition = null;
            }
        }, 50);
    }

    public void onPause() {
        super.onPause();
        scrollViewPosition = new int[]{scrollView.getScrollX(), scrollView.getScrollY()};
        presenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_ARTICLE_SCROLL_POSITION, this.scrollViewPosition);
        outState.putSerializable(KEY_REQUESTS, this.activityRequesters);
        for (EditHolder<?> holder : getEditHolders().values()) {
            Bundle holderBundle = new Bundle();
            outState.putBundle(KEY_EDIT_HOLDER + holder.getName(), holderBundle);
            holder.onSaveInstanceState(holderBundle);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, EditHolder<?> holder) {
        activityRequesters.put(requestCode, holder.getName());
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // if (requestCode == 666){
            //TODO borrar valor por defecto y poner if comentado, solo para desarrollo
            //String qrValue = "MonetMobileQr#;#ciudaddemar#;#ObjetoActuacion#;#Term#;#0001#;#PLAYA DE LA LAJA#;#";
            if (requestCode == 666 && data != null){
                String qrValue = data.getStringExtra("SCAN_RESULT");
            //if (data != null)
            //    qrValue = data.getStringExtra("SCAN_RESULT");
                Log.d("QRCODE",qrValue);
               // Toast.makeText(this.getContext(), "Cargando QRCode: " + qrValue, Toast.LENGTH_SHORT).show();
                fillEditHolderWithQr(qrValue);

        }else{
            String editName = activityRequesters.get(requestCode);
            activityRequesters.remove(requestCode);
            EditHolder<?> holder = getEditHolders().get(editName);
            if (holder == null)
                pendingResults.add(new Result(editName, requestCode, resultCode, data));
            else
                holder.onActivityForResult(requestCode, resultCode, data);
        }

    }

    private void fillEditHolderWithQr(String qrValue){
        String[] qrParse = qrValue.split("#;#");
        if (qrParse[0].equals("MonetMobileQr")){
            String field = qrParse[2];
            String type = qrParse[3];
            Map<String, EditHolder<?>> editHolders = getEditHolders();
            for (String keys : editHolders.keySet()){
                if (keys.equals(field)){
                    Log.d("Campo encontrado ", keys);
                    EditHolder<?> holder = editHolders.get(keys);
                    String key = qrParse[4];
                    String value = qrParse[5];
                    switch (type){
                        case "Term":
                            Term term = new Term();
                            term.code = key;
                            term.label = value;
                            presenter.saveQrValue(holder, term);
                            break;
                        case "String":
                            presenter.saveQrValue(holder, value);
                            break;
                        case "Boolean":
                            Boolean boolValue = Boolean.valueOf(value);
                            presenter.saveQrValue(holder, boolValue);
                            break;
                        case "Date":
                            // TODO
                            break;
                        case "Number":
                            // TODO
                            break;

                    }


                }
                Log.d("Campos", keys);
            }
        }else{

        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_previous)
            presenter.previous();
        else if (view.getId() == R.id.button_next)
            presenter.next();
        else if (view.getId() == R.id.button_finish)
            presenter.finalizeTask();
        else if (view.getId() == R.id.qr_button)
            startActivityForResult(new IntentIntegrator(getActivity()).createScanIntent(), RESULT_FROM_QRSCAN);
    }

    public void setTitle(String title) {
        getActivity().setTitle(title);
    }

    public void setStepLabel(String label) {
        labelText.setText(label);
    }

    public void setStepDescription(String description) {
        subLabelText.setVisibility(View.VISIBLE);
        subLabelText.setText(description);
    }

    @Override
    public Map<String, EditHolder<?>> getEditHolders() {
        Map<String, EditHolder<?>> editHolders = new HashMap<>();

        if (contentLayout != null) {
            for (int i = 0; i < this.contentLayout.getChildCount(); i++) {
                EditHolder<?> holder = (EditHolder<?>) contentLayout.getChildAt(i).getTag();
                editHolders.put(holder.getName(), holder);
            }
        }

        return editHolders;
    }

    @Override
    public void addEditor(EditHolder<?> holder) {
        contentLayout.addView(holder.onCreateView(contentLayout));
    }

    @Override
    public void previousButton(boolean enable) {
        previousBtn.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void nextButton(boolean enable) {
        this.nextBtn.setEnabled(enable);
    }

    @Override
    public void finishButtonVisibility(boolean visible) {
        if (visible) {
            finishBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        } else {
            finishBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void buttonsVisibility(boolean visible) {
        if (visible)
            this.buttonsLayout.setVisibility(View.VISIBLE);
        else
            this.buttonsLayout.setVisibility(View.GONE);
    }

    @Subscribe
    public void capturingDataStarted(CapturingDataStartedEvent event) {
        this.nextBtn.setEnabled(false);
    }

    @Subscribe
    public void capturingDataFinished(CapturingDataFinishedEvent event) {
        this.nextBtn.setEnabled(true);
    }

    @Subscribe
    public void onFinished(FinishLoadingEvent event) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
    }

    @Override
    public void dispatchPendingResults() {
        for (Result result : pendingResults) {
            EditHolder<?> holder = getEditHolders().get(result.Name);
            if (holder != null)
                holder.onActivityForResult(result.RequestCode, result.ResultCode, result.Data);
        }
        pendingResults.clear();
    }

}
