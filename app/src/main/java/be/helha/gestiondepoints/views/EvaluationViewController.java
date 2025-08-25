package be.helha.gestiondepoints.views;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.controllers.EvaluationActivity;

public class EvaluationViewController {
    private OnViewInteractionListener listener;
    private EvaluationActivity mContext;
    private TextView mClassNameTextView;
    private String mSubjectClassName;
    private Button mBtn_addEvaluation;
    private EditText mEt_evaluationName;

    public EvaluationViewController(EvaluationActivity context, String className) {
        this.mContext = context;
        this.mSubjectClassName = className;
        context.setContentView(R.layout.evaluation_activity_layout);
        setupListeners();
        displayClassName();
    }

    private void displayClassName() {
        mClassNameTextView =  (TextView) mContext.findViewById(R.id.EvaluationSubjectLabel);
        mClassNameTextView.setText(mSubjectClassName);
    }

    private void setupListeners() {
        try{
            mBtn_addEvaluation = mContext.findViewById(R.id.btn_addEvaluation);
            mEt_evaluationName = mContext.findViewById(R.id.et_evaluationName);
            mBtn_addEvaluation.setOnClickListener(v -> {
                String evaluationName = mEt_evaluationName.getText().toString();
                listener.onAddMainCourse(evaluationName);
            });
        }catch (RuntimeException e){
            //Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setListener(OnViewInteractionListener listener) {
        this.listener = listener;
    }

    public interface OnViewInteractionListener {
        void onAddMainCourse(String evaluationName);
    }
}
