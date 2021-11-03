package AST.Acceso;

import AnalizadorLexico.Token;

public abstract class NodoPrimario extends NodoAcceso{

    private NodoEncadenado encadenado;

    public NodoPrimario(Token token_primario){
        super(token_primario);
    }

    public void setEncadenado(NodoEncadenado encadenado){
        this.encadenado = encadenado;
    }

    @Override
    public void mostrar_acceso() {
        System.out.println("Nodo Acceso Primario :");
        System.out.println(token_acceso.get_lexema());
    }
}
