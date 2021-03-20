package com.example.organizze1.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.organizze1.R;
import com.example.organizze1.helper.DateCustom;
import com.google.android.material.textfield.TextInputEditText;

import static com.example.organizze1.R.id.editValor;

public class SaidaActivity extends AppCompatActivity {

   private TextInputEditText campoData, campoCategoria, campoDescricao;

    private EditText campoValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saida);

        campoData = findViewById(R.id.editData);
        campoCategoria =findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);

        // colocar a data atual como padrao
        campoData.setText(DateCustom.dataAtual());
    }
}