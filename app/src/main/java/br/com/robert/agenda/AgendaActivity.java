package br.com.robert.agenda;

import android.Manifest;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Browser;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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

    //Menu de contexto
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        //ContextMenuInfo menuInfo vai ter as informações do adapeter
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) ListaAlunos.getItemAtPosition(info.position);


        //ITEM 1 -EXCLUIR###########################################  Criando menu sem o inflate
        MenuItem deletar = menu.add("Excluir");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(AgendaActivity.this) //DIALOG
                        .setTitle("Excluir Registro")
                        .setMessage("Tem certeza que deseja excluir registro?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                AlunoDAO dao = new AlunoDAO(AgendaActivity.this);
                                dao.deleta(aluno);
                                dao.close();

                                carregaLista();
                                Toast.makeText(AgendaActivity.this, aluno.getNome() + " foi excluído.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .show();

                return false;
            }
        });

        //ITEM 2 -Visitar Site ##################################################################
        MenuItem itemSite = menu.add("Visitar Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW); //Cria uma intent implícita e especifica que quer ver uma action view
        String site = aluno.getSite();              //pega o site
        if (!site.startsWith("http://")) {
            site = "http://" + site;
        }

        //intentSite.setData(Uri.parse("http://www.google.com"));
        intentSite.setData(Uri.parse(site)); //especificando o recurso que quero ver URI

        //Chamar Intent a partir do item automaticamente
        itemSite.setIntent(intentSite);


        //ITEM 3 - ###########################################  Enviar SMS
        MenuItem itemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        itemSMS.setIntent(intentSMS);

        //ITEM 4- ###########################################  Visualizar no mapa
        MenuItem itemMapa = menu.add("Visualizar no Mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        itemMapa.setIntent(intentMapa);

        //ITEM 5- ###########################################  Ligar
        /* Necessário pedir permissão ao usuário
        * */

        MenuItem itemLigar = menu.add("Ligar");
        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Verifica se a permissão de CALL_PHONE já foi GRANTED ou garantida pelo usuário naquele momento específico
                if (ActivityCompat.checkSelfPermission(AgendaActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //pedindo a permissão
                    //A partir desse ele chama o OnRequestPermissionResult que eu posso sobrescrever
                    ActivityCompat.requestPermissions(AgendaActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 123);

                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return TODO;
                }else{
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }

                return false;
            }
        });

    }


    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);          //Populando lista pelo código via DAO
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos); //Array adapter para transformar os objetos em componentes da view
        ListaAlunos.setAdapter(adapter);
    }
    @NonNull
    private ListView carregaListaString() {
        String[] alunos = {"Robert", "Albert", "Cássia", "Victória", "Graça", "Lúcia"};             //Populando lista pelo código via string
        ListView ListaAlunos = (ListView) findViewById(R.id.lista_alunos);                          //Casting pois eu tenho certeza que o tipo View genérico retornado pe função será do tipo ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alunos);
        ListaAlunos.setAdapter(adapter);
        return ListaAlunos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 123){
            Toast.makeText(AgendaActivity.this, "perimssão resultado: " + permissions, Toast.LENGTH_SHORT).show();
            //requestCode específico para cara permissão
        }

    }
}
