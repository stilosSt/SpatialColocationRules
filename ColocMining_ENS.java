package colocmining_ens;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * @data mining for location based services
 * @author Stylos Stavros
 */
public class ColocMining_ENS {

    private static final double timeDiv=1000000000.0;
  
    public static void main(String[] args) throws SQLException{
           
        System.out.println("Data Base Connection....");
         
        long startTime = System.nanoTime();
         
        DBManager connect=new DBManager();      
         
        connect.conToNodes();
        connect.conToEdges();
        connect.conToPOIs();
         
        long endTime = System.nanoTime();
         
        System.out.println("Making the Network Graph procedure:"+ (endTime - startTime)/timeDiv +" sec");
         
        System.out.println("Give a distance threshold of neigbor relationship:");
        Scanner sc=new Scanner(System.in);
        double threshold=sc.nextDouble();
         
        startTime = System.nanoTime();
         
        ENS edgeBasedNS=new ENS(connect.getNodes(),connect.getEdges(),threshold); 
        edgeBasedNS.neigborsSearching();
         
        endTime = System.nanoTime();
        
        System.out.println("_____ENS procedure:"+ (endTime - startTime)/timeDiv +" sec_____");
         
        startTime = System.nanoTime();
        StarNeighborhoods SN=new StarNeighborhoods(edgeBasedNS.getNeighborPairs(),connect.getPOIsMap());
        SN.getTransactions();
        endTime = System.nanoTime();
         
        System.out.println("_____Getting Transactions procedure:"+ (endTime - startTime)/timeDiv +" sec_____"); 
         
         
        System.out.println("Give prevalence threshold:");
        double min_prev=sc.nextDouble();
         
        System.out.println("Give condition probability threshold:");
        double min_cond_prob=sc.nextDouble();
         
        startTime = System.nanoTime();
        ColocationMining NMColoc=new ColocationMining(SN.getStarNeigborhoods(),connect.getFeaturesSum(),edgeBasedNS.getNeighborPairs(),min_prev,min_cond_prob);
        NMColoc.mainProcedure();
        endTime = System.nanoTime();
         
        System.out.println("_____Getting Co-location patterns procedure:"+ (endTime - startTime)/timeDiv +" sec_____"); 
    }
    
}
