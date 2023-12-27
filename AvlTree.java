//baran Korkmaz 2021400090
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class AvlTree<Float>{
    //AvlNode Class for the nodes
    private static class AvlNode<Float>{
        AvlNode(float theElement, String theName){
            this(theElement,null,null, theName);
        }
        AvlNode(float theElement, AvlNode<Float> lt, AvlNode<Float> rt, String theName){
            element = theElement;
            left = lt;
            right = rt;
            height = 0;
            name = theName;
        }
        float element;
        AvlNode<Float> left;
        AvlNode<Float> right;
        int height;
        String name;
    }

    //height function that returns a node's height
    private int height(AvlNode<Float> element){
        return (element == null) ? -1 : element.height;
    }

    public AvlNode<Float> root;
    public AvlTree(){
        root = null;
    }

    public void remove(float x, FileWriter myWriter){
        root = remove(x,root);
    }


    //remove function that removes an element, how it works is that first it finds the element with going left and right beacause of its
    //gms value. Then when founded, if the node has both of its children, it removes the node and it's replaced by the lowest node on the right side of the node.
    //if the node has no child, then it does not get replaced by any other node. If it has a right child but doesn't have a left child, then
    //it is replaced by its right child. It is the other way around when it has a left child but doesn't have a right child.
    ArrayList<String> replace = new ArrayList<>();
    public AvlNode<Float> remove(float x , AvlNode<Float> t){
        if(t ==null){
            return t;
        }
        if(x<t.element){
            t.left = remove(x,t.left);
        }else if ( x>t.element){
            t.right = remove(x,t.right);
        }else if(t.left != null && t.right != null){
            replace.add(findMin(t.right).name);
            t.element = findMin(t.right).element;
            t.name = findMin(t.right).name;
            t.right = remove(t.element, t.right);

        }else if (t.left!= null || t.right != null){
            if(t.left!= null){
                t = t.left;
                replace.add(t.name);
            }else {
                t = t.right;
                replace.add(t.name);
            }
        }else{
            t = null;
        }
        return balance(t);
    }

    //insert function, it founds the right place for the node with goiing left and right with the gms value
    public AvlNode<Float> insert(float x, AvlNode<Float> t, String name, FileWriter myWriter)throws IOException{
        if(t == null){
            return new AvlNode<>(x,null,null, name);
        }
        //System.out.println(t.name + " welcomed " + name);
        myWriter.write(t.name + " welcomed " + name + "\n");
        if(x<t.element){
            t.left = insert(x,t.left,name, myWriter);
        }else if(x>t.element) {
            t.right = insert(x, t.right,name, myWriter);
        }
        return balance(t);
    }

    public void checkBalance(){
        checkBalance(root);
    }

    //the method that check's the balance
    private int checkBalance(AvlNode<Float> t){
        if(t==null){
            return -1;
        }
        if(t!=null){
            int hl = checkBalance(t.left);
            int hr = checkBalance(t.right);
            if(Math.abs(height(t.left) - height(t.right)) >1 ||
                    height(t.left) != hl || height(t.right) != hr){
                System.out.println("not balanced");
            }
        }
        return height(t);
    }

    private static final int ALLOWED_IMBALANCE = 1;

    //the method that balances the tree with rotations
    private AvlNode<Float> balance(AvlNode<Float> t){
        if(t==null){
            return t;
        }
        if(height(t.left) - height(t.right) > ALLOWED_IMBALANCE){
            if(height(t.left.left) >= height(t.left.right)){
                t = rotateWithLeftChild(t);
            }else{
                t = doubleWithLeftChild(t);
            }
        }else{
            if(height(t.right)-height(t.left) > ALLOWED_IMBALANCE){
                if(height(t.right.right) >= height(t.right.left)){
                    t = rotateWithRightChild(t);
                }else{
                    t = doubleWithRightChild(t);
                }
            }
        }
        t.height = Math.max(height(t.left),height(t.right)) + 1;
        return t;
    }

    //rotate with the left child method
    private AvlNode<Float> rotateWithLeftChild(AvlNode<Float> k2){
        AvlNode<Float> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left),height(k2.right)) +1;
        k1.height = Math.max(height(k1.left), k2.height) +1;
        return k1;
    }

    //rotate with right child method
    private AvlNode<Float> rotateWithRightChild(AvlNode<Float> k1){
        AvlNode<Float> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left),height(k1.right)) +1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    //double rotation with the left child
    private AvlNode<Float> doubleWithLeftChild(AvlNode<Float> k3){
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    //double rotation with the right child
    private AvlNode<Float> doubleWithRightChild(AvlNode<Float> k1){
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    //prints tree in increasing order
    private void printTree( AvlNode<Float> t ) {
        if( t != null ) {
            printTree( t.left );
            System.out.println( t.element );
            printTree( t.right );
        }
    }

    //the method that checks if a value is in the tree
    private boolean contains( float x, AvlNode<Float> t ) {
        while( t != null ) {
            if(x<t.element){
                t = t.left;
            }else if ( x> t.element){
                t = t.right;
            }else {
                return true;
            }
        }
        return false;
    }


    //the target funciton, how it wokrs is that while a value is trying to be found every superior of the value on the way is added to a list
    //then doing this for the two given element, we have two different arraylist which have a least a common element.
    public ArrayList<String> intelTarget( float x, AvlNode<Float> t ) {
        ArrayList<String> superiorList = new ArrayList<>();
        while( t != null ) {
            if(x<t.element){
                String s = String.format(Locale.US,"%.3f",t.element);
                superiorList.add(t.name + " " + s);
                t = t.left;
            }else if ( x> t.element){
                String s = String.format(Locale.US,"%.3f",t.element);
                superiorList.add(t.name + " " + s);
                t = t.right;
            }else {
                return superiorList;
            }
        }
        return superiorList;
    }

    ArrayList<String> sameRanks = new ArrayList<>();

    //rank function. a recursice function that finds the same ranked the elements with the given rank.
    public void recursiveRank(int originalRank, AvlNode<Float> t){
        if(t == null){
            return;
        }
        if(originalRank==0){
            String s = String.format(Locale.US,"%.3f",t.element);
            sameRanks.add(t.name + " " + s);
        }
        recursiveRank(originalRank-1, t.left);
        recursiveRank(originalRank-1, t.right);
    }



    public boolean contains( float x )
    {
        return contains( x, root );
    }

    public void makeEmpty( )
    {
        root = null;
    }

    public boolean isEmpty( )
    {
        return root == null;
    }

    private AvlNode<Float> findMax(AvlNode<Float> t){
        if(t==null){
            return t;
        }
        while(t.right != null){
            t = t.right;
        }
        return t;
    }
 // the function that finds the min value of the tree with the given root
    private AvlNode<Float> findMin(AvlNode<Float> t){
        if(t ==null){
            return t;
        }
        while(t.left!=null){
            t = t.left;
        }
        return t;
    }
    public void printTree( )
    {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root );
    }

    //the divide recursive function. it calcultes the most number of unrelated nodes. a leaf(a node that doesn't have any children) has a value of one.
    //let's say there is a node with two leaves, then its value is -2. its minus because it's related to its children.
    //it starts from the root and goes all the way to the leaves, then returns the correct sum of maximum number of unrelated nodes.
    public int intelDivide(AvlNode<Float> t){
        if(t == null){
            return 0;
        }else if(t.left==null && t.right==null ){
            return 1;
        }
        int rt = intelDivide(t.right);
        int lt = intelDivide(t.left);
        if(rt < 0 && lt< 0){
            return (-1*(rt + lt) + 1);
        }else if (rt >= 0 && lt >= 0 ){
            return (-1*(rt+lt));
        }else{
            int absRt = Math.abs(rt);
            int absLt = Math.abs(lt);
            return (-1*(absRt+absLt));
        }
    }

}
