package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

import java.io.IOException;

public abstract class NodoExpresion {

    protected Token token_expresion;

    public NodoExpresion(Token token){
        this.token_expresion = token;
    }

    public Token getToken(){ return token_expresion; }

    public abstract void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica;
    public abstract Tipo get_tipo_expresion() throws ExcepcionTipo, ExcepcionSemantica;
    public abstract void chequeo_acceso_estatico() throws ExcepcionTipo, ExcepcionSemantica;
    public abstract void mostrar_expresion();
    public abstract void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica;
    public abstract void set_lado_der();
}
