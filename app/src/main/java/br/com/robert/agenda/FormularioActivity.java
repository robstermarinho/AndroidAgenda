package br.com.robert.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.robert.agenda.dao.AlunoDAO;
import br.com.robert.agenda.modelo.Aluno;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        //Criando um Helper
        helper = new FormularioHelper(this);

        Button botaoSalvar = (Button) findViewById(R.id.formulario_salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FormularioActivity.this, "Salvo!", Toast.LENGTH_SHORT).show();
                finish(); //Finaliza e destrói activity
            }
        });
    }

    //Método criado para dizer quais são intens no menu que irão aparecer
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:

                //Pegar os dados em texto aqui mesmo
                /*
                EditText campoNome = (EditText) findViewById(R.id.formulario_nome);
                String nome = campoNome.getText().toString();

                EditText campoEndereco = (EditText) findViewById(R.id.formulario_endereco);
                String endereco = campoEndereco.getText().toString();

                EditText campoTelefone = (EditText) findViewById(R.id.formulario_telefone);
                String telefone = campoTelefone.getText().toString();

                EditText campoSite = (EditText) findViewById(R.id.formulario_site);
                String site = campoSite.getText().toString();
                */

                //pegar os dados em texto por uma classe separada
                Aluno aluno = helper.getAluno();
                AlunoDAO dao = new AlunoDAO(this);
                dao.insere(aluno);
                dao.close();

                Toast.makeText(FormularioActivity.this, aluno.getNome() + " salvo com sucesso!" , Toast.LENGTH_SHORT).show();

                //Não se preocupar em coisas do banco. instancia o DAO


                finish(); //Finaliza e destrói activity
                break;
            case R.id.menu_formulario_msg:
                Toast.makeText(FormularioActivity.this, "Mensagem linda!!", Toast.LENGTH_LONG).show();
                finish(); //Finaliza e destrói activity
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
