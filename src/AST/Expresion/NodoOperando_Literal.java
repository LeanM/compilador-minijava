package AST.Expresion;

import AnalizadorLexico.Token;
import Traductor.Traductor;

import java.io.IOException;

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

    public void chequeo_acceso_estatico() {
        //nada
    }

    public void generar_codigo() throws IOException {
        Traductor.getInstance().gen("PUSH "+token_expresion.get_lexema());
    }

    public void set_lado_der() {}
}
