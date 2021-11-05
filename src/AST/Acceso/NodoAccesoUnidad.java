package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public abstract class NodoAccesoUnidad extends NodoPrimario {

    protected LinkedList<NodoExpresion> argumentos;

    public NodoAccesoUnidad(Token nombre, LinkedList<NodoExpresion> args, String key_clase){
        super(nombre,key_clase);
        argumentos = args;
    }
    public void setArgumentos(LinkedList<NodoExpresion> argumentos){
        this.argumentos = argumentos;
    }

    public boolean conforma_argumentos(LinkedList<EntradaParametro> argumentos_formales) throws ExcepcionTipo, ExcepcionSemantica {
        boolean iguales = true;

        if(argumentos.size() == argumentos_formales.size()) {
            iguales = true;
            for (int i = 0; i < argumentos.size() && iguales; i++) {
                if(!this.argumentos.get(i).get_tipo_expresion().es_de_tipo(argumentos_formales.get(i).get_tipo()))
                    iguales = false;
            }
        }
        else
            iguales = false;

        return iguales;
    }
}
