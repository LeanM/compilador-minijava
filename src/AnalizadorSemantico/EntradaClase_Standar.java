package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Enumeration;
import java.util.LinkedList;

public class EntradaClase_Standar extends EntradaClase{

    protected Token clase_super;

    public EntradaClase_Standar(Token clase) {
        super(clase);
    }

    public void setClaseSuper(Token claseSuper){
        this.clase_super = claseSuper;
    }

    public Token getClaseSuper() {
        return clase_super;
    }

    public void esta_bien_declarada() throws ExcepcionSemantica {

        //Verifico que la clase super no sea null
        if(this.clase_super == null)
            throw new ExcepcionSemantica(token_clase,"Error Semantico en linea "+ token_clase.get_nro_linea() +": La clase "+this.getNombre()+" no hereda de ninguna clase.");

        herenciaCircular();

        if(lista_constructores.isEmpty()) {
            //agregar constructor predeterminado
            Token token_constructor = new Token("idClase",token_clase.get_lexema(),token_clase.get_nro_linea());
            EntradaConstructor constructor_predeterminado = new EntradaConstructor(token_constructor,new TipoReferencia(token_constructor));
            this.setConstructor(constructor_predeterminado.getNombre(),constructor_predeterminado);
        }
        else {
            for(EntradaConstructor ec : lista_constructores)
                ec.esta_bien_declarado();
        }

        Enumeration<EntradaAtributo> enum_atrs = tabla_atributos.elements();
        while(enum_atrs.hasMoreElements()){
            enum_atrs.nextElement().esta_bien_declarado();
        }

        Enumeration<LinkedList<EntradaMetodo>> enum_metodos = tabla_metodos.elements();
        while(enum_metodos.hasMoreElements()){
            for(EntradaMetodo em : enum_metodos.nextElement())
                em.esta_bien_declarado();
        }

    }

    public void herenciaCircular() throws ExcepcionSemantica {
        LinkedList<String> lista_ancestros = new LinkedList<String>();
        get_lista_ancestros(lista_ancestros);
        if (lista_ancestros.contains(this.getNombre()))
            throw new ExcepcionSemantica(token_clase,"Error Semantico en linea "+token_clase.get_nro_linea() +": La clase "+token_clase.get_lexema()+" posee herencia circular.");
    }

    protected void get_lista_ancestros(LinkedList<String> jerarquia_ancestros) throws ExcepcionSemantica {
        String ancestro = this.clase_super.get_lexema();
        if (TablaSimbolos.getInstance().clase_esta_declarada(ancestro)) {
            if (!jerarquia_ancestros.contains(ancestro)) {
                jerarquia_ancestros.add(ancestro);
                TablaSimbolos.getInstance().get_entrada_clase(clase_super.get_lexema()).get_lista_ancestros(jerarquia_ancestros);
            } else
                jerarquia_ancestros.add(ancestro);

        }
        else throw new ExcepcionSemantica(clase_super,"Error Semantico en linea "+ clase_super.get_nro_linea() +": La clase "+this.getNombre()+" hereda de una clase "+ancestro+" que no esta declarada.");
    }
}
