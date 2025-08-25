package be.helha.gestiondepoints.views.fragments.arrayAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.models.Evaluation;

public class EvaluationArrayAdapter extends ArrayAdapter<Evaluation> {
    private Context mContext;
    private OnViewInteractionListener mListener;
    private Map<Long, List<Evaluation>> mEvaluationMap;
    private int mIndent;



    //indent est la distance entre le bord gauche de l'écran et le texte (50px)
    public EvaluationArrayAdapter(Context context, List<Evaluation> evaluations, Map<Long, List<Evaluation>> mEvaluationMap, int indent) {
        super(context, 0, evaluations);
        this.mContext = context;
        this.mEvaluationMap = mEvaluationMap;
        this.mIndent = indent;
    }

    public void setListener(OnViewInteractionListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Evaluation evaluation = getItem(position);
        if (convertView == null) {
            //le R.layout.simple_list_item_1 est un layout fourni par android qui contient un TextView. Donc tout les éléments de la liste seront affichés dans un TextView
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.evaluation_list_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.evaluationName);
        ImageButton addEvaluationButton = convertView.findViewById(R.id.addButton);

        int level = getEvaluationLevel(evaluation.getId(), 0);
        switch (level) {
            case 1:
                textView.setText("Cours : " + evaluation.getName());
                break;
            case 2:
                textView.setText("Evaluation : " + evaluation.getName());
                break;
            case 3:
                textView.setText("Sous-évaluation : " + evaluation.getName());
                addEvaluationButton.setVisibility(View.GONE);
                break;
        }

        addEvaluationButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onAddSubEvaluation(evaluation);
            }
        });

        convertView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onEvaluationClick(evaluation);
            }
        });


        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        params.leftMargin = mIndent * level;
        textView.setLayoutParams(params);

        return convertView;
    }

    private int getEvaluationLevel(Long evaluationId, int level) {
        for (Map.Entry<Long, List<Evaluation>> entry : mEvaluationMap.entrySet()) {
            for (Evaluation evaluation : entry.getValue()) {
                if (evaluation.getId() == evaluationId) {
                    return getEvaluationLevel(entry.getKey(), level + 1);
                }
            }
        }
        return level;
    }

    public interface OnViewInteractionListener {
        void onAddSubEvaluation(Evaluation evaluation);
        void onEvaluationClick(Evaluation evaluation);
    }

}
