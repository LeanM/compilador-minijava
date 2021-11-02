package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AnalizadorLexico.Token;

public abstract class NodoAsignacion extends NodoSentencia {

    private NodoAcceso lado_izq;
    private Token tipo_asignacion;

    public NodoAsignacion (NodoAcceso lado_izq, Token tipo_asignacion){
        super();
        this.lado_izq = lado_izq;
        this.tipo_asignacion = tipo_asignacion;
    }

}
