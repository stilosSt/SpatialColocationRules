package colocmining_ens;

import java.util.TreeSet;


public class Transaction {
    
    private TreeSet<NetworkObject> transaction;
    
    public Transaction(){
        
        transaction=new TreeSet<>();

    }
    
    public void add(NetworkObject nOj){
        
        
        transaction.add(nOj);
        
    }
    
    public TreeSet<NetworkObject> getTrans(){
        
        
        return transaction;
        
    }
    
    @Override
    public String toString(){
        
       return ""+transaction;
    }
    
    
}

