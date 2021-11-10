package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

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
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        lado_izq.esta_bien_definido();
        if(!lado_izq.get_tipo_acceso().getNombre().equals("int"))
            throw new ExcepcionTipo(tipo_asignacion,"Error de tipo : La asignacion -- solo puede aplicarse a tipos int");
    }
}
