package colocmining_ens;

import java.util.Objects;

public class NetworkObject implements Comparable{

    private String type;
    private int oid;
    private int id;
    private Edge e;
    private double pos;
    
    
    
    public NetworkObject(String type,int oid,int id,Edge e,double pos){
        
        this.type=type;
        this.oid=oid;
        this.id=id;
        this.e=e;
        this.pos=pos;
        
        
    }

    public String getType(){
        
        return type;
        
    }
    
    public int getOid(){
        
        return oid;
    }
    
    public int getID(){
        
        return id;
        
    }
   
    public Edge getEdge(){
        
        return e;
        
    }
    
    public double getPos(){
        
        return pos;
        
    }
    

//    public int HashCode(){
//        
//        return type.hashCode();
//        
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.type);
        return hash;
    }
    
    
    
    @Override
    public boolean equals(Object other){
        
        NetworkObject otherObj=(NetworkObject)other;
        
        return otherObj.getType().equals(this.getType()) && otherObj.getID()==this.getID();
        
    }

    @Override
    public String toString(){
        
        //return "type:"+type+" id:"+id+" e:"+e+" pos:"+pos;
        return type+"."+id+"";
    }

    @Override
    public int compareTo(Object other) {
        
        NetworkObject otherObj=(NetworkObject)other;
        
        return this.getType().compareTo(otherObj.getType());
        //return this.getID()-otherObj.getID();
        
    }
       
    
}



