package AST.Acceso;

import AnalizadorLexico.Token;

import java.util.LinkedList;

public abstract class NodoPrimario extends NodoAcceso{

    protected LinkedList<NodoEncadenado> lista_encadenados;

    public NodoPrimario(Token token_primario, String key_clase){
        super(token_primario,key_clase);
    }

    public void setEncadenado(NodoEncadenado encadenado){
        this.lista_encadenados.addLast(encadenado);
    }
    public LinkedList<NodoEncadenado> get_lista_encadenados(){
        return lista_encadenados;
    }
    public int get_pos_en_encadenados(NodoEncadenado encadenado){
        int toReturn = 0;
        for(int i = 0; i < lista_encadenados.size() ; i++)
            if(lista_encadenados.get(i) == encadenado) {
                toReturn = i;
                break;
            }
        return toReturn;
    }

    public NodoEncadenado get_encadenado_izq(NodoEncadenado encadenado_der){
        NodoEncadenado toReturn;
        int pos = 0;
        for(int i = 0; i < lista_encadenados.size() ; i++)
            if(lista_encadenados.get(i) == encadenado_der) {
                pos = i-1;
                break;
            }
        if(pos > 0)
            toReturn = lista_encadenados.get(pos);
        else
            toReturn = lista_encadenados.get(0);

        return toReturn;
    }

    public boolean tiene_encadenados(){
        return !lista_encadenados.isEmpty();
    }

    public NodoEncadenado get_ultimo_encadenado(){
        return lista_encadenados.getLast();
    }

    @Override
    public void mostrar_acceso() {
        System.out.println("Nodo Acceso Primario :");
        System.out.println(token_acceso.get_lexema());
    }
}
