package colocmining_ens;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.TreeSet;

public class PrevalentColocation {
    
    private TreeSet<String> elements;
    private double pi;
    private int instances;

    public PrevalentColocation(TreeSet<String> prevSet,double pi,int instNum){

        this.elements=prevSet;
        this.pi=pi;
        this.instances=instNum;
        
    }
    
    public TreeSet<String> getPrevCol(){
        
        return elements;
        
    }
    
    public double getPI(){
        
        return pi;
        
    }
    
    public int getInst(){
        
        return instances;
        
    }
    

    @Override
    public String toString(){

        DecimalFormat df=new DecimalFormat("#.####");
        return elements+"      (prev:"+df.format(pi)+")";

    } 
    
//    @Override
//    public int hashCode(){
//        
//        return elements.hashCode();
//        
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.elements);
        return hash;
    }

    
    
    
    @Override
    public boolean equals(Object obj) {
        
        PrevalentColocation otherCol=(PrevalentColocation)obj;
        return otherCol.getPrevCol().equals(this.getPrevCol()) && otherCol.getPI()==this.getPI() && otherCol.getInst()==this.getInst();
    }
    

}