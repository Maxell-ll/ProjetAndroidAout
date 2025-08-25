package be.helha.gestiondepoints.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.models.Evaluation;

public class AddEvaluationDialogViewController {

    private Context mContext;
    private OnDialogInteractionListener mListener;
    private Long classId;

    public AddEvaluationDialogViewController(Context context, Long classId) {
        this.mContext = context;
        this.classId = classId;
    }
    public void setListener(OnDialogInteractionListener listener) {
        this.mListener = listener;
    }

    public void showAddEvaluationDialog(long parentId) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.dialog_add_evaluation, null);
        EditText etName = dialogView.findViewById(R.id.et_evalName);
        EditText etPoints = dialogView.findViewById(R.id.et_evalPointsMax);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView)
                .setTitle("Ajouter une évaluation")
                .setCancelable(false)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String evaluationName = etName.getText().toString();
                    String points = etPoints.getText().toString();
                    if (!evaluationName.isEmpty() && !points.isEmpty()) {
                        try {
                            int maxPoints = Integer.parseInt(points);
                            mListener.onEvaluationAddedInDialog(-1L, evaluationName, maxPoints, parentId, classId);
                        } catch (NumberFormatException e) {
                            Toast.makeText(mContext, "Points maximum doit être un nombre valide", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public interface OnDialogInteractionListener {
        void onEvaluationAddedInDialog(Long id, String name, int maxPoints, long parentId, long classId);
    }
}
