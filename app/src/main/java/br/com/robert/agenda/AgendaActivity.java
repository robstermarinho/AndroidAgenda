package br.com.robert.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import br.com.robert.agenda.dao.AlunoDAO;
import br.com.robert.agenda.modelo.Aluno;

//No android as classes herdam de Uma Activity
public class AgendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // É necessário chamar o super para recuperar o comportamento de onCreate da classe pai
        super.onCreate(savedInstanceState);
        //R são os Resources - Recursos
        setContentView(R.layout.activity_agenda);

        //carregaLista();


/*
        //Populando lista pelo código via string
        String[] alunos = {"Robert", "Albert", "Cássia", "Victória", "Graça", "Lúcia", "Thales", "Joana", "Duda", "Nuna", "Net","Robert", "Albert", "Cássia", "Victória", "Graça", "Lúcia", "Thales", "Joana", "Duda", "Nuna", "Net"};
        //Casting pois eu tenho certeza que o tipo View genérico retornado pe função será do tipo ListView
        ListView ListaAlunos = (ListView) findViewById(R.id.lista_alunos);
        //Array adapter para transformar essas strings em componentes da view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alunos);
        ListaAlunos.setAdapter(adapter);
*/



        //Instancia botão pelo ID
        Button novoAluno = (Button) findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria uma intenção para uma nova tela
                Intent intentVaiProFormulario = new Intent(AgendaActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

    }


    /* É necessário escrever novamente a ação de buscar a lista dentro do onResume do android
    *  pq é a partir dele que a a activity volta depois de uma pausa
    */
    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }
    private void carregaLista() {
        //Populando lista pelo código via DAO
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        ListView ListaAlunos = (ListView) findViewById(R.id.lista_alunos);
        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        ListaAlunos.setAdapter(adapter);
    }


}
