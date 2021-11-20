package AST.Sentencia;

import AST.Acceso.NodoPrimario_Component;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import Traductor.Traductor;

import java.io.IOException;

public class Var_Local extends Var{

    protected NodoVarLocal variable;

    public Var_Local(NodoVarLocal variable, NodoPrimario_Component acceso_variable){
        super(acceso_variable);
        this.variable = variable;
    }
    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        if(!acceso_variable.es_lado_izq() || encadenado){
            //Si no es lado izquierdo o cadena no es nulo
            Traductor.getInstance().gen("LOAD "+variable.get_offset());
        }
        else {
            Traductor.getInstance().gen("STORE "+variable.get_offset());
        }
    }
}
