package be.helha.gestiondepoints.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.views.fragments.arrayAdapter.ClassArrayAdapter;
import be.helha.gestiondepoints.views.ClassesViewController;
import be.helha.gestiondepoints.models.Class;
import be.helha.gestiondepoints.views.fragments.ListFragment;

public class ClassesActivity extends AppCompatActivity implements ClassesViewController.OnViewInteractionListener, ListFragment.ListFragmentListener<Class>, ClassArrayAdapter.OnViewInteractionListener {
    private ClassesViewController viewController;
    private DatabaseHelper dbHelper;
    //controller de vue pour le fragment ListFragment
    private ListFragment<Class> mClassListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        viewController = new ClassesViewController(this);
        viewController.setListener(this);

        //récupération du fragment
        mClassListFragment = (ListFragment<Class>) getSupportFragmentManager().findFragmentById(R.id.class_list_fragment);

        updateClassList();
    }


    private void updateClassList() {
        try {
            List<Class> classList = dbHelper.getAllClasses();
            ClassArrayAdapter adapter = new ClassArrayAdapter(this, classList);
            adapter.setListener(this);
            mClassListFragment.setItemList(classList, adapter);
        } catch (Exception e) {
            Log.e("ClassesActivity", "updateClassList", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAddClassButtonClicked(String name) {
        Class newClass = new Class(name);
        dbHelper.insertClass(newClass);
        updateClassList();
    }


    //nouveau listener lié au fragment
    @Override
    public void onItemClicked(Class item) {
        Intent intent = new Intent(this, StudentsActivity.class);
        intent.putExtra("class_id", item.getId());
        startActivity(intent);
    }

    @Override
    public void onToStudentsButtonClicked(Class c) {
        Intent intent = new Intent(this, StudentsActivity.class);
        intent.putExtra("class_id", c.getId());
        startActivity(intent);
    }

    @Override
    public void onToCoursesButtonClicked(Class c) {
        Intent intent = new Intent(this, EvaluationActivity.class);
        intent.putExtra("class_id", c.getId());
        startActivity(intent);
    }
}