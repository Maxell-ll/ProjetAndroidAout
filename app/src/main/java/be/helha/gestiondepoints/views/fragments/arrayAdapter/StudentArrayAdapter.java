package be.helha.gestiondepoints.views.fragments.arrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import be.helha.gestiondepoints.models.Student;

public class StudentArrayAdapter extends ArrayAdapter {
    public StudentArrayAdapter(Context context, List<Student> students ) {
        super(context, 0, students);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Recycle la vue si possible
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // 2. Récupère les données de l'objet Class à la position actuelle
        Student c = (Student)getItem(position);

        // 3. Met à jour le texte de l’élément (affiche les informations de Class)
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText("ID : " + c.getId() + " - " + c.getFirstName() + " " + c.getLastName());

        // 4. Retourne la vue configurée pour cet élément
        return convertView;
    }
}
