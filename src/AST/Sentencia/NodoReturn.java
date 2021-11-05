package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public class NodoReturn extends NodoSentencia {

    protected Token token_return;

    public NodoReturn(Token token_return){
        super();
        this.token_return = token_return;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        //nada
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("Nodo return : ");
    }
}
