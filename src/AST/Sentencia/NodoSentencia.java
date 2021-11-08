package AST.Sentencia;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public abstract class NodoSentencia {

    protected boolean en_metodo_static;

    public NodoSentencia(){
        en_metodo_static = false;
    }

    public void en_metodo_static() { this.en_metodo_static = true;}
    public abstract void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica;
    public abstract void mostar_sentencia();
}
