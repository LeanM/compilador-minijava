package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;

public class NodoExpresionBinaria extends NodoExpresion {

    private NodoExpresion expresion_izq;
    private NodoExpresion expresion_der;

    public NodoExpresionBinaria(NodoExpresion exp_izq, Token op_binario, NodoExpresion exp_der){
        super(op_binario);
        this.expresion_izq = exp_izq;
        this.expresion_der = exp_der;
        //this.operador_binario = op_binario;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        expresion_izq.esta_bien_definido();
        expresion_der.esta_bien_definido();
        get_tipo_expresion();
    }

    @Override
    public Tipo get_tipo_expresion() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo tipo_izq = expresion_izq.get_tipo_expresion();
        Tipo tipo_der = expresion_der.get_tipo_expresion();

        return Resolucion_Tipos.getInstance().resolver_tipo_binario(tipo_izq,token_expresion,tipo_der);
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionTipo, ExcepcionSemantica {
        expresion_izq.chequeo_acceso_estatico();
        expresion_der.chequeo_acceso_estatico();
    }

    @Override
    public void mostrar_expresion() {
        System.out.println("Expresion Binaria : ");
        expresion_izq.mostrar_expresion();
        System.out.println(token_expresion.get_lexema());
        expresion_der.mostrar_expresion();
    }

    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        expresion_izq.generar_codigo();
        expresion_der.generar_codigo();

        switch (token_expresion.get_lexema()) {
            case "+" : Traductor.getInstance().gen("ADD");
            case "-" : Traductor.getInstance().gen("SUB");
            case "*" : Traductor.getInstance().gen("MUL");
            case "==" : Traductor.getInstance().gen("EQ");
            case "/" : Traductor.getInstance().gen("DIV");
            case "%" : Traductor.getInstance().gen("MOD");
            case "&&" : Traductor.getInstance().gen("AND");
            case "||" : Traductor.getInstance().gen("OR");
            case "!=" : {Traductor.getInstance().gen("NOT");}  //Hacerlo
            case "<" : Traductor.getInstance().gen("LT");
            case ">" : Traductor.getInstance().gen("GT");
            case "<=" : Traductor.getInstance().gen("LT"); //Hacerlo
            case ">=" : Traductor.getInstance().gen("GT");    //Hacerlo
        }
    }
}
