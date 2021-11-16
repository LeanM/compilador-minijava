package AST.Sentencia;

import AST.Acceso.NodoPrimario_Component;
import AST.Acceso.NodoVarEncadenada_Decorator;

import java.io.IOException;

public abstract class Var {

    boolean es_lado_izq, encadenado;
    NodoPrimario_Component acceso_variable;

    public Var(NodoPrimario_Component acceso_variable){
        this.acceso_variable = acceso_variable;
        encadenado = acceso_variable instanceof NodoVarEncadenada_Decorator;
    }

    public void set_es_lado_izq(){
        this.es_lado_izq = true;
    }
    public abstract void generar_codigo() throws IOException;
}
