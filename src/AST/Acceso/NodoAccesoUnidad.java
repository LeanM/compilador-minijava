package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public abstract class NodoAccesoUnidad extends NodoPrimario {

    private LinkedList<NodoExpresion> argumentos;
    private Token token_nombre_unidad;

    public NodoAccesoUnidad(Token nombre){
        super();
        this.token_nombre_unidad = nombre;
        argumentos = new LinkedList<NodoExpresion>();
    }

    public void setArgumentos(LinkedList<NodoExpresion> argumentos){
        this.argumentos = argumentos;
    }
}
