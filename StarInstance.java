package colocmining_ens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class StarInstance extends Instance{
    

    
    public StarInstance(CandidateColocation cc,ArrayList<TreeSet<NetworkObject>> nObjL){
             
        super(cc,nObjL);
        
    }
    
    
    public double getPI(TreeMap<String,Integer> featuresSum){
        
        HashMap<String,Double> prMap=new HashMap<>(); 
        HashSet<NetworkObject> tempSet;
        double pr;
        //boolean match=false;
        
        for(String elemType:cC.getCandidate()){
            tempSet=new HashSet<>();

            for(TreeSet<NetworkObject> nObSet:nObjList){
            
                for(NetworkObject nObj:nObSet ){
                
                    if(nObj.getType().equals(elemType)){
                        
                        if(tempSet.contains(nObj)){
                            
                            break;
                        }
                        tempSet.add(nObj); 
                        break;                         
                    }
                }
            }
            
            pr=tempSet.size()/(double)featuresSum.get(elemType); 
            //System.out.println(pr);
            prMap.put(elemType,pr);               
        }   
      
        double min=1.1;
        
        for(String str:prMap.keySet()){
            
            if(prMap.get(str)<min){
                
                min=prMap.get(str);
            }
            
        }
        if(min>1){
            
            return 0;
            
        }
        
        return min;
    }      

    
}




