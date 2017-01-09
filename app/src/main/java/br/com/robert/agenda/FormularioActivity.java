package br.com.robert.agenda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import br.com.robert.agenda.dao.AlunoDAO;
import br.com.robert.agenda.modelo.Aluno;

public class FormularioActivity extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 567;
    private FormularioHelper helper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);


        helper = new FormularioHelper(this);                        //Criando um Helper para trabalhar com o formulário
        Intent intent =  getIntent();                               //recebe algum dado extra via intent
        final Aluno aluno = (Aluno) intent.getSerializableExtra("aluno"); //Dado extra vindo da label aluno

        if(aluno !=null){                       //ou seja se não for um novo aluno. Se for a página de editar
            helper.preencheFormulario(aluno);
        }

        Button botaoFoto = (Button) findViewById(R.id.formulario_botao_foto);
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //Invocando a Câmera
                //Passa o caminho da foto no diretório do aplicativo com um numero unico baseado no timestamp
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                //Ele vai chamar o OnActivityResult

                //Criar constante
                //refactor/extract/contant/CODIGO_CAMERA
                startActivityForResult(intentCamera, CODIGO_CAMERA);
                //CODIGO_CAMERA = 567 é o identificador para quando eu chamar o OnActivityResult
            }
        });
        /*
        Button botaoSalvar = (Button) findViewById(R.id.formulario_salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FormularioActivity.this, "Salvo!", Toast.LENGTH_SHORT).show();
                finish(); //Finaliza e destrói activity
            }
        });
       */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) { // Se a ação de tirar a foto foi completada
            if (requestCode == CODIGO_CAMERA) { //Se for o código do botão da câmera
                //Abrir a foto tirada
                helper.carregaImagem(caminhoFoto);


            }
        }
    }

    //Método criado para dizer quais são itens no menu que irão aparecer
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

                AlunoDAO dao = new AlunoDAO(this);
                Aluno aluno = helper.getAluno();            //pegar os dados em texto

                if(aluno.getId() !=null){                   //Se for um aluno que já tenha ID - EDITAR
                    dao.altera(aluno);
                    Toast.makeText(FormularioActivity.this, aluno.getNome() + " alterado com sucesso!" , Toast.LENGTH_SHORT).show();
                }else {
                    dao.insere(aluno);
                    Toast.makeText(FormularioActivity.this, aluno.getNome() + " salvo com sucesso!" , Toast.LENGTH_SHORT).show();
                }
                dao.close();
                finish(); //Finaliza e destrói activity
                break;

            case R.id.menu_formulario_msg:
                Toast.makeText(FormularioActivity.this, "Simples mensagem!!", Toast.LENGTH_LONG).show();
                finish(); //Finaliza e destrói activity
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
