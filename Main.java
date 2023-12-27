//Baran Korkmaz 2021400090
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//MEMBER_IN <SURNAME_NAME> <GMS>
//<SUPERIOR_SURNAME_NAME> welcomed <NEW_MEMBER_SURNAME_NAME>

//MEMBER_OUT <SURNAME_NAME> <GMS>
//<SURNAME_NAME> left the family, replaced by nobody
//<LEAVING_MEMBER_SURNAME_NAME> left the family, replaced by <REPLACING_MEMBER_SURNAME_NAME>

// INTEL_TARGET <SURNAME_NAME_1> <GMS_1> <SURNAME_NAME_2> <GMS_2>
//Target Analysis Result: <SURNAME_NAME> <GMS>

// INTEL_DIVIDE
//Division Analysis Result: <TARGET_LIST_SIZE>

//INTEL_RANK <SURNAME_NAME> <GMS>
//<SURNAME_NAME_1> <GMS_1> <SURNAME_NAME_2> <GMS_2> ...
public class Main {
    public static void main(String[] args) throws IOException{
        //Choosing the file to read
        File myObj = new File("test5.txt");
        Scanner myReader = new Scanner(myObj);

        //Preparing to write on to the file
        FileWriter myWriter = new FileWriter("output.txt");
        //Creating the tree and starting to read the file
        AvlTree tree = new AvlTree<>();
        String bossData = myReader.nextLine();
        String[] bossDivided = bossData.split(" ");

        //First inserting the root to the tree
        tree.root = tree.insert(Float.parseFloat(bossDivided[1]),null,bossDivided[0],myWriter);
        String bossName = bossDivided[0];
        String bossElement = bossDivided[1];
        while(myReader.hasNextLine()){
            String data = myReader.nextLine();
            String[] dataDivided = data.split(" ");
            if(dataDivided[0].equals("MEMBER_IN")){
                //if a member comes in, we use the insert function in the AvlTree
                float GMS = Float.parseFloat(dataDivided[2]);
                String name = dataDivided[1];
                tree.root = tree.insert(GMS,tree.root,name,myWriter);
            }else if(dataDivided[0].equals("INTEL_TARGET")){
                //if a target is wanted, we use the intelTarget function in the AvlTree class(i explained what the function does in the class)
                String name1 = dataDivided[1];
                float GMS1 =Float.parseFloat(dataDivided[2]);
                String name2 = dataDivided[3];
                float GMS2 =Float.parseFloat(dataDivided[4]);
                ArrayList<String> player1Sup = tree.intelTarget(GMS1,tree.root);
                player1Sup.add(0, bossName + " " + bossElement);
                ArrayList<String> player2Sup = tree.intelTarget(GMS2,tree.root);
                player2Sup.add(0, bossName + " " + bossElement);

                //leaving only the common elements in the player1sup arraylist
                player1Sup.retainAll(player2Sup);
                String answer = player1Sup.get(player1Sup.size()-1);
                String[] answerArray = answer.split(" ");
                myWriter.write("Target Analysis Result: " + answerArray[0] + " " + answerArray[1] + "\n");
            }else if (dataDivided[0].equals("INTEL_RANK")){
                //using recursive rank function for the rank analysis
                String name = dataDivided[1];
                float GMS =Float.parseFloat(dataDivided[2]);
                int rank = tree.intelTarget(GMS,tree.root).size();
                tree.recursiveRank(rank,tree.root);
                ArrayList<String> list = tree.sameRanks;
                myWriter.write("Rank Analysis Result:");
                for(int i = 0; i< list.size(); i++){
                    myWriter.write(" " + list.get(i) );
                }
                myWriter.write("\n");
                tree.sameRanks.clear();
            }else if (dataDivided[0].equals("MEMBER_OUT")){
                //using the remove function when a member leaves
                float GMS = Float.parseFloat(dataDivided[2]);
                String name = dataDivided[1];
                tree.root = tree.remove(GMS,tree.root);
                if(tree.replace.size()==0){
                    myWriter.write(name + " left the family, replaced by nobody\n");
                }else {
                    myWriter.write(name + " left the family, replaced by " + tree.replace.get(0) + "\n");
                }
                tree.replace.clear();
            }else if ( dataDivided[0].equals("INTEL_DIVIDE")){
                //using the recursive divide function for the data analysis
                int ans = Math.abs(tree.intelDivide(tree.root));
                myWriter.write("Division Analysis Result: " + ans + "\n" );
            }
        }
        myWriter.close();
    }
}
