package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoAccesoMetodo extends NodoAccesoUnidad{

    public NodoAccesoMetodo(Token nombre){
        super(nombre);
    }

    @Override
    public void esta_bien_definido() {

    }
}
