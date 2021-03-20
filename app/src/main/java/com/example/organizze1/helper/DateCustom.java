package com.example.organizze1.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){

        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }

    public static String mesAnoDataEscolhida(String data){
        String retornaData [] = data.split("/");
        String dia = retornaData [0];
        String mes = retornaData [1];
        String ano = retornaData [2];

        String mesAno = mes + ano;
        return  mesAno;
    }


}
