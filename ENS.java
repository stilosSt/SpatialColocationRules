package colocmining_ens;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;


public class ENS {

    private HashSet<Node> nodes;    //set of nodes in network G
    private HashSet<Edge> edges;    //set of edges in network G    
    
    private double ds;  //threshold distance of neighbor relationship
    
    private HashSet<Tuple> EDC; //a set of tuples,in wich the tuple<e,e',ET,ED(e,e')> contains two edges(e and e') and their ED<ds
    
    private HashSet<NeighborPair> STC;  //set of neighborhood transactions
    private HashSet<NeighborPair> ST;   //set containing all neighborhood transactions
    
    
    
    public ENS(HashSet<Node> nodes, HashSet<Edge> edges,double threshold) {
        
        this.nodes=nodes;
        this.edges=edges;
        this.ds=threshold;
        
        EDC=new HashSet<>();
        STC=new HashSet<>();
        ST=new HashSet<>();
        
    }
    
    public void neigborsSearching(){
        
        for(Edge e:edges){
            
            EDC.clear();
            edgeExpansion(e);            
            for(Tuple t:EDC){
                STC.clear();
                neighborObjectsRefining(t);
                
                for(NeighborPair trnsc:STC){
                    
                    ST.add(trnsc);
                    
                }
            }
            
        }
        
        System.out.println(ST.size()+" neigbor pairs.");        
        //System.out.println(ST);
                    
        //return ST;        
    }

    public void edgeExpansion(Edge e){
        
        
        Node startNode=e.getStartNode();
        Node endNode=e.getEndNode();
        ArrayList<Node> thisEdgeNodes=new ArrayList<>();
        thisEdgeNodes.add(startNode);
        thisEdgeNodes.add(endNode);
        double ND;
        double ED;
        
        Node theOtherNode;
        
        //preprocessing for minHeap
        
        for(Node node:thisEdgeNodes){ 
          
            
            node.setDistance(0);
            
            for(Node initNode:nodes){
                
                if(!initNode.equals(node)){                
                
                    initNode.init();
                }
                
            }      
            
            
            //minHeap Initialization
            Queue<Node> minHeap = new PriorityQueue<>(node.getEdges().size(),new nodesDistComparator());            
            minHeap.add(node);
      
            while(!minHeap.isEmpty()){
                  
                Node nMin = minHeap.poll();
                nMin.setVisited();
                               
                for(Edge edge:nMin.getEdges()){
                                         
                    ED=nMin.getDistance();
                    
                    if(ED<=ds){

                            
                        if(node.equals(startNode) && nMin.equals(edge.getStartNode())){
                                
                            EDC.add(new Tuple(e,edge,"SS",ED));
                                
                        }
                        if(node.equals(startNode) && nMin.equals(edge.getEndNode())){
                            
                            EDC.add(new Tuple(e,edge,"SE",ED));
                                
                        }
                        if(node.equals(endNode) && nMin.equals(edge.getStartNode())){
                            
                            EDC.add(new Tuple(e,edge,"ES",ED));
                                
                        }
                        if(node.equals(endNode) && nMin.equals(edge.getEndNode())){
                            
                            EDC.add(new Tuple(e,edge,"EE",ED));
                                
                        }                                                
                                    
                        if(nMin.equals(edge.getStartNode())){
                                
                            theOtherNode=edge.getEndNode();

                        }
                        else{
                                
                            theOtherNode=edge.getStartNode();
                                
                        }

                        if(!theOtherNode.getVisited()){    
                                
                            ND=nMin.getDistance()+edge.getWeight();
                                    
                            if(theOtherNode.getDistance()>ND && ds>=ND){
                                                                           
                                theOtherNode.setDistance(ND);
                                if(!minHeap.contains(theOtherNode)){
                                        
                                    minHeap.add(theOtherNode);
                                
                                }
                                else{
                                    //remove and add for rearrangement of the heap
                                    minHeap.remove(theOtherNode);
                                    minHeap.add(theOtherNode);
                                }
                                    
                            } 
                                
                        }
  
                    }     
                    
                }  
                
            }    
            
        }
                
    }
        
                   
    public void neighborObjectsRefining(Tuple t){
                
        
        int TransID = ST.size();
        NeighborPair neighbTrans;
        
        for(NetworkObject nObj1:t.getE1().getObjects()){
            
            for(NetworkObject nObj2:t.getE2().getObjects()){
                double netDist = 0;
                /*based on StarNeighborhood partition model we don't care about same type pairs. 
                  this,reduces the computational cost of ENS*/
                if(nObj1.getType().compareTo(nObj2.getType())!=0){                    
                
                    if(t.getET().equals("SS")){
                    
                        netDist=nObj1.getPos()+t.getED()+nObj2.getPos();
                    
                    }
                    if(t.getET().equals("SE")){
                    
                        netDist=nObj1.getPos()+t.getED()+t.getE2().getWeight()-nObj2.getPos();
                    
                    }
                    if(t.getET().equals("ES")){
                    
                        netDist=t.getED()+t.getE1().getWeight()-nObj1.getPos()+nObj2.getPos();
                    
                    }
                    if(t.getET().equals("EE")){
                    
                        netDist=t.getED()+t.getE1().getWeight()-nObj1.getPos()+t.getE2().getWeight()-nObj2.getPos();
                    
                    }
                   
                    if(Math.abs(netDist) <= ds){

                        //insert with lexicographical order
                        if(nObj1.getType().compareTo(nObj2.getType()) < 0){
                    
                            neighbTrans=new NeighborPair(TransID,nObj1,nObj2,netDist);
                            STC.add(neighbTrans);
                        
                        }
                        else{
                        
                            neighbTrans=new NeighborPair(TransID,nObj2,nObj1,netDist);
                            STC.add(neighbTrans);
                        
                        }
  
                    }
                
                }
          
            }         
     
        }
        
    }
    
    public HashSet<NeighborPair> getNeighborPairs(){
        
        return ST;
        
    }

}


class nodesDistComparator implements Comparator<Node>{

    @Override
    public int compare(Node n1, Node n2) {
        
        return (int) (n1.getDistance()-n2.getDistance());
        
    }
        
}
    
    
    
    

