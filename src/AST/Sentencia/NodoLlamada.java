package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AnalizadorLexico.Token;

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
    public void esta_bien_definido() {
        nodo_acceso.esta_bien_definido();
    }
}
