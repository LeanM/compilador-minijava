package AST.Sentencia;

import AST.NodoBloque;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

import java.io.IOException;

public abstract class NodoSentencia {

    protected boolean en_metodo_static;
    protected String nombre_unidad_declarada;

    public NodoSentencia(){
        en_metodo_static = false;
        nombre_unidad_declarada = "";
    }

    public void en_metodo_static() { this.en_metodo_static = true;}
    public void set_nombre_unidad_declarada(String nombre) {
        this.nombre_unidad_declarada = nombre;
    }
    public abstract void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica;
    public abstract void mostar_sentencia();
    public abstract void generar_codigo() throws IOException, ExcepcionSemantica, ExcepcionTipo;
}
