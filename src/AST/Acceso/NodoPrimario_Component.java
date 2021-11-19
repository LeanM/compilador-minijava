package AST.Acceso;

import AnalizadorLexico.Token;
import java.util.LinkedList;

public abstract class NodoPrimario_Component extends NodoAcceso{

    protected boolean es_ultimo_encadenado;

    public NodoPrimario_Component(Token token_primario){
        super(token_primario);
        es_ultimo_encadenado = true;
    }

    @Override
    public void mostrar_acceso() {
        System.out.println("Nodo Acceso Primario :");
        System.out.println(token_acceso.get_lexema());
    }

    public void set_no_es_ultimo_encadenado() {
        this.es_ultimo_encadenado = false;
    }

    public boolean es_ultimo_encadenado() {
        return es_ultimo_encadenado;
    }

}
