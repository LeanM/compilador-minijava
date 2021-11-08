package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public class NodoLlamada extends NodoSentencia{

    private NodoAcceso nodo_acceso;

    public NodoLlamada(NodoAcceso nodo) {
        super();
        this.nodo_acceso = nodo;
    }

    public NodoAcceso get_nodo_acceso(){
        return nodo_acceso;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        nodo_acceso.esta_bien_definido();
        if(nodo_acceso.puede_ser_asignado())
            throw new ExcepcionTipo(nodo_acceso.getToken(),"No es una sentencia correcta, la llamada debe ser a un metodo.");
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo llamada : ");
        nodo_acceso.mostrar_acceso();
        System.out.println("-----------------------------------------------------------");
    }
}
