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
        varLocal.esta_bien_definido();
        condicion.esta_bien_definido();
        asignacion.esta_bien_definido();
        cuerpo_for.esta_bien_definido();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo for");
        varLocal.mostar_sentencia();
        condicion.mostrar_expresion();
        asignacion.mostar_sentencia();
        cuerpo_for.mostar_sentencia();
        System.out.println("-----------------------------------------------------------");
    }
}
