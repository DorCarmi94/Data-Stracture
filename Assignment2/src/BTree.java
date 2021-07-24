import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

class BTree {

    private BTreeNode root;
    private final int t;
    private final int maxValues;
    private int hight;

    public BTree(String tS) {
        try {
            int t = Integer.valueOf(tS);
            this.t = t;
            this.maxValues = 2 * t - 1;
            this.root = new BTreeNode(t,0);
            this.hight = 0;
        }
        catch (Exception e){
            throw new IllegalArgumentException("the input is not a number: +" +tS);
        }
    }

    private SearchOutput search(String key) {
        BTreeNode current = root;
        int index;
        while (!current.IsLeaf()) {
            index = current.indexOfValue(key);
            if (current.getValuesInIndex(index)==(key)) {
                return new SearchOutput(index, current);
            } else {
                current = current.getChildAt(index);
            }
        }
        index = current.indexOfValue(key);
        if (current.getValuesInIndex(index)!=null &&current.getValuesInIndex(index).equals(key)) {
            return new SearchOutput(index, current);
        } else {
            return null;
        }
    }

    public void insert(String value) {
        BTreeNode r = this.root;
        if (r.getNumOfValues() == (2 * t - 1)) {
            BTreeNode s = new BTreeNode(t,root.getHight()+1);
            root = s;
            s.setLeaf(false);
            s.setChildAt(0, r);
            splitChild(s, 0);
            InsertNonFull(s, value);
        } else {
            InsertNonFull(r, value);
        }
    }

    private void InsertNonFull(BTreeNode x, String value) {
        int i = x.getNumOfValues();
        if (x.IsLeaf()) {
            x.addValue(value);
            x.setNumOfvalues(x.getNumOfValues()+1);
        } else {
            while (i > 0 && value.compareTo(x.getValuesInIndex(i-1)) < 0) {
                i--;
            }
            if (x.getChildAt(i).getNumOfValues() == maxValues) {
                splitChild(x, i);
                if (value.compareTo(x.getValuesInIndex(i)) > 0) {
                    i++;
                }
            }
            InsertNonFull(x.getChildAt(i), value);
        }
    }

    private static void splitChild(BTreeNode parrent, int i) {
        int t = parrent.getT();
        BTreeNode y = parrent.getChildAt(i);
        BTreeNode z = new BTreeNode(t,y.getHight());
        z.setLeaf(y.IsLeaf());
        z.setNumOfvalues(t - 1);
        for (int j = 0; j < t - 1; j++) {
            z.addValue(y.getValuesInIndex(j + t));
        }
        if (!y.IsLeaf()) {
            for (int j = 0; j < t; j++) {
                z.setChildAt(j,y.getChildAt(j+t));
            }
        }
        for (int j = parrent.getNumOfValues(); j > i + 1; j--) {
            parrent.setChildAt(j + 1, parrent.getChildAt(j));
        }
        parrent.setChildAt(i + 1, z);
        parrent.addValue(y.getValuesInIndex(t-1));
        parrent.setNumOfvalues(parrent.getNumOfValues() + 1);
        y.setNumOfvalues(t - 1);
    }

    public void createFullTree(String s) {
        try {
            File file = new File(s);
            Scanner cs = new Scanner(file);
            while(cs.hasNextLine()){
                String pass= cs.nextLine().toLowerCase();
                this.insert(pass);
            }
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    public String toString(){
        String output=toString(root,"",root.getHight());
        if(!output.isEmpty())
            output= output.substring(0,output.length()-1);
        return output;
    }

    public String toString(BTreeNode x,String str, int roothight){
        if(x.IsLeaf()){
            for(int i=0; i<x.getNumOfValues(); i++) {
                str = str + x.toString(i,roothight);
            }
            return str;
        }
        else{
            int i;
            for(i=0; i<x.getNumOfValues(); i++){
                str=toString(x.getChildAt(i),str,roothight);
                str=str+x.toString(i,roothight);
            }
            str=toString(x.getChildAt(i),str,roothight);
            return str;
        }
    }


    public String getSearchTime(String s) {
        try {
            File file = new File(s);
            Scanner cs = new Scanner(file);
            long start = System.nanoTime();
            while (cs.hasNextLine()){
                String check=cs.nextLine().toLowerCase();
                this.search(check);
            }
            long end=System.nanoTime();
            Long diff= end-start;
            double diffInDouble= diff.doubleValue();
            diffInDouble=diffInDouble/1000000;
            return String.format("%.4f",diffInDouble);

        }
        catch (Exception e){
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement st:stackTrace) {
                System.out.println(st.toString());
            }
            return null;
        }
    }


    public void deleteKeysFromTree(String path){
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String pass = sc.nextLine().toLowerCase();
                this.deleteKey(pass, root, root);
            }
        }
        catch (Exception e){
            StackTraceElement[] stackTrace = e.getStackTrace();
            System.out.println(Arrays.toString(stackTrace));

        }
    }

    public boolean deleteKey(String key,BTreeNode current, BTreeNode father) {
        Boolean side;
        int index = current.indexOfValue(key);
        //if(current.getValuesInIndex(index)!=null && current.getValuesInIndex(index).equals(key)&&current==root&&current.getNumOfValues()==1)
          //  return deleteFromRoot(key,current);

        if (current.getNumOfValues() == t-1 && current!=root) {
            if ((side = haveBrotherWhithTKeys(current, father)) != null) {
                getKeyFromBrother(current, father, side);
            } else {    current= mergeWithBrotherAndFather(current, father);
            }
        }
        index = current.indexOfValue(key);
        if (current.getValuesInIndex(index)!=null && current.getValuesInIndex(index).equals(key)) {
            if (current.IsLeaf()) {
                current.deleteKeyFromLeaf(index);
                return true;
            } else { swapKeyWithLeftChild(key, current, current.getChildAt(index));
                return deleteKey(key, current.getChildAt(index), current);
            }
        } else { return deleteKey(key, current.getChildAt(index), current);
        }
    }

    private boolean deleteFromRoot(String key, BTreeNode current) {
        if(current.getChildAt(0)==null) {
            root = new BTreeNode(t,0);
            return true;
        }
        BTreeNode newRoot=new BTreeNode(t,current.getHight()-1);
        BTreeNode mergedNode1=current.getChildAt(0);
        BTreeNode mergedNode2=current.getChildAt(1);
        int i; for(i=0;i<mergedNode1.getNumOfValues();i++){
            newRoot.addValue(mergedNode1.getValuesInIndex(i));
            newRoot.setNumOfvalues(newRoot.getNumOfValues()+1);
            newRoot.setChildAt(i,mergedNode1.getChildAt(i));
        }
        newRoot.setChildAt(i,mergedNode1.getChildAt(i));
        i++; int j;  for(j=0; j<mergedNode2.getNumOfValues(); j++){
            newRoot.addValue((mergedNode2.getValuesInIndex(j)));
            newRoot.setNumOfvalues(newRoot.getNumOfValues()+1);
            newRoot.setChildAt(i+j,mergedNode2.getChildAt(j));
        }
        newRoot.setChildAt(i+j,mergedNode2.getChildAt(j));
        newRoot.setLeaf(mergedNode1.IsLeaf());
        root=newRoot;
        return true;
    }


    private void swapKeyWithLeftChild(String key, BTreeNode current, BTreeNode leftChild) {
        String childKey=leftChild.getValuesInIndex(leftChild.getNumOfValues()-1);
        leftChild.setValueAt(leftChild.getNumOfValues()-1,key);
        current.setValueAt(current.indexOfValue(key),childKey);
    }

    private BTreeNode mergeWithBrotherAndFather(BTreeNode current, BTreeNode father) {
        if(father.getChildAt(0)==current){
            return mergeWithRightBrother(current,father);
        }
        else{
            return mergeWithLeftBrother(current,father);
        }

    }

    private BTreeNode mergeWithRightBrother(BTreeNode current, BTreeNode father) {
        BTreeNode mergedNode=new BTreeNode(t,current.getHight());
        mergedNode.setLeaf(current.IsLeaf());
        BTreeNode mergeWith=father.getChildAt(father.indexOfNode(current)+1);
        int i; for(i=0;i<current.getNumOfValues();i++){
            mergedNode.addValue(current.getValuesInIndex(i));
            mergedNode.setNumOfvalues(mergedNode.getNumOfValues()+1);
            mergedNode.setChildAt(i,current.getChildAt(i));
        }
        mergedNode.setChildAt(i,current.getChildAt(i));
        mergedNode.addValue(father.getValuesInIndex(father.indexOfNode(current)));
        mergedNode.setNumOfvalues(mergedNode.getNumOfValues()+1);
        i++; int j;  for(j=0; j<mergeWith.getNumOfValues(); j++){
            mergedNode.addValue((mergeWith.getValuesInIndex(j)));
            mergedNode.setNumOfvalues(mergedNode.getNumOfValues()+1);
            mergedNode.setChildAt(i+j,mergeWith.getChildAt(j));
        }
        mergedNode.setChildAt(i+j,mergeWith.getChildAt(j));
        return checkFatherRight(mergedNode,father,current);

    }

    private BTreeNode checkFatherRight(BTreeNode mergedNode, BTreeNode father,BTreeNode current) {
        if(father==root && father.getNumOfValues()==1) {
            hight--; return root=mergedNode;
        }
        else { father.removeKeyAt(father.indexOfNode(current));
            father.setChildAt(father.indexOfNode(current), mergedNode);
            int k;
            for (k = father.indexOfNode(mergedNode); k < father.getNumOfValues() - 1; k++) {
                father.setChildAt(k + 1, father.getChildAt(k + 2));
            }
            father.setChildAt(k + 1, father.getChildAt(k + 2));
            return mergedNode;
        }
    }

    private BTreeNode mergeWithLeftBrother(BTreeNode current, BTreeNode father) {
        BTreeNode mergedNode=new BTreeNode(t,current.getHight());
        BTreeNode mergeWith=father.getChildAt(father.indexOfNode(current)-1);
        mergedNode.setLeaf(current.IsLeaf());
        int i;
        for(i=0;i<mergeWith.getNumOfValues();i++){
            mergedNode.addValue(mergeWith.getValuesInIndex(i));
            mergedNode.setNumOfvalues(mergedNode.getNumOfValues()+1);
            mergedNode.setChildAt(i,mergeWith.getChildAt(i));
        }
        mergedNode.setChildAt(i,mergeWith.getChildAt(i));
        mergedNode.addValue(father.getValuesInIndex(father.indexOfNode(current)-1));
        mergedNode.setNumOfvalues(mergedNode.getNumOfValues()+1);
        i++;
        int j;
        for(j=0; j<current.getNumOfValues(); j++){
            mergedNode.addValue((current.getValuesInIndex(j)));
            mergedNode.setNumOfvalues(mergedNode.getNumOfValues()+1);
            mergedNode.setChildAt(i+j,current.getChildAt(j));
        }
        mergedNode.setChildAt(i+j,current.getChildAt(j));
        return checkFatherLeft(father,mergedNode,current);

    }

    private BTreeNode checkFatherLeft(BTreeNode father, BTreeNode mergedNode, BTreeNode current) {
        if(father==root && father.getNumOfValues()==1) {
            hight--;
            return root=mergedNode;
        }
        else{
            father.removeKeyAt(father.indexOfNode(current) - 1);
            father.setChildAt(father.indexOfNode(current) - 1, mergedNode);
            int k;
            for (k = father.indexOfNode(mergedNode) ; k < father.getNumOfValues() - 1; k++) {
                //father.setValueAt(k, father.getValuesInIndex(k + 1));
                father.setChildAt(k + 1, father.getChildAt(k + 2));
            }
            return mergedNode;
        }
    }

    private void getKeyFromBrother(BTreeNode current, BTreeNode father, Boolean side ) {
        int index=father.indexOfNode(current);
        if(side){
            current.addValue(father.getValuesInIndex(index));
            current.setNumOfvalues(current.getNumOfValues()+1);
            current.setChildAt(current.getNumOfValues(),father.getChildAt(index+1).getChildAt(0));
            father.getChildAt(index+1).removeChildAt(0);
            father.setValueAt(index,father.getChildAt(index+1).getValuesInIndex(0));
            father.getChildAt(index+1).removeKeyAt(0);
        }if(!side){
            current.addValue(father.getValuesInIndex(index-1));
            current.setNumOfvalues(current.getNumOfValues()+1);
            current.setChildAt(current.getNumOfValues(),father.getChildAt(index-1).getChildAt(father.getChildAt(index-1).getNumOfValues()+1));
            father.getChildAt(index-1).removeChildAt(father.getChildAt(index-1).getNumOfValues()+1);
            father.setValueAt(index-1,father.getChildAt(index-1).getValuesInIndex(father.getChildAt(index-1).getNumOfValues()-1));
            father.getChildAt(index-1).removeKeyAt(father.getChildAt(index-1).getNumOfValues()-1);
        }
    }

    private Boolean haveBrotherWhithTKeys(BTreeNode current, BTreeNode father) {
        int index=father.indexOfNode(current);
        if(index==father.getNumOfValues()) {
            if (father.getChildAt(index - 1).getNumOfValues() >= t) {
                return false; }
            return null; }
        if(index==0) {
            if (father.getChildAt(index + 1).getNumOfValues() >= t) {
                return true; }
            return null;}
        if (father.getChildAt(index - 1).getNumOfValues() >= t) {
            return false;}
        if (father.getChildAt(index + 1).getNumOfValues() >= t) {
            return true;}
        return null;
    }

    public void delete(String hellow) {
        deleteKey(hellow,root,root);
    }
}
