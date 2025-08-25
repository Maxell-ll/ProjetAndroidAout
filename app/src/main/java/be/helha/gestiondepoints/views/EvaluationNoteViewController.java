package be.helha.gestiondepoints.views;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.controllers.EvaluationNoteActivity;
import be.helha.gestiondepoints.views.fragments.arrayAdapter.EvaluationArrayAdapter;

public class EvaluationNoteViewController {
    private EvaluationNoteActivity mContext;
    private String mEvaluationName;
    private OnViewInteractionListener listener;
    private Button btnCalculateAverage;
    private boolean isParent;

    public EvaluationNoteViewController(EvaluationNoteActivity context, String evaluationName, boolean isParent) {
        this.mContext = context;
        this.mEvaluationName = evaluationName;
        this.isParent = isParent;
        context.setContentView(R.layout.note_evaluations_activity);
        btnCalculateAverage = mContext.findViewById(R.id.btn_calculate_avg);
        setupListeners();
        displayEvaluationName();
        initSetup();
    }

    private void initSetup() {
        if (isParent) {
            btnCalculateAverage.setVisibility(View.VISIBLE);
        } else {
            btnCalculateAverage.setVisibility(View.GONE);
        }
    }

    private void displayEvaluationName() {
        TextView tvEvaluationName = mContext.findViewById(R.id.EvaluationSubjectLabel);
        tvEvaluationName.setText(mEvaluationName);
    }

    public void setListener(OnViewInteractionListener listener) {
        this.listener = listener;
    }

    private void setupListeners() {
        Button btnSave = mContext.findViewById(R.id.save_button);
        btnSave.setOnClickListener(v -> {
            listener.onSaveModification();
        });
        btnCalculateAverage.setOnClickListener(v -> {
            listener.onCalculateAverage();
        });
    }


    public interface OnViewInteractionListener {
        void onSaveModification();
        void onCalculateAverage();
    }

}
