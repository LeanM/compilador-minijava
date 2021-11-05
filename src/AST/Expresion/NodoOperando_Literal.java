package AST.Expresion;

import AnalizadorLexico.Token;

public abstract class NodoOperando_Literal extends NodoOperando {

    //private Token literal;

    public NodoOperando_Literal(Token literal){
        super(literal);
        //this.literal = literal;
    }

    public void mostrar_expresion(){
        System.out.println("Operando : ");
        System.out.println(token_expresion.get_lexema());
    }
}
