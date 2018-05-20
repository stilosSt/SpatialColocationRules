package colocmining_ens;

//import java.util.ArrayList;

import java.util.Objects;


public class NeighborPair{
    
    //private ArrayList<NetworkObject> transaction;
    private Integer id;
    private NetworkObject netObj1;
    private NetworkObject netObj2;
    private double ND;
    
    public NeighborPair(int id,NetworkObject obj1,NetworkObject obj2,double netDist){
        
        this.id=id;
        this.netObj1=obj1;
        this.netObj2=obj2;
        this.ND=netDist;
        
    }
    
    public int getID(){
        
        return id;
        
    }
            
    public NetworkObject getObj1(){
        
        return netObj1;
        
    } 
    
    public NetworkObject getObj2(){
        
        return netObj2;
        
    } 
    
    public double getND(){
        
        return ND;
        
    }
    
       
//    public int HashCode(){
//        
//       return netObj1.getType().hashCode(); 
//               
//    }
    
    @Override
    public int hashCode(){
        
        int hash=3;
        hash = 59 * hash + Objects.hashCode(this.netObj1.getType());
        return hash;
        
    }

    @Override
    public boolean equals(Object other) {
        NeighborPair otherTransaction=(NeighborPair) other;
        return otherTransaction.getObj1().equals(this.getObj1()) && otherTransaction.getObj2().equals(this.getObj2());
    }
    
    public String toString(){
        
        return "id:"+id+" obj1:"+netObj1+" obj2:"+netObj2;
        
    }

    
            
}

