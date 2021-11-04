package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

public abstract class NodoAcceso {

    protected Token token_acceso;
    protected String key_clase;

    public NodoAcceso(Token token, String key_clase){
        this.token_acceso = token;
        this.key_clase = key_clase;
    }

    public Token getToken() { return token_acceso; }
    public abstract void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica;
    public abstract Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica;

    public abstract void mostrar_acceso();

}
