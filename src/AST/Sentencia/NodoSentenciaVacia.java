package AST.Sentencia;

import AnalizadorLexico.Token;

public class NodoSentenciaVacia extends NodoSentencia{
    public NodoSentenciaVacia() {
        super();
    }

    @Override
    public void esta_bien_definido() {
        //nada
    }

    @Override
    public void mostar_sentencia() {
        //nada
    }
}
