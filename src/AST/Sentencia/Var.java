package AST.Sentencia;

import AST.Acceso.NodoPrimario_Component;
import AST.Acceso.NodoVarEncadenada_Decorator;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

import java.io.IOException;

public abstract class Var {

    boolean encadenado;
    NodoPrimario_Component acceso_variable;

    public Var(NodoPrimario_Component acceso_variable){
        this.acceso_variable = acceso_variable;
        encadenado = !acceso_variable.es_ultimo_encadenado();
    }

    public abstract void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica;
}
