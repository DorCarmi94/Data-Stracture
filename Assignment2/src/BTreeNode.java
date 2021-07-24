public class BTreeNode {

    private final int t;
    private final String[] values;
    private final BTreeNode[] pointers;
    private int numOfvalues;
    private boolean isLeaf;
    private final int hight;

    public BTreeNode(int t,int hight){
        this.t=t;
        this.hight=hight;
        values=new String[2*t-1];
        pointers=new BTreeNode[2*t];
        numOfvalues=0;
        this.isLeaf=true;
    }

    public int getHight() {
        return hight;
    }

    public int getT() {
        return t;
    }

    public void setNumOfvalues(int n){
        this.numOfvalues=n;
    }

    public void setChildAt(int index, BTreeNode child){
        this.pointers[index]=child;
    }

    public int getNumOfValues(){
        return this.numOfvalues;
    }

    public BTreeNode getChildAt(int index){
        return pointers[index];
    }

    public boolean IsLeaf(){
        return isLeaf;
    }

    public void setLeaf(boolean b){
        isLeaf=b;
    }

    public String getValuesInIndex(int index) {
        if(numOfvalues<=index)
            return null;
        return values[index];
    }


    public void addValue(String value){
        AddByOrder(value);
    }

    private void AddByOrder(String value){
        for(int i=numOfvalues-1; i>=0;i--){
            if(values[i]!=null){
                if(values[i].compareTo(value)<0){
                    values[i+1]=value;
                    return;
                }
                values[i+1]=values[i];
                values[i]=null;
            }
        }
        values[0]=value;


    }

    public int indexOfValue(String value){
        if(values[0].compareTo(value)>=0){
            return 0;
        }
        int i;
        for(i=1; i<numOfvalues;i++){
            if(values[i].compareTo(value)>=0){
                return i;
            }
        }
        return i;
    }

    public String toString(int index,int roothight){
        return values[index]+"_"+String.valueOf(roothight-hight)+",";

    }

    public void deleteKeyFromLeaf(int index) {
        while(index<numOfvalues-1){
            values[index]=values[index+1];
            index++;
        }
        numOfvalues--;
    }

    public int indexOfNode(BTreeNode current) {//TODO
        for(int i=0; i<this.pointers.length; i++){
            if(pointers[i]==current){
                return i;
            }
        }
        return -1;
    }

    public void removeChildAt(int i) {
        for (int j=i ; j<this.numOfvalues; j++){
            this.pointers[j]=pointers[j+1];

        }
    }

    public void removeKeyAt(int i) {
        for (int j=i ; j<this.numOfvalues-1; j++){
            this.values[j]=values[j+1];
        }
        numOfvalues--;
    }

    public void setValueAt(int index, String valuesInIndex) {
        values[index]=valuesInIndex;
    }
}
