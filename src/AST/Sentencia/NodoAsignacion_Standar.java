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
        super.esta_bien_definido();
        lado_der.esta_bien_definido();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo asignacion normal : ");
        lado_izq.mostrar_acceso();
        System.out.println(tipo_asignacion.get_lexema());
        lado_der.mostrar_expresion();
        System.out.println("-----------------------------------------------------------");
    }
}
