package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

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
}
