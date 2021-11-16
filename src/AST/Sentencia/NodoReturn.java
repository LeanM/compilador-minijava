package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

public class NodoReturn extends NodoSentencia {

    protected Token token_return;
    protected Tipo tipo_metodo;

    public NodoReturn(Token token_return, Tipo tipo_metodo){
        super();
        this.token_return = token_return;
        this.tipo_metodo = tipo_metodo;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        if(!tipo_metodo.getNombre().equals("void"))
            throw new ExcepcionTipo(token_return,"El metodo no es de tipo void");
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("Nodo return : ");
    }

    @Override
    public void generar_codigo() {

    }
}
