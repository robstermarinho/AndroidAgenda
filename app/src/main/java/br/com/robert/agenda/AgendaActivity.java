package br.com.robert.agenda;

import android.content.Intent;
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

//No android as classes herdam de Uma Activity
public class AgendaActivity extends AppCompatActivity {

    private ListView ListaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // É necessário chamar o super para recuperar o comportamento de onCreate da classe pai
        super.onCreate(savedInstanceState);
        //R são os Resources - Recursos
        setContentView(R.layout.activity_agenda);
        ListaAlunos = (ListView) findViewById(R.id.lista_alunos);

        //Clique simples em algum item da lista
        ListaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) ListaAlunos.getItemAtPosition(position);
                Toast.makeText(AgendaActivity.this, "Clique em " + aluno.getNome(), Toast.LENGTH_SHORT).show();

                Intent intentVaiProFormulario = new Intent(AgendaActivity.this, FormularioActivity.class);
                intentVaiProFormulario.putExtra("aluno", aluno);
                startActivity(intentVaiProFormulario);

            }
        });
        //Clique longo no item da lista
        ListaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AgendaActivity.this, "Clique LONGO ", Toast.LENGTH_SHORT).show();
                return false;   //FALSE se vc quer que outros métodos utilizem
                                //TRUE se vc quer que só ele utilize
            }
        });


        //carregaLista();

        //Tenho que registrar a lista como alguém que tenha um menu de contexto
        registerForContextMenu(ListaAlunos);

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

        //Antes a lista de alunos era local, agora ela virou um atributo da classe para ser acesssado por todos.
        //ListView ListaAlunos = (ListView) findViewById(R.id.lista_alunos);
        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
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


/* caso eu tivesse um ID
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.){

        }

        return super.onContextItemSelected(item);
    }
*/
}
