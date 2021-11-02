package AST.Sentencia;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

public class NodoVarLocal extends NodoSentencia{

    private Token variable;
    private Tipo tipo;

    public NodoVarLocal(Tipo tipo, Token variable){
        super();
        this.tipo = tipo;
        this.variable = variable;
    }

    @Override
    public void esta_bien_definido() {

    }
}
