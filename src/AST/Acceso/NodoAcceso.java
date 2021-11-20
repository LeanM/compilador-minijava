package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

import java.io.IOException;

public abstract class NodoAcceso {

    protected Token token_acceso;
    protected boolean es_lado_izq;

    public NodoAcceso(Token token){
        this.token_acceso = token;
        this.es_lado_izq = false;

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
    public abstract void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica;

    public void set_lado_izq() {
        this.es_lado_izq = true;
    }
    public void set_lado_der() {this.es_lado_izq = false;}
    public boolean es_lado_izq() {
        return es_lado_izq;
    }
}
