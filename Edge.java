package colocmining_ens;

import java.util.ArrayList;
import java.util.Objects;

public class Edge{
    
    private Integer id;
    private Node st_p,en_p;
    private Double weight;
    private ArrayList<NetworkObject> Oe;   

    
    
    
    public Edge(int AnID,Node sp,Node ep,double aWeight){
        
        id=AnID;
        st_p=sp;
        en_p=ep;
        weight=aWeight;
        Oe=new ArrayList<>();   

    }
    
    public void addSnappedObject(NetworkObject nObj){
        
        Oe.add(nObj);
        
    }
    
    
    
    public int getID(){
        
        return id;
        
    }
    
    public Node getStartNode(){
        
        return st_p;
        
    }
    
    public Node getEndNode(){
        
        return en_p;
        
    }
    
    public double getWeight(){
        
        return weight;
        
    }
    
    
    public ArrayList<NetworkObject> getObjects(){
        
        return Oe;
        
    }
            
    
//    @Override
//    public int hashCode(){
//        
//        return weight.hashCode();
//
//    }

    
    @Override
    public int hashCode() {
        
        int hash=7;
        hash = 29 * hash + Objects.hashCode(this.weight);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        Edge otherEdge=(Edge)other;
        return otherEdge.getID()==(this.getID());
    }
    
    @Override
    public String toString(){
        
        return "ID:"+id+" ST_P:"+st_p+" EN_P:"+en_p+" W:"+weight;
        
    }
    
    

    
    /*@Override
    public int compareTo(Object other) {
        Edge otherEdge=(Edge)other;
        if(id < otherEdge.id){
            
            return 1;
        }
        else if(id > otherEdge.id){
            
            return -1;
        }
        else{
            
            return 0;
        }
        
    }*/
    
    
}

