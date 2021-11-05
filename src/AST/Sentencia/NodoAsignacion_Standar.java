package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Acceso.NodoPrimario;
import AST.Acceso.NodoVarEncadenada;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public class NodoAsignacion_Standar extends NodoAsignacion{

    private NodoExpresion lado_der;

    public NodoAsignacion_Standar(NodoAcceso lado_izq, Token tipo_asignacion, NodoExpresion lado_der) {
        super(lado_izq, tipo_asignacion);
        this.lado_der = lado_der;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        super.esta_bien_definido();
        lado_der.esta_bien_definido();

        /*  Terminar control despues
        if(lado_izq instanceof NodoPrimario){
            if(((NodoPrimario) lado_izq).tiene_encadenados())
                if(!(((NodoPrimario) lado_izq).get_ultimo_encadenado() instanceof NodoVarEncadenada))
                    throw new ExcepcionTipo("El lado izquierdo de la asignacion debe ser una variable");
        }
        */

        lado_der.get_tipo_expresion().es_de_tipo(lado_izq.get_tipo_acceso());
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
