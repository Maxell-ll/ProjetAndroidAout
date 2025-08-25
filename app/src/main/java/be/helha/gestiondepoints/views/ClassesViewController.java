package be.helha.gestiondepoints.views;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.controllers.ClassesActivity;
import be.helha.gestiondepoints.models.Class;

public class ClassesViewController {
    private ClassesActivity mContext;
    private OnViewInteractionListener mListener;
    private Button mAddClassButton;
    private EditText mClassNameEditText;


    public ClassesViewController(ClassesActivity context) {
        context.setContentView(R.layout.activity_add_class);
        this.mContext = context;
        setupListeners();
    }

    public void setListener(OnViewInteractionListener listener) {
        mListener = listener;
    }

    private void setupListeners() {
        mAddClassButton = mContext.findViewById(R.id.add_class_button);
        mClassNameEditText = mContext.findViewById(R.id.editClassName);

        mAddClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String className = mClassNameEditText.getText().toString();
                    if (className.isEmpty()) {

                        mClassNameEditText.setError("Le nom de la classe ne peut pas être vide");
                    } else {
                        mListener.onAddClassButtonClicked(className);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public interface OnViewInteractionListener {
        void onAddClassButtonClicked(String name);
    }
}
