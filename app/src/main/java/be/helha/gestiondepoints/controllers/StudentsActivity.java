package be.helha.gestiondepoints.controllers;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.models.Student;
import be.helha.gestiondepoints.views.fragments.arrayAdapter.StudentArrayAdapter;
import be.helha.gestiondepoints.views.StudentsViewController;
import be.helha.gestiondepoints.views.fragments.ListFragment;

public class StudentsActivity extends AppCompatActivity implements StudentsViewController.OnViewInteractionListener, ListFragment.ListFragmentListener<Student> {
    private StudentsViewController viewController;
    private long classId;
    private DatabaseHelper dbHelper;
    // controller de vue pour le fragment ListFragment (on précise le type de l'objet à afficher)
    private ListFragment<Student> mStudentListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        viewController = new StudentsViewController(this);
        viewController.setListener(this);
        classId = getIntent().getLongExtra("class_id", -1);

        // récupération du fragment
        mStudentListFragment = (ListFragment<Student>) getSupportFragmentManager().findFragmentById(R.id.student_list_fragment);

        updateStudentList();
    }

//    void updateStudentList() {
//        List<Student> studentList = dbHelper.getStudentsByClassId(classId);
//        viewController.updateStudentList(studentList);
//    }

    void updateStudentList() {
        try{
            Log.i("StudentsActivity", "dans la méthode updateStudentList");
            List<Student> studentList = dbHelper.getStudentsByClassId(classId);
            StudentArrayAdapter adapter = new StudentArrayAdapter(this, studentList);
            mStudentListFragment.setItemList(studentList, adapter);

        } catch (Exception e) {
            Log.e("StudentsActivity", "updateStudentList", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAddStudentButtonClicked(String firstName, String lastName) {
        Student newStudent = new Student();
        newStudent.setClassId(classId);
        newStudent.setFirstName(firstName);
        newStudent.setLastName(lastName);
        dbHelper.insertStudent(newStudent);
        updateStudentList();
    }

    @Override
    public void onItemClicked(Student item) {
        // code pour gérer le clic sur un élément de la liste
        //TODO Ouvrir la page de détail de l'étudiant
    }
}
