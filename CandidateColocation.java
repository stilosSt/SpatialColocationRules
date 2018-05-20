package colocmining_ens;

import java.util.Objects;
import java.util.TreeSet;

public class CandidateColocation {

    
    TreeSet<String> elements;
    StarInstance sInstances;
    CliqueInstance cInstances;
    
    public CandidateColocation(TreeSet<String> aSet){
        
        this.elements=new TreeSet<>();
        this.elements.addAll(aSet);
      
    }
           
    public void addSI(StarInstance si){
        
        sInstances=si;
        
    }
    
    public void addCI(CliqueInstance ci){
        
        cInstances=ci;
        
    }
      
    public TreeSet<String> getCandidate(){
        
        return elements;
        
    }
    
    public StarInstance getSI(){
        
        return sInstances;
    }
    
    public CliqueInstance getCI(){
        
        return cInstances;
        
    }
    
    @Override
    public String toString(){
        
        return elements+"";
    }
    
//    @Override
//    public int hashCode(){
//        
//        return elements.hashCode();
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.elements);
        return hash;
    }
    
    
    
    @Override
    public boolean equals(Object obj) {
        
        CandidateColocation otherCand=(CandidateColocation)obj;
        return otherCand.getCandidate().equals(this.getCandidate());
    }
    
}

