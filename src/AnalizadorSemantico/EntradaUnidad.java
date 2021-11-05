package AnalizadorSemantico;

import AST.NodoBloque;
import AST.Sentencia.NodoSentencia;
import AST.Sentencia.NodoVarLocal;

import java.util.Hashtable;
import java.util.LinkedList;

public abstract class EntradaUnidad {

        protected Hashtable<String,EntradaParametro> tabla_argumentos;
        protected LinkedList<EntradaParametro> lista_argumentos;
        protected Hashtable<String,NodoVarLocal> tabla_var_locales;
        protected NodoBloque bloque_sentencias;
        protected Tipo tipo_unidad;

        public EntradaUnidad(Tipo tipo_unidad){
            tabla_argumentos = new Hashtable<String,EntradaParametro>();
            lista_argumentos = new LinkedList<EntradaParametro>();
            bloque_sentencias = new NodoBloque(new LinkedList<NodoSentencia>()); //Inicializo con bloque vacio para evitar null
            tabla_var_locales = new Hashtable<String,NodoVarLocal>();
            this.tipo_unidad = tipo_unidad;
        }

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
            if(tabla_var_locales.containsKey(nombre_var))
                throw new ExcepcionSemantica(nodo.get_token(),"Ya hay una variable con el mismo nombre definida en el alcance");
            tabla_var_locales.put(nombre_var,nodo);
        }

        public Hashtable<String,NodoVarLocal> get_tabla_var_locales(){ return tabla_var_locales; }

        public Tipo get_tipo(){return tipo_unidad;}
        public void set_bloque_sentencias(NodoBloque bloque) {
            this.bloque_sentencias = bloque;
        }
        public NodoBloque get_bloque_sentencias(){
            return bloque_sentencias;
        }

        public LinkedList<EntradaParametro> get_lista_argumentos() {  return lista_argumentos;}
        public abstract void esta_bien_declarado() throws ExcepcionSemantica;
}
