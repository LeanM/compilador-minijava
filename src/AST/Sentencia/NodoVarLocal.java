package AST.Sentencia;

import AST.NodoBloque;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoVarLocal extends NodoSentencia{

    protected Token var_local;
    protected Tipo tipo;
    protected NodoBloque bloque_var_local;
    protected EntradaUnidad unidad_var_local;
    protected int offset;

    public NodoVarLocal(Tipo tipo, Token variable, EntradaUnidad unidad, NodoBloque bloque){
        super();
        this.tipo = tipo;
        this.var_local = variable;
        this.unidad_var_local = unidad;
        this.bloque_var_local = bloque;
        this.offset = 0;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        //Controles semanticos
        var_local_bien_definido();

        if(!tipo.esPrimitivo())
            if(!TablaSimbolos.getInstance().get_tabla_clases().containsKey(tipo.getNombre()) || tipo.getNombre().equals("null"))
                throw new ExcepcionSemantica(var_local,"El tipo de la variable declarada es un tipo que no esta definido o es invalido (null)");
        else
            if(tipo.getNombre().equals("void"))
                throw new ExcepcionSemantica(var_local,"El tipo de la variable declarada es un tipo invalido (void)");
    }

    public void var_local_bien_definido() throws ExcepcionSemantica {
        NodoBloque padre = bloque_var_local.get_bloque_padre();

        if(unidad_var_local.get_tabla_argumentos().containsKey(var_local.get_lexema()))
            throw new ExcepcionSemantica(var_local, "No se pueden definir variables locales con el mismo nombre en el mismo alcance");

        if(bloque_var_local != unidad_var_local.get_bloque_principal()) {
            if(unidad_var_local.get_bloque_principal().get_tabla_var_locales().containsKey(var_local.get_lexema())) {
                if (unidad_var_local.get_bloque_principal().get_tabla_var_locales().get(var_local.get_lexema()).get_token().get_nro_linea() < var_local.get_nro_linea())
                    throw new ExcepcionSemantica(var_local, "No se pueden definir variables locales con el mismo nombre en el mismo alcance");
            }
            do {
                if (padre.get_tabla_var_locales().containsKey(var_local.get_lexema())) {
                    //La var local tambien esta definida en un bloque padre [si esta definida antes que la var local this --> error]
                    if (padre.get_tabla_var_locales().get(var_local.get_lexema()).get_token().get_nro_linea() < var_local.get_nro_linea())
                        throw new ExcepcionSemantica(var_local, "No se pueden definir variables locales con el mismo nombre en el mismo alcance");
                    break;
                } else padre = padre.get_bloque_padre();
            } while (padre != unidad_var_local.get_bloque_principal());
        }
    }

    public Token get_token(){
        return var_local;
    }
    public Tipo getTipo(){
        return tipo;
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("varLocal : "+var_local.get_lexema());
    }

    @Override
    public void generar_codigo() {

    }

    public int get_offset() { return offset; }
    public void set_offset(int offset) { this.offset = offset; }
}
