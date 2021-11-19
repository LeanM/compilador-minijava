package AST.Sentencia;

import AST.Acceso.NodoPrimario_Component;
import AST.Acceso.NodoVarEncadenada_Decorator;
import AnalizadorSemantico.EntradaAtributo;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Traductor;

import java.io.IOException;

public class Var_Instancia extends Var{

    protected EntradaAtributo variable;

    public Var_Instancia(EntradaAtributo variable, NodoPrimario_Component acceso_variable){
        super(acceso_variable);
        this.variable = variable;
    }

    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        Traductor.getInstance().gen("LOAD 3");
        if(!acceso_variable.es_lado_izq() || encadenado){
            Traductor.getInstance().gen("LOADREF "+variable.get_offset());
        }
        else {
            Traductor.getInstance().gen("SWAP");
            Traductor.getInstance().gen("STOREREF "+variable.get_offset());
        }
    }
}
