package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

public abstract class NodoAcceso {

    protected Token token_acceso;

    public NodoAcceso(Token token){
        this.token_acceso = token;

    }

    public Token getToken() { return token_acceso; }
    public abstract void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica;
    public abstract Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica;
    public boolean puede_ser_asignado(){
        return false;
    }
    public abstract void chequeo_acceso_estatico() throws ExcepcionSemantica, ExcepcionTipo;
    public abstract NodoPrimario_Concreto obtener_primario_concreto();
    public abstract void mostrar_acceso();
    public abstract void generar_codigo();
}
