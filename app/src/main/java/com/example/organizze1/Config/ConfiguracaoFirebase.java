package com.example.organizze1.Config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;

    //retornar instancia do firebaseauth

    public static FirebaseAuth getFirebaseAutenticacao() {
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;
    }
}
