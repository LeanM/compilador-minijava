///[Error:m2|10]
// Tipos incompatibles en el pasaje de parametros: B no conforma con String - ln: 10
class A {
    public int a1;
    public int v1;
    

    dynamic void m1(int p1) 
    {
        m2(p1+(v1-10), new B(), "hola!");
    }
    
    dynamic void m2(int p1, String x, B p2)
    {}
         
    A(){
        
    }
    

}

class B{
}


class Init{
    static void main()
    { }
}


