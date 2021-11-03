package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AnalizadorLexico.Token;

public abstract class NodoAsignacion extends NodoSentencia {

    protected NodoAcceso lado_izq;
    protected Token tipo_asignacion;

    public NodoAsignacion (NodoAcceso lado_izq, Token tipo_asignacion){
        super();
        this.lado_izq = lado_izq;
        this.tipo_asignacion = tipo_asignacion;
    }

}
