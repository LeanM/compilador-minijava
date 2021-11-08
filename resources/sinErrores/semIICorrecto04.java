// mediante la llamada a un metodo directo accede encadenadamente a una public
class A {
    public int a1;
    
    dynamic void m1(int p1)
    {
        p1 = m3().a1;
    }
    
    dynamic void m2()
    {}
    
    dynamic A m3(){
        return new A();
    }
         
    

}




class Init{
    static void main()
    { }
}


