package be.helha.gestiondepoints.views.fragments.arrayAdapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.models.Note;
import be.helha.gestiondepoints.models.Student;

public class EvaluationNoteArrayAdapter extends ArrayAdapter<Student> {
    private Context mContext;
    private Double evaluationMaxPoint;

    private HashMap<Integer, Double> scores = new HashMap<>();


    public EvaluationNoteArrayAdapter(@NonNull Context context, List<Student> students, Double evaluationMaxPoint, List<Note> notes) {
        super(context, 0, students);
        this.mContext = context;
        this.evaluationMaxPoint = evaluationMaxPoint;

        for (Note note : notes) {
            int position = -1;
            Log.d("EvaluationNoteArrayAdapter", "Note studentId: " + note.getStudentId());
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId() == note.getStudentId()) {
                    position = i;
                    break;
                }
            }
            if (position != -1) {
                scores.put(position, note.getScore());
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Student student = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_in_evaluation_list_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.student_name);
        TextView maxPoint = convertView.findViewById(R.id.max_point_evaluation);
        EditText pointsInput = convertView.findViewById(R.id.points);

        textView.setText(student.getFirstName() + " " + student.getLastName());
        maxPoint.setText("/" + evaluationMaxPoint);

        if (scores.containsKey(position)) {
            double roundedScore = Math.round(scores.get(position) * 2) / 2.0;
            pointsInput.setText(String.valueOf(roundedScore));
        } else {
            pointsInput.setText("");
        }

        // Ajouter un TextWatcher pour capturer les modifications
        pointsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double score = Double.parseDouble(s.toString());
                    if (score >= 0 && score <= evaluationMaxPoint) {
                        scores.put(position, score); // Enregistrer le score
                    } else {
                        pointsInput.setError("Valeur invalide");
                    }
                } catch (NumberFormatException e) {
                    scores.remove(position); // Retirer la valeur si non valide
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return convertView;
    }

    public void setScores(HashMap<Integer, Double> scores) {
        this.scores = scores;
    }



    public List<Note> getNotes() {
        Log.d("EvaluationNoteArrayAdapter", "getNotes");
        List<Note> notes = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            Student student = getItem(i);
            if (scores.containsKey(i)) {
                double score = scores.get(i);
                Note note = new Note(-1, student.getId(), student.getClassId(), evaluationMaxPoint);
                note.setScore(score);
                notes.add(note);
            } else {
                Note note = new Note(-1, student.getId(), student.getClassId(), evaluationMaxPoint);
                note.setScore(-1);
                notes.add(note);
            }
        }
        return notes;
    }

}
