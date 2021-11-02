package AST.Sentencia;

import AST.Expresion.NodoExpresion;

public class NodoFor extends NodoSentencia {

    private NodoVarLocal varLocal;
    private NodoExpresion condicion;
    private NodoAsignacion asignacion;
    private NodoSentencia cuerpo_for;

    public NodoFor(NodoVarLocal var, NodoExpresion condicion, NodoAsignacion asignacion, NodoSentencia cuerpo_for){
        super();
        this.varLocal = var;
        this.condicion = condicion;
        this.asignacion = asignacion;
        this.cuerpo_for = cuerpo_for;
    }

    @Override
    public void esta_bien_definido() {

    }
}
