package AST.Sentencia;

import AnalizadorLexico.Token;
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
    public void esta_bien_definido() {

    }

    @Override
    public void mostar_sentencia() {
        System.out.println("varLocal : "+variable.get_lexema());
    }
}
