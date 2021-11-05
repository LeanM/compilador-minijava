package AST.Sentencia;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

public class NodoVarLocal extends NodoSentencia{

    protected Token variable;
    protected Tipo tipo;

    public NodoVarLocal(Tipo tipo, Token variable){
        super();
        this.tipo = tipo;
        this.variable = variable;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        //Controles semanticos
    }

    public Token get_token(){
        return variable;
    }

    public Tipo getTipo(){
        return tipo;
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("varLocal : "+variable.get_lexema());
    }
}
