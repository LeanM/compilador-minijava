package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AnalizadorLexico.Token;

public class NodoAsignacion_Dec extends NodoAsignacion{

    public NodoAsignacion_Dec(NodoAcceso lado_izq, Token tipo_asignacion){
        super(lado_izq,tipo_asignacion);
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("NodoAsignacion_Dec : ");
        lado_izq.mostrar_acceso();
        System.out.println(tipo_asignacion.get_lexema());
        System.out.println("-----------------------------------------------------------");
    }
}
