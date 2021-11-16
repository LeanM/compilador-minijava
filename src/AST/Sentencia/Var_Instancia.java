package AST.Sentencia;

import AST.Acceso.NodoPrimario_Component;
import AnalizadorSemantico.EntradaAtributo;
import AnalizadorSemantico.Traductor;

import java.io.IOException;

public class Var_Instancia extends Var{

    protected EntradaAtributo variable;

    public Var_Instancia(EntradaAtributo variable, NodoPrimario_Component acceso_variable){
        super(acceso_variable);
        this.variable = variable;
    }

    @Override
    public void generar_codigo() throws IOException {
        Traductor.getInstance().gen("LOAD 3");
        if(!es_lado_izq || !encadenado){
            Traductor.getInstance().gen("LOADREF variable.offset()");
        }
        else {
            Traductor.getInstance().gen("SWAP");
            Traductor.getInstance().gen("STOREREF variable.offset()");
        }

        if(encadenado)
            acceso_variable.generar_codigo();
    }
}
