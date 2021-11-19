package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;

public class NodoExpresionUnaria extends NodoExpresion {

    protected NodoOperando operando;

    public NodoExpresionUnaria(Token op_unario, NodoOperando operando){
        super(op_unario);
        this.operando = operando;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        operando.esta_bien_definido();
        Resolucion_Tipos.getInstance().resolver_tipo_unario(token_expresion,operando.get_tipo_expresion());
    }

    @Override
    public Tipo get_tipo_expresion() throws ExcepcionTipo, ExcepcionSemantica {
        return Resolucion_Tipos.getInstance().resolver_tipo_unario(token_expresion,operando.get_tipo_expresion());
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionTipo, ExcepcionSemantica {
        operando.chequeo_acceso_estatico();
    }

    @Override
    public void mostrar_expresion() {
        System.out.println("Expresion Unaria : ");
        System.out.println(token_expresion.get_lexema());
        operando.mostrar_expresion();
    }

    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        operando.generar_codigo();

        switch (token_expresion.get_lexema()) {
            case "!" : Traductor.getInstance().gen("NOT");
            case "-" : Traductor.getInstance().gen("NEG");
            case "+" : Traductor.getInstance().gen("NOT");  //Hacerlo
        }
    }
}
