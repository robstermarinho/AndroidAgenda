package br.com.robert.agenda;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.robert.agenda.modelo.Aluno;

/**
 * Created by robert on 22/12/2016.
 */

public class FormularioHelper  {

    private final EditText campoNome;
    private final RatingBar campoNota;
    private final EditText campoSite;
    private final EditText campoTelefone;
    private final EditText campoEndereco;

    public FormularioHelper(FormularioActivity activity){
        campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
    }

    public Aluno getAluno() {
        Aluno aluno = new Aluno();
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        return aluno;
    }
}
