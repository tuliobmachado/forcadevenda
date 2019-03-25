package br.com.informsistemas.forcadevenda.controller.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.helper.Misc;

public class RelatorioPedidoModalFragment extends DialogFragment {

    private Calendar myCalendar = Calendar.getInstance();
    private EditText edtDataInicio;
    private EditText edtDataFim;
    DatePickerDialog.OnDateSetListener dateInicio = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInicio();
        }
    };
    DatePickerDialog.OnDateSetListener dateFim = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateFim();
        }
    };

    public static RelatorioPedidoModalFragment newInstance(){
           RelatorioPedidoModalFragment frag = new RelatorioPedidoModalFragment();
           return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogDefault);
        builder.setTitle("Pesquisa");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_modal_relatorio_pedido, null);
        edtDataInicio = view.findViewById(R.id.edtDataInicio);
        edtDataInicio.setOnClickListener(onGetClickListener(dateInicio));

        edtDataFim = view.findViewById(R.id.edtDataFim);
        edtDataFim.setOnClickListener(onGetClickListener(dateFim));
        builder.setView(view);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("dataInicio", edtDataInicio.getText().toString());
                bundle.putString("dataFim", edtDataFim.getText().toString());
                intent.putExtras(bundle);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            }
        });

        return builder.create();
    }

    private View.OnClickListener onGetClickListener(final DatePickerDialog.OnDateSetListener date){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), R.style.DialogDatePicker, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        };
    }

    private void updateDateInicio(){
        edtDataInicio.setText(Misc.formatDate(myCalendar.getTime(), "dd/MM/yyyy"));
    }

    private void updateDateFim(){
        edtDataFim.setText(Misc.formatDate(myCalendar.getTime(), "dd/MM/yyyy"));
    }
}
