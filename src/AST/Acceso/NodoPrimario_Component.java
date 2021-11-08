package AST.Acceso;

import AnalizadorLexico.Token;
import java.util.LinkedList;

public abstract class NodoPrimario_Component extends NodoAcceso{

    public NodoPrimario_Component(Token token_primario){
        super(token_primario);
    }

    @Override
    public void mostrar_acceso() {
        System.out.println("Nodo Acceso Primario :");
        System.out.println(token_acceso.get_lexema());
    }

}
