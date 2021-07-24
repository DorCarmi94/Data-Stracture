package default_package;
public class FloorsArrayLink {

    private double key;
    private FloorsArrayLink[] forwardArr;
    private FloorsArrayLink[] backwardArr;

    public FloorsArrayLink(double key, int arrSize){
        this.key=key;
        this.forwardArr=new FloorsArrayLink[arrSize];
        this.backwardArr=new FloorsArrayLink[arrSize];

        for (FloorsArrayLink forLink:this.forwardArr) {
            forLink=this;

        }
        for (FloorsArrayLink backLink:this.backwardArr) {
            backLink=this;

        }
    }

    public double getKey() {
        return this.key;
    }

    public FloorsArrayLink getNext(int i) {
        return this.forwardArr[i-1];
    }

    public FloorsArrayLink getPrev(int i) {
        return this.backwardArr[i-1];
    }

    public void setNext(int i, FloorsArrayLink next) {
        forwardArr[i-1]=next;
    }

    public void setPrev(int i, FloorsArrayLink prev) {
        backwardArr[i-1]=prev;
    }

    public int getArrSize(){
        return forwardArr.length;
    }


    public String toString() {
        String str="back:";

        for (FloorsArrayLink backLink:this.backwardArr)
        {
            if(backLink==null)
            {
                str += "  " + "this";
            }
            else {
                if (backLink.getKey() == Double.MAX_VALUE) {
                    str += "  " + "&";
                } else if (backLink.getKey() == Double.MAX_VALUE * (-1)) {
                    str += "  " + "-&";
                } else {
                    str += "  " + backLink.getKey();
                }
            }
        }
        str+="\n";
        str+=this.key;
        str+="\n"+"forw:";
        for (FloorsArrayLink forwLink:this.forwardArr)
        {
            if(forwLink==null)
            {
                str += "  " + "this";
            }
            else {
                if (forwLink.getKey() == Double.MAX_VALUE) {
                    str += "  " + "&";
                } else if (forwLink.getKey() == Double.MAX_VALUE * (-1)) {
                    str += "  " + "-&";
                } else {
                    str += "  " + forwLink.getKey();
                }
            }
        }

        return str;
    }
}

