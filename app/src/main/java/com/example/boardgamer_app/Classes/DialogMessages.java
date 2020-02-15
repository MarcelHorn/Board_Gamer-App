package com.example.boardgamer_app.Classes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;

import android.widget.TextView;

import com.example.boardgamer_app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogMessages extends DialogFragment {

    //Interface, welches von Main2Activity verwendet werden muss
    public interface OnInputListener{
        void sendInput(String input);
    }

    public OnInputListener mOnInputListener;

    //logt-snippet
    private static final String TAG = "DialogGames";

    //widgets
    private EditText mInput;
    private TextView mActionOk, mActionCancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dialog_message, container, false);
        mActionCancel = view.findViewById(R.id.textViewDialogMessCancel);
        mActionOk = view.findViewById(R.id.textViewDialogMessOk);
        mInput = view.findViewById(R.id.editTextMessageInput);

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
                String input = mInput.getText().toString();
                Log.d(TAG, input);
                mOnInputListener.sendInput(input);
                getDialog().dismiss();
            }

        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputListener = (DialogMessages.OnInputListener) getActivity();
        }catch(ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
