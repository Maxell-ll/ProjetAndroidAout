package be.helha.gestiondepoints.views.fragments.arrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import be.helha.gestiondepoints.R;
import be.helha.gestiondepoints.models.Class;

public class ClassArrayAdapter extends ArrayAdapter<be.helha.gestiondepoints.models.Class> {
    private Context mContext;
    private OnViewInteractionListener mListener;
    public ClassArrayAdapter(Context context, List<be.helha.gestiondepoints.models.Class> classes) {
        super(context, 0, classes);
        this.mContext = context;
    }

    public void setListener(OnViewInteractionListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            //le R.layout.simple_list_item_1 est un layout fourni par android qui contient un TextView. Donc tout les éléments de la liste seront affichés dans un TextView
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.classes_list_item, parent, false);
        }
        Class c = getItem(position);
        TextView textView = convertView.findViewById(R.id.text_view);
        ImageButton toStudentsManagement = convertView.findViewById(R.id.toStudent_button);
        ImageButton toLessonsManagement = convertView.findViewById(R.id.toLessons_button);
        textView.setText("ID : " + c.getId() + " - " + c.getName());

        toStudentsManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onToStudentsButtonClicked(c);
            }
        });

        toLessonsManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onToCoursesButtonClicked(c);
            }
        });

        return convertView;
    }

    public interface OnViewInteractionListener {
        void onToStudentsButtonClicked(Class c);
        void onToCoursesButtonClicked(Class c);
    }
}

