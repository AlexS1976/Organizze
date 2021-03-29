package com.example.organizze1.Activitys;

import android.content.Intent;
import android.os.Bundle;

import com.example.organizze1.Adapter.AdapterMovimentacao;
import com.example.organizze1.Config.ConfiguracaoFirebase;
import com.example.organizze1.Model.Movimentacao;
import com.example.organizze1.Model.Usuario;
import com.example.organizze1.helper.Base64Custon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.organizze1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView saudacao;
    private TextView saldo;
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;



    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFireBaseDataBase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener eventListenerMovimentacoes;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        saudacao = findViewById(R.id.textSaudacao);
        saldo = findViewById(R.id.textSaldo);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recicleMovimentos);

        configuraCalendarWiew();
        //configurara adapter

        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        //config recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);



 }
    public void recuperarMovimentacoes(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custon.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("movimentacao")
                                    .child(idUsuario)
                                    .child(mesAnoSelecionado);
        Log.i("teste", mesAnoSelecionado);

        eventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                movimentacoes.clear();
                for (DataSnapshot dados: snapshot.getChildren()) {

                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacoes.add(movimentacao);
                }
               adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    public  void recuperarResumo(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custon.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format(resumoUsuario);

                saudacao.setText("olá, " + usuario.getNome());
                saldo.setText("R$ " + resultadoFormatado);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:

                autenticacao.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarReceita(View view){

        startActivity(new Intent(this, EntradaActivity.class));

    }

    public void adicionarDespesa(View view){

        startActivity(new Intent(this, SaidaActivity.class));

    }

    public void configuraCalendarWiew(){
        CharSequence meses[] = {"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"};
        calendarView.setTitleMonths(meses);

        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth()) );
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth() ) );
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());

                movimentacaoRef.removeEventListener(eventListenerMovimentacoes);
                recuperarMovimentacoes();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();

    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(eventListenerMovimentacoes);


    }
}