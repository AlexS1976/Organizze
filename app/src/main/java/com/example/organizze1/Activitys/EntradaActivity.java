package com.example.organizze1.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze1.Config.ConfiguracaoFirebase;
import com.example.organizze1.Model.Movimentacao;
import com.example.organizze1.Model.Usuario;
import com.example.organizze1.R;
import com.example.organizze1.helper.Base64Custon;
import com.example.organizze1.helper.DateCustom;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EntradaActivity extends AppCompatActivity {
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;

    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFireBaseDataBase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        campoData = findViewById(R.id.editData);
        campoCategoria =findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);

        // colocar a data atual como padrao
        campoData.setText(DateCustom.dataAtual());

        recuperarReceitaTotal();
    }

    public void salvarReceita(View view){

        if(validarCamposReceita() ){

            movimentacao = new Movimentacao();

            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor( valorRecuperado );
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("R");


            double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizaReceita(receitaAtualizada);

            movimentacao.salvar(data);

            finish();

        }

    }

    public boolean validarCamposReceita(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        String textoCategoria =  campoCategoria.getText().toString();

        if(!textoValor.isEmpty() ){
            if (!textoData.isEmpty()){
                if (!textoDescricao.isEmpty()){
                    if(!textoCategoria.isEmpty()){
                        return true;

                    }
                    else{
                        Toast.makeText(EntradaActivity.this, "campo categoria não preenchido",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }

                }
                else{
                    Toast.makeText(EntradaActivity.this, "campo descricao não preenchido",
                            Toast.LENGTH_LONG).show();
                    return false;
                }

            }
            else {
                Toast.makeText(EntradaActivity.this, "campo data não preenchido",
                        Toast.LENGTH_LONG).show();
                return false;
            }

        }

        else{
            Toast.makeText(EntradaActivity.this, "campo valor não preenchido",
                    Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public void recuperarReceitaTotal(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custon.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizaReceita(Double receita){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custon.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);

    }
}