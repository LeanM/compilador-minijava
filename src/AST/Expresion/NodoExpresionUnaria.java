package AST.Expresion;

import AnalizadorLexico.Token;

public class NodoExpresionUnaria extends NodoExpresion {

    //private Token operador_unario;
    protected NodoOperando operando;

    public NodoExpresionUnaria(Token op_unario, NodoOperando operando){
        super(op_unario);
        this.operando = operando;
    }

    @Override
    public void esta_bien_definido() {
        operando.esta_bien_definido();
    }

    @Override
    public void mostrar_expresion() {
        System.out.println("Expresion Unaria : ");
        System.out.println(token_expresion.get_lexema());
        operando.mostrar_expresion();
    }
}
