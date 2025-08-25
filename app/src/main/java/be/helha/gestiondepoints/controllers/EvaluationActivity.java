package be.helha.gestiondepoints.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.models.Evaluation;
import be.helha.gestiondepoints.utils.EvaluationHierarchy;
import be.helha.gestiondepoints.views.AddEvaluationDialogViewController;
import be.helha.gestiondepoints.views.EvaluationViewController;
import be.helha.gestiondepoints.views.fragments.ListFragment;
import be.helha.gestiondepoints.views.fragments.arrayAdapter.EvaluationArrayAdapter;
import be.helha.gestiondepoints.models.Class;
import be.helha.gestiondepoints.controllers.EvaluationNoteActivity;

public class EvaluationActivity extends AppCompatActivity implements EvaluationViewController.OnViewInteractionListener, ListFragment.ListFragmentListener<Evaluation>, EvaluationArrayAdapter.OnViewInteractionListener, AddEvaluationDialogViewController.OnDialogInteractionListener {
    private EvaluationViewController viewController;
    private AddEvaluationDialogViewController dialogController;
    private DatabaseHelper dbHelper;
    private ListFragment<Evaluation> mCourseListFragment;
    private long classId;
    private Class mSubjectClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        classId = getIntent().getLongExtra("class_id", -1);
        mSubjectClass = dbHelper.getClassesById(classId);
        viewController = new EvaluationViewController(this, mSubjectClass.getName());
        dialogController = new AddEvaluationDialogViewController(this, classId);
        dialogController.setListener(this);
        viewController.setListener(this);
        mCourseListFragment = (ListFragment<Evaluation>) getSupportFragmentManager().findFragmentById(R.id.student_fragment);
        loadEvaluationList();
    }

    private void loadEvaluationList() {
        try {
            //récupère les évaluations de la base de données
            List<Evaluation> evaluations = dbHelper.getEvaluationsByClassId(classId);


            //Debug

            for (Evaluation evaluation : evaluations) {
                Log.i("EvaluationActivity", "Evaluation: " + evaluation.getName() + " Parent: " + evaluation.getParentEvaluationId());
            }


            //crée une hiérarchie d'évaluations (indente les évaluations dans le fragment en fonction de leur parent)
            Map<Long, List<Evaluation>> hierarchy = EvaluationHierarchy.buildHierarchy(evaluations);
            List<Evaluation> flatList = EvaluationHierarchy.flattenHierarchy(hierarchy, (long) -1);

            Log.i("EvaluationActivity", "flatList of activity and child-activity: " + flatList);

            for(Evaluation evaluation : flatList){
                Log.i("EvaluationActivity", "Evaluation: " + evaluation.getName() + " Parent: " + evaluation.getParentEvaluationId());
            }

            //constructeur de l'adapter
            EvaluationArrayAdapter adapter = new EvaluationArrayAdapter(this, flatList, hierarchy, 50);
            adapter.setListener(this);

            //j'envoie la liste des évaluations à mon fragment pour qu'il les affiche
            mCourseListFragment.setItemList(flatList, adapter);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //Listeners

    @Override
    public void onAddMainCourse(String evaluationName) {
        if(evaluationName.isEmpty()){
            throw new RuntimeException("Nom du cours requis");
        }
        Evaluation evaluation = new Evaluation(-1, evaluationName, 20, classId, -1);
        dbHelper.insertCourse(evaluation);
        loadEvaluationList();
    }

    @Override
    public void onItemClicked(Evaluation item) {
    }

    @Override
    public void onAddSubEvaluation(Evaluation evaluation) {
        // Afficher la fenêtre de dialogue pour ajouter une sous-évaluation
        dialogController.showAddEvaluationDialog(evaluation.getId());
    }

    @Override
    public void onEvaluationClick(Evaluation evaluation) {
        //lance la prochaine activité qui affiche les détails de l'évaluation (gestion des points)
        Log.i("EvaluationActivity", "onItemClicked: " + evaluation.getName());
        Log.i("EvaluationActivity", "onItemClicked: " + evaluation.getId());

        Intent intent = new Intent(this, EvaluationNoteActivity.class);
        intent.putExtra("evaluation_id", evaluation.getId());
        intent.putExtra("is_parent", dbHelper.isParentEvaluation(evaluation.getId()));
        startActivity(intent);
    }

    //gestionnaire d'event quand l'ajout d'une évaluation est confirmé (dans la fenêtre de dialogue d'ajout d'évaluation)
    @Override
    public void onEvaluationAddedInDialog(Long id, String name, int maxPoints, long parentId, long classId) {
        Evaluation evaluation = new Evaluation(id, name, maxPoints, classId, parentId);
        dbHelper.insertCourse(evaluation);
        loadEvaluationList();
    }
}
