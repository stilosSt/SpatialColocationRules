package colocmining_ens;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class CliqueInstance extends Instance{
        

    public CliqueInstance(CandidateColocation cc,ArrayList<TreeSet<NetworkObject>> nObjL){
             
        super(cc,nObjL);
        
    }
               
    
    public HashMap<Double,Integer> getPI(TreeMap<String,Integer> featuresSum){
        
        HashMap<String,Double> Pr=new HashMap<>(cC.getCandidate().size()); 
        HashMap<Double,Integer> retMap=new HashMap<>();
        HashSet<NetworkObject> tempSet;
        double pi;
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
                        //match=true;
                        break;                         
                    }
                }
            }
            //if(match){
                        
                pi=tempSet.size()/(double)featuresSum.get(elemType); 
                Pr.put(elemType,pi);
                //break;        
                
            //}                    
        }   
        
        
        double min=1.1;
       
        int inst=nObjList.size();
        
        for(String str:Pr.keySet()){
            
            if(Pr.get(str)<min){
                
                min=Pr.get(str);
            }
            
        }
        if(min>1){
            
            retMap.put(0.0,0);
            return retMap;
            
        }
        
        retMap.put(min,inst);
        return retMap;
    }       

}


