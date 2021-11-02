package AnalizadorSemantico;

import AST.NodoBloque;

import java.util.Hashtable;
import java.util.LinkedList;

public abstract class EntradaUnidad {

        protected Hashtable<String,EntradaParametro> tabla_argumentos;
        protected LinkedList<EntradaParametro> lista_argumentos;
        protected NodoBloque bloque_sentencias;

        public EntradaUnidad(){
            tabla_argumentos = new Hashtable<String,EntradaParametro>();
            lista_argumentos = new LinkedList<EntradaParametro>();
        }

        public abstract void setArgumento(String nombre, EntradaParametro argumento) throws ExcepcionSemantica;
        //public abstract void setVariable(EntradaVariable variable);   No de esta etapa var locales

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

        public void set_bloque_sentencias(NodoBloque bloque) {
            this.bloque_sentencias = bloque;
        }

        public LinkedList<EntradaParametro> get_lista_argumentos() {  return lista_argumentos;}
        public abstract void esta_bien_declarado() throws ExcepcionSemantica;
}
