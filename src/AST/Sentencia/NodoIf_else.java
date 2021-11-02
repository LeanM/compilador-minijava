package AST.Sentencia;

import AST.Expresion.NodoExpresion;

public class NodoIf_else extends NodoIf {

    private NodoSentencia cuerpo_else;

    public NodoIf_else(NodoExpresion condicion, NodoSentencia then, NodoSentencia cuerpo_else) {
        super(condicion, then);
        this.cuerpo_else = cuerpo_else;
    }

    @Override
    public void esta_bien_definido() {

    }
}
