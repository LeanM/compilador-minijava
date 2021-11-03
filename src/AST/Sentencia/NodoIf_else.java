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

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo If else");
        condicion.mostrar_expresion();
        cuerpo_then.mostar_sentencia();
        cuerpo_else.mostar_sentencia();
        System.out.println("-----------------------------------------------------------");
    }
}
