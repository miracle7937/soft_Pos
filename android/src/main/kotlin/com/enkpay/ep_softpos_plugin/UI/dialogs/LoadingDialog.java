package com.enkpay.ep_softpos_plugin.UI.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.enkpay.ep_softpos_plugin.R;
import com.enkpay.ep_softpos_plugin.databinding.LayoutLoadingDialogBinding;

public class LoadingDialog extends DialogFragment {
    private String loadingMessage;
    TextView textView;
    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    private LayoutLoadingDialogBinding binding;
    private   View view;

    public LoadingDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       view = inflater.inflate(R.layout.layout_loading_dialog, container, false);
//        binding = DataBindingUtil.inflate(inflater, R.layout.layout_loading_dialog, container, false);
        setLoadingMessage("Processing...");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView= view.findViewById(R.id.loading_message);
        textView.setText(loadingMessage);
//        binding.loadingMessage.setText(loadingMessage);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.curve_bg);
            getDialog().setCancelable(false);
        }
    }
}
