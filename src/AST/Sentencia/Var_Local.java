package AST.Sentencia;

import AST.Acceso.NodoPrimario_Component;
import AST.Acceso.NodoVarEncadenada_Decorator;
import AnalizadorSemantico.Traductor;

import java.io.IOException;

public class Var_Local extends Var{

    protected NodoVarLocal variable;

    public Var_Local(NodoVarLocal variable, NodoPrimario_Component acceso_variable){
        super(acceso_variable);
        this.variable = variable;
    }
    @Override
    public void generar_codigo() throws IOException {
        if(!es_lado_izq || encadenado){
            //Si no es lado izquierdo o cadena no es nulo
            Traductor.getInstance().gen("LOAD +variable.get_offset()"); //Implementar get offset en variables
        }
        else {
            Traductor.getInstance().gen("STORE +variable.get_offset()");
        }

        if(encadenado)
            acceso_variable.generar_codigo();
    }
}
