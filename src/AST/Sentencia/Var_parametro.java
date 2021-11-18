package AST.Sentencia;

import AST.Acceso.NodoPrimario_Component;
import AnalizadorSemantico.EntradaParametro;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Traductor;

import java.io.IOException;

public class Var_parametro extends Var{

    protected EntradaParametro variable;

    public Var_parametro(EntradaParametro variable, NodoPrimario_Component acceso_variable){
        super(acceso_variable);
        this.variable = variable;
    }

    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        if(!acceso_variable.es_lado_izq() || encadenado){
            //Si no es lado izquierdo o cadena no es nulo
            Traductor.getInstance().gen("LOAD "+variable.get_offset()); //Implementar get offset en variables
        }
        else {
            Traductor.getInstance().gen("STORE "+variable.get_offset());
        }
    }
}
