package com.example.boardgamer_app.Classes;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.boardgamer_app.Activity_evening_details;
import com.example.boardgamer_app.Main4Activity;
import com.example.boardgamer_app.R;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogGames extends DialogFragment {

    public interface OnInputListener{
        void sendInput(String input);
    }

    public OnInputListener mOnInputListener;

    //logt-snippet
    private static final String TAG = "DialogGames";

    //widgets
    private Spinner mInput;
    private TextView mActionOk, mActionCancel;
    List<String> gamesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dialog_games, container, false);
        mActionCancel = view.findViewById(R.id.textDialogCancel);
        mActionOk = view.findViewById(R.id.textDialogOk);
        mInput = view.findViewById(R.id.spinnerGames);
        gamesList = new ArrayList<String>();
        gamesList.add("Monopoly");
        gamesList.add("Uno");
        gamesList.add("Tabu");
        gamesList.add("Risiko");
        gamesList.add("Romme");
        gamesList.add("Poker");
        gamesList.add("Siedler von Catan");
        gamesList.add("Jumanji");
        gamesList.add("Dixit");
        gamesList.add("Scotland Yard");



        SpinnerConfig();

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logd-snippet
                Log.d(TAG, "onClick: Cancel");
                getDialog().dismiss();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: OK");
                String input = mInput.getSelectedItem().toString();
                Log.d(TAG, input);
                //((Activity_evening_details)getActivity()).listViewObjects.add(input);
                mOnInputListener.sendInput(input);
                getDialog().dismiss();
            }

        });

        return view;
    }

    private void SpinnerConfig() {
        ArrayAdapter<String> adapterGames = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, gamesList);
        adapterGames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mInput.setAdapter(adapterGames);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputListener = (OnInputListener) getActivity();
        }catch(ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
