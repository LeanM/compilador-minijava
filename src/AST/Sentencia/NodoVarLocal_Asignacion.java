package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

public class NodoVarLocal_Asignacion extends NodoVarLocal{

    private NodoExpresion expresion_de_asignacion;

    public NodoVarLocal_Asignacion(Tipo tipo, Token variable) {
        super(tipo, variable);
    }
    public NodoVarLocal_Asignacion(Tipo tipo, Token variable,NodoExpresion exp) {
        super(tipo, variable);
        this.expresion_de_asignacion = exp;
    }

    public void set_expresion(NodoExpresion exp) {
        this.expresion_de_asignacion = exp;
    }

    @Override
    public void esta_bien_definido() {

    }
}
