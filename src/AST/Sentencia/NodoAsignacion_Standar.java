package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

public class NodoAsignacion_Standar extends NodoAsignacion{

    private NodoExpresion lado_der;

    public NodoAsignacion_Standar(NodoAcceso lado_izq, Token tipo_asignacion, NodoExpresion lado_der) {
        super(lado_izq, tipo_asignacion);
        this.lado_der = lado_der;
    }

    @Override
    public void esta_bien_definido() {

    }
}
