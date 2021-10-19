import AnalizadorSemantico.TablaSimbolos;
import kotlin.test.AssertionsKt;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ModuloPrincipalTest {

    private static String directorio_archivos = "Test/archivos-test-etapa3/";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private String errorCode;

    @Before
    public  void setUpClass() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public  void tearDownClass() {
        System.setOut(originalOut);
    }

    @Test
    public void main() {
        assertEquals("asd","asd");
        pruebas_etapa_3(0);
        pruebas_etapa_3(1);
        //pruebas_etapa_4();
    }

    void pruebas_etapa_3(int tipo_prueba){
        switch (tipo_prueba) {
            case 0 : clase_duplicada();
            case 1 : atributos_duplicados();
        }
    }

    void clase_duplicada () {
        //Prueba dos clases con el mismo nombre
        errorCode = "ya esta declarada";
        String [] args = {directorio_archivos+"clases-repetidas.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void atributos_duplicados() {
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "atributo declarado";
        String [] args = {directorio_archivos+"atr-repetidos.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }
}