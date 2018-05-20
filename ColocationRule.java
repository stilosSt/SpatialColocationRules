package colocmining_ens;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.TreeSet;

public class ColocationRule {
    
    private TreeSet<String> LHS;
    private TreeSet<String> RHS;
    private double condProb;
    
    public ColocationRule(TreeSet<String> LHS,TreeSet<String> RHS,double condProb){
        
        this.LHS=new TreeSet<>(LHS);
        this.RHS=new TreeSet<>(RHS);
        this.condProb=condProb;
           
    }
    
    public TreeSet<String> getLHS(){
        
        return LHS;
    }
    
    public TreeSet<String> getRHS(){
        
        return RHS;
    }
    
    public double getCondProb(){
        
        return condProb;
    }
    
    @Override
    public String toString(){
        DecimalFormat df=new DecimalFormat("#.####");
        return LHS+"--->"+RHS+"      (Cond_Prob:"+df.format(condProb)+")";
    }
    
//    @Override
//    public int hashCode(){
//        
//        return RHS.hashCode();
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.RHS);
        return hash;
    }
    
    

    @Override
    public boolean equals(Object obj) {
       
        ColocationRule otherCR=(ColocationRule)obj;
        return otherCR.getLHS().equals(this.getLHS()) && otherCR.getRHS().equals(this.getRHS());
        
    }
    
}


