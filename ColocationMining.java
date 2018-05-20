package colocmining_ens;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class ColocationMining {
    
    //Inputs:
    private TreeMap<String,ArrayList<Transaction>> transactionsMap;
    private TreeMap<String,Integer> featuresSum;
    private double minPrev;
    private double minCondProb;
    
    //Variables:  

    private HashMap<Integer,HashSet<CandidateColocation>> ccMap; //a map of size k candidate colocations
    private HashMap<Integer,HashSet<PrevalentColocation>> pcMap; //a map of size k(key) prevalent colocations
    private HashSet<StarInstance> siSet;
    private HashMap<Integer,HashSet<StarInstance>> siMap;
    private HashSet<CliqueInstance> ciSet;
    private HashMap<Integer,HashSet<CliqueInstance>> ciMap;
    private HashMap<Integer,HashSet<ColocationRule>> crMap;
    
    public ColocationMining(TreeMap<String,ArrayList<Transaction>> transMap,TreeMap<String,Integer> featuresSum,
            HashSet<NeighborPair> ST,double min_prev,double min_cond_prob){
        
        this.transactionsMap=transMap;
        this.featuresSum=featuresSum;
        this.minPrev=min_prev;
        this.minCondProb=min_cond_prob;  
        
        ccMap=new HashMap<>();
        pcMap=new HashMap<>();
        siMap=new HashMap<>();
        ciMap=new HashMap<>();
        crMap=new HashMap<>();
        
    }
    
    public void mainProcedure(){

        HashSet<PrevalentColocation> itemset=new HashSet<>();
        
        for(String elem:featuresSum.keySet()){
            TreeSet<String> tempSet=new TreeSet<>();
            tempSet.add(elem);
            itemset.add(new PrevalentColocation(tempSet,1,featuresSum.get(elem)));
        }
        
        pcMap.put(1,itemset);
        System.out.println("==============================================================");
        System.out.println(" Prevalent colocations"+"-"+1+":");
        System.out.println("==============================================================");
        for(PrevalentColocation pC:pcMap.get(1)){
               
               System.out.println(pC);
               
        }
        System.out.println("==============================================================");
        
        int k=2;
        
        while(!pcMap.get(k-1).isEmpty()){
        
            
           gen_candidate_colocations(pcMap.get(k-1),k-1);

            //System.out.println(ccMap.get(k));
           
            siSet=new HashSet<>();
            
            for(String feature:transactionsMap.keySet()){
                   
                for(CandidateColocation cC:ccMap.get(k)){
                       
                    if(cC.getCandidate().first().equals(feature)){
                           
                        filter_star_instances(cC,transactionsMap.get(feature),k);    
                           
                    }                       
                }    
            }                    
            
            siMap.put(k, siSet);
            //siMap.get(k).print();
            
            ciSet=new HashSet<>();
            if(k==2){           
                
                for(StarInstance stInst:siMap.get(k)){
                    
                    CliqueInstance ci=new CliqueInstance(stInst.getInstCC(),stInst.getNobjL());
                    ciSet.add(ci);
                }    
                
                //diagrafh twn candidates pou den emperiexontai se kapoia colocation instance 
                //gia na apofugoume nulls se epanalhpseis
                Iterator<CandidateColocation> iter=ccMap.get(k).iterator();
                while(iter.hasNext()){
                    
                    CandidateColocation cc=iter.next();
                    boolean hasClInst=false;
                    
                    for(CliqueInstance clIn: ciSet){
                        
                        if(cc.equals(clIn.getInstCC())){
                        
                            cc.addCI(clIn);
                            hasClInst=true;
                        }
                    }
                    if(!hasClInst){
                        
                        iter.remove();
                    }
                }

            }
            else{
                
                select_coarse_prevalent_colocations(k);
                filter_clique_instances(k);
                
            }
            
            ciMap.put(k, ciSet);
            //System.out.println(ciMap.get(k));
            
            select_prevalent_colocations(k);
            if(!pcMap.get(k).isEmpty()){
                
                System.out.println("==============================================================");
                System.out.println(" Prevalent colocations"+"-"+k+":");
                System.out.println("==============================================================");
                for(PrevalentColocation pC:pcMap.get(k)){
               
                    System.out.println(pC);
               
                }
            
            System.out.println("==============================================================");
            
            }
            
            /*
            if(siMap.get(k).equals(ciMap.get(k))){
                
                System.out.println("SAME");
            }
            else{
                
                System.out.println("NOT SAME");
                
            }*/
            gen_colocation_rules(k);
            if(!crMap.get(k).isEmpty()){
                
                System.out.println("==============================================================");
                System.out.println(" Colocation rules"+"-"+k+"");
                System.out.println("==============================================================");
                for(ColocationRule colR:crMap.get(k)){
                
                    System.out.println(colR);
                
                }
            }
            
            k++;
        }
        
        int cINum=0;
        for(int i:ciMap.keySet()){

            cINum=cINum+ciMap.get(i).size();

        }
        
        System.out.println("Number of Colocation Instances "+cINum);

        int pNum=0;
        for(int i:pcMap.keySet()){
                
            pNum=pNum+pcMap.get(i).size();

        }
        
        System.out.println("Number of Colocation Patterns "+pNum);
        
        
    }
 
    /*
     * Generate Candidate Colocation with Fk-1XFk-1 method
     * and feature lelel filtering-if any subset of the generated colocation
     * is not in Pk-1 set,is pruned.   
     */
    
    public void gen_candidate_colocations(HashSet<PrevalentColocation> prevPc,int prevK){
        
        int count1,count2;      
        HashSet<CandidateColocation> cCset=new HashSet<>();
        
        for(PrevalentColocation set1:prevPc){
                        
            ArrayList<String> tempList1=new ArrayList<>();
            count1=0;
            
            for(String str:set1.getPrevCol()){
               
                if(prevK==1){
                    
                    tempList1.add(str);
                    break;
                }
                if(count1<prevK-1){ 
                   
                    tempList1.add(str);
                    
                }  
                count1++;
            }
            for(PrevalentColocation set2:prevPc){

                if(set2.equals(set1)){
                    
                    continue;
                }
                
                ArrayList<String> tempList2=new ArrayList<>();
                count2=0;
            
                for(String str:set2.getPrevCol()){
                    
                    if(prevK==1){
                    
                        tempList2.add(str);
                        break;
                    }
                    if(count2<prevK-1){ 
                   
                        tempList2.add(str);
                    
                    }  
                    count2++;
                }
                
                if(prevK==1 || tempList2.equals(tempList1)){
                
                    TreeSet<String> tempSet=new TreeSet<>();
                    tempSet.addAll(set1.getPrevCol());
                    tempSet.addAll(set2.getPrevCol());                    
                    cCset.add(new CandidateColocation(tempSet));       
                }            
            }            
        }
               
        //prune step
        //we delete alla colocations c ε Ck such that some size k-1 subset of c is not in Pk
        if(prevK>1){
            
            Iterator<CandidateColocation> iter=cCset.iterator();
            while(iter.hasNext()){
                
                boolean prevalent=false;
                int count=0;
                CandidateColocation cCol=iter.next();
                
                for(PrevalentColocation set:prevPc){
                    
                    if(cCol.getCandidate().containsAll(set.getPrevCol())){           
                        
                        count++;
                        
                        if(count==cCol.getCandidate().size()){
                            
                            prevalent=true;
                            break; 
                        }
                                 
                        
                    }
   
                }
                if(!prevalent){
                    
                    iter.remove();   
                    
                }
            }                  
        } 
        
       ccMap.put(prevK+1, cCset);       
       
    }
    
    
    /*  
     *   Filter star instances procedure.    
     *  The star instances of a candidate colocation are gatherd from the sta neighborhoods whose 
     *  center object's feature type is the same as the first feature of the colocation.
     */
    
    public void filter_star_instances(CandidateColocation cC,ArrayList<Transaction> transList,int k){    
             
        TreeSet<NetworkObject> tempSet;
        
        ArrayList<TreeSet<NetworkObject>> tempArray = new ArrayList<>();
        int count;
        boolean gathered;
        
        for(Transaction t:transList){
            
            tempSet=new TreeSet<>(new ObjComparator());
            gathered=false;
            count=0;
            
            for(NetworkObject nObj:t.getTrans()){
                
                for(String elem:cC.getCandidate()){

                    if(nObj.getType().equals(elem)){
                         
                        if(count<k){
                            
                            tempSet.add(nObj);
                            count++;       
                        }
                        if(count==k){

                            gathered=true;
                            
                        }
                        break;
                    }
                }
                if(gathered){
                        
                    break;
                }
            }//oloklhrwsh mias sunallaghs star instance
            if(gathered){

                if(tempArray.contains(tempSet)){
                    
                   continue;
                }
                tempArray.add(tempSet);
                       
            }
            //else{
                
                //tempSet=null;
                
            //}
        }
        
        if(tempArray.size()>0){
            
            StarInstance stIns=new StarInstance(cC,tempArray);
            siSet.add(stIns);
            cC.addSI(stIns);
        }
    }
    
    /*  
     *  Select coarse prevalent colocations procedure.
     *  We filter a candidate colocation using the partition index of its star instance.
     */
    
    
    public void select_coarse_prevalent_colocations(int k){
                
        Iterator<CandidateColocation> iter=ccMap.get(k).iterator();
        
        while(iter.hasNext()){
            
            CandidateColocation tempCand=iter.next();
                    
            if(tempCand.getSI().getPI(featuresSum) < minPrev){
   
                iter.remove();

            }
        }        
    }
    
    /*
     *  Filter clique instances procedure.
     *  From the star instances of a candidate colocation we filter
     *  its colocation(clique) instances using the instance look-up schema.
     */
        
    public void filter_clique_instances(int k){

        boolean formsClique;
        Iterator<CandidateColocation> iter=ccMap.get(k).iterator();
        
        while(iter.hasNext()){
            
           
            CandidateColocation candSet=iter.next();
            TreeSet<String> tailSet1 = (TreeSet<String>) candSet.getCandidate().tailSet(candSet.getCandidate().first(),false);
            
            
            ArrayList<TreeSet<NetworkObject>> tempList=new ArrayList<>();
            for(TreeSet<NetworkObject> nObjS1:candSet.getSI().getNobjL()){
                
                TreeSet<NetworkObject> fObj=new TreeSet<>(new ObjComparator());
                fObj.addAll(nObjS1);
                TreeSet<NetworkObject>tailSet2 = (TreeSet<NetworkObject>) fObj.tailSet(fObj.first(),false);  
                formsClique=false;
                
                for(CliqueInstance ci:ciMap.get(k-1)){
                        
                    if(ci.getInstCC().getCandidate().equals(tailSet1)){
                             
                        for(TreeSet<NetworkObject> nObjS2:ci.getNobjL()){
                            
                            if(tailSet2.equals(nObjS2)){
                                
                                formsClique=true; 
                                if(tempList.contains(fObj)){
                                    
                                    break;
                                }
                                tempList.add(fObj);

                                break;
                                        
                            }
                        }
                    }
                    if(formsClique){
                        
                        break;
                    }
                }
            }                        
            if(tempList.size()>0){
                
                CliqueInstance ci=new CliqueInstance(candSet,tempList);
                //System.out.println(candSet + " " +tempList);
                ciSet.add(ci);
                candSet.addCI(ci);
                
            }
            //else{
                
                //iter.remove();
            //}    
        }
        
    }
    
    /*
     *  Select prevalent colocations procedure.
     *  The refinement filtering of colocations is done by the true partition
     *  index values,calculated from their colocation(clique) instances.
     */
    public void select_prevalent_colocations(int k){
        
        HashSet<PrevalentColocation> itemset=new HashSet<>();
        HashMap<Double,Integer> tempMap;
        double Pi=0;
        int inst=0;
        Iterator<CandidateColocation> iter=ccMap.get(k).iterator();
        
        while(iter.hasNext()){
            
            TreeSet<String> prC=new TreeSet<>();
            CandidateColocation cC=iter.next();
 
            for(String elem:cC.getCandidate()){
                
                prC.add(elem);
                
            }
            if(cC.getCI()==null){
                //System.out.println("null"+k);
                continue;
            }
            tempMap=cC.getCI().getPI(featuresSum);       
            for(Double d:tempMap.keySet()){
                
                Pi=d;
                inst=tempMap.get(d);
                break;
            }           
            if(Pi >= minPrev){
                
                itemset.add(new PrevalentColocation(prC,Pi,inst));

            }
            else{
                
                Iterator<CliqueInstance> cM=ciMap.get(k).iterator();
                
                while(cM.hasNext()){
                    
                    CliqueInstance ci=cM.next();
                    
                    if(ci.getInstCC().getCandidate().equals(prC)){
                        
                        if(Pi<minPrev){
                            
                            cM.remove();
                        }
                        
                    }

                }
                
                iter.remove();
            }
        }
        
        pcMap.put(k, itemset);

    }


    public void gen_colocation_rules(int k){
        
        HashSet<ColocationRule> rulesSet=new HashSet<>();
        HashSet<ColocationRule> prevSet=new HashSet<>();
        
        double condPr;
                   
        for(PrevalentColocation prC:pcMap.get(k)){
            
            int level=1;
            //ArrayList<String> tempList=new ArrayList<>(prC.getPrevCol());
            ArrayList<String> tempList=new ArrayList<>();
            for(String feature:prC.getPrevCol()){
                
                tempList.add(feature);
                
            }
            for(String elem:prC.getPrevCol()){
                
                TreeSet<String> RHS=new TreeSet<>();
                TreeSet<String> LHS=new TreeSet<>();
                
                RHS.add(elem);
                for(String otherElem:tempList){
                    
                    if(!otherElem.equals(elem)){
                        
                        LHS.add(otherElem);
                        
                    }    
                }
                TreeSet<String> tempSet=new TreeSet<>(RHS);
                tempSet.addAll(LHS);
                
                condPr = get_condition_probabilty(tempSet,RHS);
                if(condPr>=minCondProb){
                    
                    ColocationRule colRule=new ColocationRule(LHS,RHS,condPr);
                    rulesSet.add(colRule);
                    prevSet.add(colRule);
                    
                }  
            }
            
            level++;     
            
            while(k>level-1){

                HashSet<ColocationRule> temp=new HashSet<>();
                
                for(ColocationRule colR1:prevSet){
                    
                    TreeSet<String> RHS=new TreeSet<>();
                    TreeSet<String> LHS=new TreeSet<>();
                    
                    for(ColocationRule colR2:prevSet){

                        if(!colR1.equals(colR2)){

                            RHS.addAll(colR1.getRHS());
                            RHS.addAll(colR2.getRHS());
                            
                            LHS.addAll(colR1.getLHS());
                            LHS.addAll(colR2.getLHS());

                            break;
                        }   
                        
                    }

                    Iterator<String> iter=LHS.iterator();
                        
                    while(iter.hasNext()){
                        
                        String str=iter.next(); 
                        if(RHS.contains(str)){
                            
                            iter.remove();
                            
                        }
                        
                    }
                        
                    TreeSet<String> tempSet=new TreeSet<>();
                    tempSet.addAll(RHS);
                    tempSet.addAll(LHS);   
                    
                    if(tempSet.isEmpty() || LHS.isEmpty()){
                        
                        continue;
                        
                    }
                    
                    condPr = get_condition_probabilty(tempSet,LHS);
                        
                    if(condPr >= minCondProb){
                    
                        ColocationRule colRule=new ColocationRule(LHS,RHS,condPr);
                        rulesSet.add(colRule);
                        temp.add(colRule);
                        
                    } 
                }

                prevSet.clear();
                prevSet.addAll(temp);
                level++;
                
                if(prevSet.isEmpty()){
            
                    break;
                }
            }
            
        
        }

        crMap.put(k,rulesSet);
    }
    
    public double get_condition_probabilty(TreeSet<String> tempSet,TreeSet<String> LHS){
        
        int k=tempSet.size();
        
        HashSet<CliqueInstance> corCiSet= ciMap.get(k);
        TreeSet<NetworkObject> nOList=new TreeSet<>(new TreeSet<>(new ObjComparator()));
        HashSet<TreeSet<NetworkObject>> hashTemp=new HashSet<>();
        int numarator=-1;
        int denominator=1;
        boolean gotDen=false;
                
        for(CliqueInstance cI:corCiSet){            
            
            if(tempSet.equals(cI.getInstCC().getCandidate())){
                
                for(TreeSet<NetworkObject> set2:cI.getNobjL()){
                    
                    for(NetworkObject nOb:set2){
                                
                        if(LHS.contains(nOb.getType())){
                            
                            if(nOList.contains(nOb)){
                                
                                continue;
                            }
                            nOList.add(nOb);
                                                 
                        }                  
                    }
                    if(nOList.size()==LHS.size()){
                        
                        hashTemp.add(nOList);
                        
                    }
                    
                    nOList=new TreeSet<>();
                    
                } 

                numarator=hashTemp.size();
                break;
            }
        
        }
        k=LHS.size();

        HashSet<PrevalentColocation> coresPC=pcMap.get(k);
        
        for(PrevalentColocation set:coresPC){
            
            if(LHS.equals(set.getPrevCol())){
                
                denominator=set.getInst();
                gotDen=true;
            }
        }
        
        //se pretiptwsh pou den htan prevalent 
        if(!gotDen){
            
            return 0;
            
        }
        
        double conProb=numarator/(double)denominator;
        if(conProb>1){
            
            System.out.println(numarator+" "+denominator);
        }
        
        if(conProb>=0){
            
            return conProb;
        }
        
        return 0;
        
    }
    
}

class ObjComparator implements Comparator<NetworkObject>{

    @Override
    public int compare(NetworkObject t1, NetworkObject t2) {
        int i;
        i=t1.getType().compareTo(t2.getType());
        if(i!=0){
            
            return i;
            
        }
        return t1.getID()-t2.getID();
      
    }

}

