package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoEstatico extends NodoPrimario_Concreto{

    protected Tipo tipo_estatico;
    protected NodoPrimario_Component encadenado_estatico;
    public NodoAccesoEstatico(Tipo tipo_estatico, String key_clase){
        super(tipo_estatico.get_token_tipo(),key_clase);
        this.tipo_estatico = tipo_estatico;
    }

    public void set_encadenado_estatico(NodoPrimario_Component encadenado_estatico){
        this.encadenado_estatico = encadenado_estatico;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionSemantica, ExcepcionTipo {
        //Verificar semanticas de acceso estatico
        if(!TablaSimbolos.getInstance().clase_esta_declarada(token_acceso.get_lexema()))
            throw new ExcepcionSemantica(token_acceso,"La clase "+token_acceso.get_lexema()+" no esta declarada.");

        if(encadenado_estatico instanceof NodoMetodoEncadenado_Decorator){
            EntradaMetodo metodo_conforma = TablaSimbolos.getInstance().conforma_metodo(encadenado_estatico.token_acceso, ((NodoMetodoEncadenado_Decorator) encadenado_estatico).argumentos,key_clase);
            if(metodo_conforma == null)
                throw new ExcepcionTipo(token_acceso,"La llamada a metodo "+encadenado_estatico.getToken().get_lexema()+" no conforma con ningun metodos de la clase del encadenado de la izquierda ( "+tipo_estatico.getNombre()+" )");
            if(!metodo_conforma.es_estatico())
                throw new ExcepcionSemantica(token_acceso,"No existe un metodo estatico "+encadenado_estatico.getToken().get_lexema()+" definido en "+tipo_estatico.getNombre());
        }
        else {
            if (encadenado_estatico instanceof NodoVarEncadenada_Decorator) {
                EntradaAtributo atributo_conforma = TablaSimbolos.getInstance().conforma_atributo(encadenado_estatico.token_acceso, key_clase);
                if (atributo_conforma == null)
                    throw new ExcepcionTipo(token_acceso, "La llamada a atributo " + encadenado_estatico.getToken().get_lexema() + " no conforma con ningun metodos de la clase del encadenado de la izquierda ( " + tipo_estatico.getNombre() + " )");
                if (!atributo_conforma.es_estatico())
                    throw new ExcepcionSemantica(token_acceso, "No existe un atributo estatico " + encadenado_estatico.getToken().get_lexema() + " definido en " + tipo_estatico.getNombre());
            }
        }
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        return tipo_estatico;
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionSemantica, ExcepcionTipo {
        //nada
    }


}
