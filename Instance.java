package colocmining_ens;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeSet;

public class Instance{
    

    protected CandidateColocation cC;
    protected ArrayList<TreeSet<NetworkObject>> nObjList;
    

    public Instance(CandidateColocation cc,ArrayList<TreeSet<NetworkObject>> nObjL){
             
        this.cC=cc;
        this.nObjList=nObjL;
        
    }
               
    public CandidateColocation getInstCC(){
        
        return cC;
        
    }
    
    public ArrayList<TreeSet<NetworkObject>> getNobjL(){

        return nObjList;
    }
    
    
    @Override
    public String toString(){
        
        return cC+" "+nObjList;
    }      
    
//    @Override
//    public int hashCode(){
//        
//        return cC.hashCode();
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.cC);
        return hash;
    }
    

    @Override
    public boolean equals(Object obj) {
        
        CliqueInstance otherInst=(CliqueInstance)obj;
        return otherInst.getInstCC().equals(this.getInstCC()) && otherInst.getNobjL()==this.getNobjL();
    }
    

}

