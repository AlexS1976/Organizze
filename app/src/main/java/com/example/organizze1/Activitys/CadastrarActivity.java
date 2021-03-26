package com.example.organizze1.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze1.Config.ConfiguracaoFirebase;
import com.example.organizze1.Model.Usuario;
import com.example.organizze1.R;
import com.example.organizze1.helper.Base64Custon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastrarActivity extends AppCompatActivity {

    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;
    private Button botaoCadastrar;

    private FirebaseAuth autenticacao;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        getSupportActionBar().setTitle("Cadastro");

        campoNome = findViewById(R.id.editName);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);


        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){

                           usuario = new Usuario();
                           usuario.setNome(textoNome);
                           usuario.setEmail(textoEmail);
                           usuario.setSenha(textoSenha);

                            cadastrarUsuario();

                        }else{
                            Toast.makeText(CadastrarActivity.this,
                                    "preencha o campo senha", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(CadastrarActivity.this,
                                "Preencha o E-mail", Toast.LENGTH_LONG).show();
                    }

                } else{
                    Toast.makeText(CadastrarActivity.this,
                            "Preencha o nome", Toast.LENGTH_LONG).show();

                }


            }
        });
    }

    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String idUsuario = Base64Custon.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();

                    finish();
                }else{

                    String excecao = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        excecao ="Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um email válido";
                    }catch (FirebaseAuthUserCollisionException e ){
                        excecao = "Email já cadastrado";
                    }catch (Exception e ){
                        excecao = "erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastrarActivity.this, excecao, Toast.LENGTH_LONG).show();
                }

            }
        });


    }


}