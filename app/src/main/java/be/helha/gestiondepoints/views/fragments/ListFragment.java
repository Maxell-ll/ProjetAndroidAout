package be.helha.gestiondepoints.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import be.helha.gestiondepoints.R;

public class ListFragment<T> extends Fragment {
    private ListView mListView;
    //chaque activité qui utilise ce fragment a des objets différents donc l'ArrayAdapter est générique
    private ArrayAdapter<T> mAdapter;
    //Listener qui va permettre de notifier le controller de l'activité parente que l'utilisateur a cliqué sur un élément de la liste
    private ListFragmentListener<T> mListener;
    //Liste des objets à afficher dans la liste
    private List<T> mItemList;


    public interface ListFragmentListener<T> {
        void onItemClicked(T item);
    }

    //onAttach est appelée lorsque le fragment est attaché à une activité
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ListFragmentListener) {
            mListener = (ListFragmentListener<T>) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ListFragmentListener");
        }
    }

    //onCreateView est appelée lorsque le fragment est créé
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mListView = view.findViewById(R.id.list_view);
        //on appelle la méthode qui va configurer la liste (ajout des éléments, adapter, listener)
        //setupListView();
        return view;
    }

    //méthode qui va permettre de configurer la liste en fonction de la class des objets qu'elle contiendra
    //sera appelée dans le controller de l'activité parente
    public void setItemList(List<T> items, ArrayAdapter<T> adapter) {
        this.mItemList = items;
        this.mAdapter = adapter;
        //Appel de la méthode qui va configurer la liste après que les objets et l'adapter aient été définis
        setupListView();
    }



    private void setupListView() {
        Log.i("ListFragment", "Dans la méthode setupListView");
        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onItemClicked(mItemList.get(position));
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
