package AST.Expresion;

import AST.Acceso.NodoAcceso;

public class NodoOperando_Acceso extends NodoOperando {

    private NodoAcceso nodo_acceso;

    public NodoOperando_Acceso(NodoAcceso acceso){
        super(acceso.getToken());
        this.nodo_acceso = acceso;
    }

    @Override
    public void esta_bien_definido() {
        nodo_acceso.esta_bien_definido();
    }

    @Override
    public void mostrar_expresion() {
        nodo_acceso.mostrar_acceso();
    }
}
