package AnalizadorSemantico;

import AST.NodoBloque;
import AST.Sentencia.NodoSentencia;
import AST.Sentencia.NodoVarLocal;
import AnalizadorLexico.Token;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

public abstract class EntradaUnidad {

        protected Hashtable<String,EntradaParametro> tabla_argumentos;
        protected LinkedList<EntradaParametro> lista_argumentos;
        protected Hashtable<String,NodoVarLocal> tabla_var_locales;
        protected NodoBloque bloque_principal;
        protected NodoBloque bloque_actual;
        protected Tipo tipo_unidad;
        protected Token token_unidad;

        public EntradaUnidad(Tipo tipo_unidad, Token token_unidad){
            tabla_argumentos = new Hashtable<String,EntradaParametro>();
            lista_argumentos = new LinkedList<EntradaParametro>();
            bloque_principal = new NodoBloque(this); //Inicializo con bloque vacio para evitar null
            bloque_actual = new NodoBloque(this);
            tabla_var_locales = new Hashtable<String,NodoVarLocal>();
            this.tipo_unidad = tipo_unidad;
            this.token_unidad = token_unidad;

        }

        public Token get_token_unidad(){ return token_unidad;}

        public void setArgumento(String nombre_argumento, EntradaParametro argumento) throws ExcepcionSemantica{
            if(!tabla_argumentos.containsKey(nombre_argumento)) {
                tabla_argumentos.put(nombre_argumento, argumento);
                lista_argumentos.addLast(argumento);
            }
            else throw new ExcepcionSemantica(argumento.get_token_parametro(),"Error Semantico en linea "+argumento.get_token_parametro().get_nro_linea() +": Ya hay un parametro declarado con el nombre "+nombre_argumento);
        }

        /*  Verifica que el metodo que recibe el mensaje tenga exactamente los mismos parametros que la lista de parametros
         *  parametrizada (que el orden y los tipos de cada parametro sea exactamente el mismo)
         */
        public boolean mismos_argumentos(LinkedList<EntradaParametro> lista_params_a_comparar) throws ExcepcionSemantica {
            boolean toReturn = true;
            if(lista_params_a_comparar.size() == this.lista_argumentos.size()) {
                for (int i = 0; i < lista_params_a_comparar.size() && toReturn; i++){
                    toReturn = lista_params_a_comparar.get(i).son_iguales(this.lista_argumentos.get(i));
                }
            }
            else toReturn = false;

            return toReturn;
        }

        public Hashtable<String,EntradaParametro> get_tabla_argumentos() { return tabla_argumentos; }

        public void set_var_local(String nombre_var, NodoVarLocal nodo) throws ExcepcionSemantica {
            tabla_var_locales.put(nombre_var,nodo);
        }

        public Tipo obtener_tipo_variable(Token token_variable, NodoBloque bloque_acceso_var){
            Tipo toReturn = null;
            boolean encontro = false;
            NodoBloque bloque_padre;
            if(bloque_acceso_var == bloque_principal){
                if(bloque_principal.get_tabla_var_locales().containsKey(token_variable.get_lexema()))
                    toReturn = bloque_principal.get_tabla_var_locales().get(token_variable.get_lexema()).getTipo();
            }
            else {

                if(bloque_acceso_var.get_tabla_var_locales().containsKey(token_variable.get_lexema()))
                    toReturn = bloque_acceso_var.get_tabla_var_locales().get(token_variable.get_lexema()).getTipo();
                else {
                    bloque_padre = bloque_acceso_var.get_bloque_padre();
                    while (bloque_padre != bloque_principal && !encontro) {
                        if(bloque_padre.get_tabla_var_locales().containsKey(token_variable.get_lexema())) {
                            toReturn = bloque_padre.get_tabla_var_locales().get(token_variable.get_lexema()).getTipo();
                            encontro = true;
                        }
                        else bloque_padre = bloque_padre.get_bloque_padre();
                    }
                    if(!encontro && bloque_principal.get_tabla_var_locales().containsKey(token_variable.get_lexema()))
                        toReturn = bloque_principal.get_tabla_var_locales().get(token_variable.get_lexema()).getTipo();
                }
            }
            return toReturn;
        }


        public Hashtable<String,NodoVarLocal> get_tabla_var_locales(){ return tabla_var_locales; }

        public Tipo get_tipo(){return tipo_unidad;}
        public void set_bloque_principal(NodoBloque bloque) {
            this.bloque_principal = bloque;
        }
        public NodoBloque get_bloque_principal(){
            return bloque_principal;
        }
        public NodoBloque get_bloque_actual() {
            return bloque_actual;
        }
        public void set_bloque_actual(NodoBloque bloque){
            this.bloque_actual = bloque;
        }

        public LinkedList<EntradaParametro> get_lista_argumentos() {  return lista_argumentos;}
        public String getNombre(){
        return token_unidad.get_lexema();
    }

        public abstract void esta_bien_declarado() throws ExcepcionSemantica;
        public abstract boolean no_retorna();
        public abstract int get_offset();
        public abstract String get_etiqueta();
        public abstract void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException;
}
