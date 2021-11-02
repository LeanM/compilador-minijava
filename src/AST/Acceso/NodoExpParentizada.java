package AST.Acceso;

import AST.Expresion.NodoExpresion;

public class NodoExpParentizada extends NodoPrimario {

    private NodoExpresion expresion_parentizada;

    public NodoExpParentizada(NodoExpresion exp){
        super();
        this.expresion_parentizada = exp;
    }

    @Override
    public void esta_bien_definido() {
        expresion_parentizada.esta_bien_definido();
    }
}
