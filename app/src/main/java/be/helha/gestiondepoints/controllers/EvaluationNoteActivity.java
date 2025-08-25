package be.helha.gestiondepoints.controllers;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.models.Evaluation;
import be.helha.gestiondepoints.models.Note;
import be.helha.gestiondepoints.models.Student;
import be.helha.gestiondepoints.views.AddEvaluationDialogViewController;
import be.helha.gestiondepoints.views.EvaluationNoteViewController;
import be.helha.gestiondepoints.views.EvaluationViewController;
import be.helha.gestiondepoints.views.fragments.ListFragment;
import be.helha.gestiondepoints.views.fragments.arrayAdapter.EvaluationNoteArrayAdapter;

public class EvaluationNoteActivity extends AppCompatActivity implements EvaluationNoteViewController.OnViewInteractionListener, ListFragment.ListFragmentListener<Student>{
    private EvaluationNoteViewController viewController;
    private DatabaseHelper dbHelper;
    private ListFragment<Student> mStudentListFragment;
    private long evaluationId;
    private Evaluation mEvaluation;
    private EvaluationNoteArrayAdapter adapter;
    private boolean isParent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        evaluationId = getIntent().getLongExtra("evaluation_id", -1);
        isParent = getIntent().getBooleanExtra("is_parent", false);
        mEvaluation = dbHelper.getEvaluationById(evaluationId);
        viewController = new EvaluationNoteViewController(this, mEvaluation.getName(), isParent);
        viewController.setListener(this);
        mStudentListFragment = (ListFragment<Student>) getSupportFragmentManager().findFragmentById(R.id.note_student_list_fragment);
        loadStudentList();
    }

    private void loadStudentList() {
        try{
            Log.d("EvaluationNoteActivity", evaluationId + "");
            List<Student> students = dbHelper.getStudentsByClassId(mEvaluation.getClassId());
            List<Note> notes = dbHelper.getNotesByEvaluationId(evaluationId);
            Log.d("EvaluationNoteActivity", "Loaded " + students.size() + " students");
            Log.d("EvaluationNoteActivity", "Loaded " + notes.size() + " notes");

             adapter = new EvaluationNoteArrayAdapter(this, students, mEvaluation.getMaxPoints(), notes);
            mStudentListFragment.setItemList(students, adapter);
        } catch (Exception e) {
            Log.e("EvaluationNoteActivity", "Error loading student list", e);
        }
    }

    @Override
    public void onSaveModification() {
        Log.d("EvaluationNoteActivity", "onSaveModification");
        try {
            List<Note> notes = ((EvaluationNoteArrayAdapter) adapter).getNotes();
            for (Note note : notes) {
                Log.d("EvaluationNoteActivity", "Saving note: " + note.getScore());
                note.setEvaluationId(evaluationId);
                Double score = note.getScore();
                if (score == -1){
                    dbHelper.deleteNoteForEvaluation(note.getEvaluationId(), note.getStudentId());
                } else {
                    dbHelper.upsertNoteForEvaluation(note);
                }
            }
            finish();
        } catch (Exception e) {
            Log.e("EvaluationNoteActivity", "Error saving notes", e);
        }
    }

    @Override
    public void onCalculateAverage(){
        List<Student> students = dbHelper.getStudentsByClassId(mEvaluation.getClassId());
        List<Note> notes = new ArrayList<>();
        for (Student student : students) {
            double avg = dbHelper.calculateAverageForStudent(this.evaluationId, student.getId());
            Log.d("EvaluationNoteActivity", "Average for student " + student.getFirstName() + " " + student.getLastName() + ": " + avg);
            Note note = new Note();
            note.setStudentId(student.getId());
            note.setScore(avg * mEvaluation.getMaxPoints());
            notes.add(note);
        }

        adapter = new EvaluationNoteArrayAdapter(this, students, mEvaluation.getMaxPoints(), notes);
        mStudentListFragment.setItemList(students, adapter);
    }

    @Override
    public void onItemClicked(Student item) {
    }
}
