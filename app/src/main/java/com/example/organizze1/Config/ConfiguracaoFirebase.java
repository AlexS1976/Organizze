package com.example.organizze1.Config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;

    // retorna a instancia do FireBaseDataBase
    public static DatabaseReference getFireBaseDataBase(){
        if( firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;

    }

    //retornar instancia do firebaseauth

    public static FirebaseAuth getFirebaseAutenticacao() {
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;
    }
}
