package be.helha.gestiondepoints.views;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.controllers.StudentsActivity;
import be.helha.gestiondepoints.models.Student;

public class StudentsViewController {
    private StudentsActivity mContext;
    private OnViewInteractionListener mListener;
    private ListView studentListView;
    //private List<Student> studentList;
    //private ArrayAdapter<String> studentAdapter;
    private EditText firstNameEditText;
    private EditText lastNameEditText;


    public StudentsViewController(StudentsActivity context) {
        context.setContentView(R.layout.activity_add_student);
        this.mContext = context;
        //studentListView = context.findViewById(R.id.student_list_view);
        firstNameEditText = context.findViewById(R.id.first_name_input);
        lastNameEditText = context.findViewById(R.id.last_name_input);
        setupListeners();
    }

    private void setupListeners() {
        Button addStudentButton = mContext.findViewById(R.id.add_student_button);
        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                mListener.onAddStudentButtonClicked(firstName, lastName);
            }
        });
    }

    //public void updateStudentList(List<Student> studentList) {
        //this.studentList = studentList;
        //List<String> studentNames = new ArrayList<>();
        //for (Student s : studentList) {
            //studentNames.add(s.getFirstName() + " " + s.getLastName());
        //}

        //studentAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, studentNames);
        //studentListView.setAdapter(studentAdapter);
    //}

    public void setListener(OnViewInteractionListener listener) {
        mListener = listener;
    }

    public interface OnViewInteractionListener {
        void onAddStudentButtonClicked(String firstName, String lastName);
    }
}
