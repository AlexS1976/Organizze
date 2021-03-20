package com.example.organizze1.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.organizze1.Model.Movimentacao;
import com.example.organizze1.R;
import com.example.organizze1.helper.DateCustom;
import com.google.android.material.textfield.TextInputEditText;

public class SaidaActivity extends AppCompatActivity {

   private TextInputEditText campoData, campoCategoria, campoDescricao;

    private EditText campoValor;
    private Movimentacao movimentacao;

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

    public void salvarDespesa(View view){

        movimentacao = new Movimentacao();
        String data = campoData.getText().toString();
        movimentacao.setValor(Double.parseDouble(campoValor.getText().toString()
        ) );
        movimentacao.setCategoria(campoCategoria.getText().toString());
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setData(data);
        movimentacao.setTipo("d");

        movimentacao.salvar(data);

    }


}