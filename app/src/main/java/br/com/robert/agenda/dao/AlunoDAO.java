package br.com.robert.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.robert.agenda.modelo.Aluno;

/**
 * Created by robert on 22/12/2016.
 * CONEXÃO COM O BANCO DE DADOS
 */
//VERIFICAR SE JÁ EXISTE ou SE HOUVE MODIFICAÇÃO NAS COLUNAS DO BANCO já existentes etc
//POR ISSO EXTENDE SQLITEOPENHELPER

public class AlunoDAO extends SQLiteOpenHelper{
    /*  @Contexto: Contexto que vem de fora como parâmetro
    *   @name: Nome do Bnaco de dados (Definindo com "Agenda")
    *   @factory: Caso queira customizar aspectos do banco (factory default null)
    *   @versao: 1
    */
    //Construtor do SQLiteOpenHelper
    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 1);
    }

    //sempre que ele precisa criar o banco usa essse método
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL);";
        db.execSQL(sql);
    }

    //Atualiza versão do banco de dados que atualmmente está em 1
    //Caso haja um erro ou caso haja modificação da tabela ele atualiza para uma nova versão que deve ser especificada
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Alunos";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insere(Aluno aluno) {
        //String sql = "INSERT INTO Alunos (nome, endereco, telefone, site, note) VALUES (" + aluno.getNome();

        //pede uma referência ao banco de dados
        SQLiteDatabase db = getWritableDatabase();

        //Content de valores
        ContentValues dados = new ContentValues();

        //prepara os dados
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());

        //insere
        db.insert("Alunos", null, dados);
    }

    public List<Aluno> buscaAlunos() {
        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getWritableDatabase(); //referencia ao banco
        List<Aluno> alunos = new ArrayList<Aluno>(); //Lista de obj alunos
        Cursor c = db.rawQuery(sql, null); //O banco retorna um tipo CURSOR


        //Como o cursor é como se fosse um ponteiro que aponta pra primeira linha
        //eu preciso percorrer este cursor enquanto houver resultado

        while (c.moveToNext()){
            Aluno aluno = new Aluno(); //instancia aluno temporario
            String nome = c.getString(c.getColumnIndex("nome")); //pega a coluna nome
            aluno.setNome(nome); //seta no objeto

            aluno.setId(c.getLong(c.getColumnIndex("id")));
            aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
            aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
            aluno.setSite(c.getString(c.getColumnIndex("site")));
            aluno.setNota(c.getDouble(c.getColumnIndex("nota")));

            alunos.add(aluno);
        }
        c.close(); //lembrar de fechar o cursor
        return alunos;
    }
}
