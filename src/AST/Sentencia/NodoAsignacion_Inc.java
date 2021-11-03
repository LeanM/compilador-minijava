package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AnalizadorLexico.Token;

public class NodoAsignacion_Inc extends NodoAsignacion{

    public NodoAsignacion_Inc(NodoAcceso lado_izq, Token tipo_asignacion){
        super(lado_izq,tipo_asignacion);
    }

    @Override
    public void esta_bien_definido() {

    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("NodoAsignacion_Inc : ");
        lado_izq.mostrar_acceso();
        System.out.println(tipo_asignacion.get_lexema());
        System.out.println("-----------------------------------------------------------");
    }
}