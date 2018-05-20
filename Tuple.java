package colocmining_ens;

import java.util.Objects;


public class Tuple {
    
    private Edge e1,e2;
    private String ET;
    private Double ED;
    
    public Tuple(Edge e1,Edge e2,String ET,double ED){
        
        this.e1=e1;
        this.e2=e2;
        this.ET=ET;
        this.ED=ED;
        
    }

    public Edge getE1() {
        return e1;
    }

    public Edge getE2() {
        return e2;
    }

    public String getET() {
        return ET;
    }

    public double getED() {
        return ED;
    }
    
//    @Override
//    public int hashCode(){
//        
//        return ED.hashCode();
//        
//        
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.ED);
        return hash;
    }
    
    
    

    @Override
    public boolean equals(Object other) {
        
        Tuple otherTuple=(Tuple)other;
        return otherTuple.getE1().equals(this.getE1()) && otherTuple.getE2().equals(this.getE2());
        
    }
              
 
    
}

