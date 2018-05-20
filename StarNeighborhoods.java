package colocmining_ens;


/*creating the transcations Map based on Star Neighborhood partition model*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class StarNeighborhoods {

private HashSet<NeighborPair> ST;
private TreeMap<String,ArrayList<NetworkObject>> objectsMap;
private Transaction transaction;
private ArrayList<Transaction> transactionsList;
private TreeMap<String,ArrayList<Transaction>> starNeighborhoods;


        
    public StarNeighborhoods(HashSet<NeighborPair> ST,TreeMap<String,ArrayList<NetworkObject>> objectsMap){
        
        this.ST=ST;
        this.objectsMap=objectsMap;      
        starNeighborhoods=new TreeMap<>();    
        
    }
    
    public void getTransactions(){
        
   
        for(String type: objectsMap.keySet()){
            
            transactionsList=new ArrayList<>();
            
            for(NetworkObject nObj:objectsMap.get(type)){
               
                boolean hasPair=false;
                transaction=new Transaction();
                transaction.add(nObj);
                                    
                
                for(NeighborPair nP:ST){
                                        
                    
                    if(nP.getObj1().equals(nObj)){                               
                                
                        transaction.add(nP.getObj2());
                        hasPair=true;
                        
                    } 
                    else if(nP.getObj2().equals(nObj)){
                        
                       hasPair=true;
                   }    
                }
                
                if(hasPair){
                    
                transactionsList.add(transaction);
                
                }                 
            }
        
            starNeighborhoods.put(type,transactionsList);  
 
                        
        }
   
        /*for(String k:starNeighborhoods.keySet()){
            
            System.out.println(k +" "+starNeighborhoods.get(k).size());
            System.out.println(starNeighborhoods.get(k));
            
        }*/
            
    }
    
    
    public TreeMap<String,ArrayList<Transaction>> getStarNeigborhoods(){
        
        return starNeighborhoods;
        
    }
        
        
}

