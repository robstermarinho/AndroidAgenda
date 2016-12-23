package br.com.robert.agenda;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.robert.agenda.dao.AlunoDAO;
import br.com.robert.agenda.modelo.Aluno;


public class AgendaActivity extends AppCompatActivity {                                 //No android as classes herdam de Uma Activity


    private ListView ListaAlunos;                                                       //Lista de Alunos

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);                                             //É necessário recuperar o comportamento do onCreate da classe pai
        setContentView(R.layout.activity_agenda);                                       //R são os Resources que recuperam layout em XML
        ListaAlunos = (ListView) findViewById(R.id.lista_alunos);

        ListaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {      //Clique simples em algum item da lista
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) ListaAlunos.getItemAtPosition(position);          //Recupera a posição do item na lista

                Intent intentVaiProFormulario = new Intent(AgendaActivity.this, FormularioActivity.class); //Intenção para uma Activity formulário
                intentVaiProFormulario.putExtra("aluno", aluno);                        //Adiciona conteúdo extra para essa intent pra que seja recuperad
                startActivity(intentVaiProFormulario);
            }
        });

        ListaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//Clique longo no item da lista
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AgendaActivity.this, "Clique LONGO ", Toast.LENGTH_SHORT).show();
                return false;                                                             //FALSE se vc quer que outros métodos utilizem | TRUE se vc quer que só ele utilize
            }
        });

        registerForContextMenu(ListaAlunos);                                              //Registrar a lista como alguém que tenha um menu de contexto



        Button novoAluno = (Button) findViewById(R.id.novo_aluno);                         //Instancia botão pelo ID
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria uma intenção para uma nova tela
                Intent intentVaiProFormulario = new Intent(AgendaActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

        /***Lista carregada no onResume(); ***/
    }

    /* É necessário sbrescrever o método onResume para que ele descreva a ação de buscar a lista.
    *  No android, é a partir desse método que a a activity volta depois de uma pausa
    */
    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    private void carregaLista() {

        AlunoDAO dao = new AlunoDAO(this);          //Populando lista pelo código via DAO
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos); //Array adapter para transformar os objetos em componentes da view
        ListaAlunos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        //Criando menu sem o inflate
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //ContextMenuInfo menuInfo vai ter as informações do adapeter
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                Aluno aluno = (Aluno) ListaAlunos.getItemAtPosition(info.position);

                AlunoDAO dao = new AlunoDAO(AgendaActivity.this);
                dao.deleta(aluno);
                dao.close();

                carregaLista();
                Toast.makeText(AgendaActivity.this, aluno.getNome() + " foi excluído.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @NonNull
    private ListView carregaListaString() {
        String[] alunos = {"Robert", "Albert", "Cássia", "Victória", "Graça", "Lúcia"};             //Populando lista pelo código via string
        ListView ListaAlunos = (ListView) findViewById(R.id.lista_alunos);                          //Casting pois eu tenho certeza que o tipo View genérico retornado pe função será do tipo ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alunos);
        ListaAlunos.setAdapter(adapter);
        return ListaAlunos;
    }
}
