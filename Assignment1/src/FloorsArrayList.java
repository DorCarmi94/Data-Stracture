package default_package;

public class FloorsArrayList implements DynamicSet {

    private int limit;
    private int size;
    private FloorsArrayLink negativeInfinity;
    private FloorsArrayLink positiveInfinity;
    public static final double PlusInfinityValue= Double.MAX_VALUE;
    public static final double MinusInfinityValue= Double.MAX_VALUE*(-1);

    ///////////////////////////////////////////////////////
    // The following object is used to evaluate the runtime
    //public Complicity com;
    ///////////////////////////////////////////////////////

    private int maxArrFilled;   //Used to point the highest cell in the [0] link array-
                                //the most "far" link from the first link in the list

    private FloorsArrayLink current;
    private FloorsArrayLink first;


    public FloorsArrayList(int N){
        this.limit=N;
        size=0;
        this.maxArrFilled=0;
        negativeInfinity=new FloorsArrayLink(MinusInfinityValue,N);
        positiveInfinity=new FloorsArrayLink(PlusInfinityValue,N);


        for (int i=1; i<=limit; i++ ){
            negativeInfinity.setNext(i,positiveInfinity);
            positiveInfinity.setPrev(i,negativeInfinity);
        }
        current=negativeInfinity;
        first=current.getNext(1);


    }


    public int getSize(){
        return this.size;
    }


    public FloorsArrayLink lookup(double key) {
        int index=maxArrFilled;
        if(size==0){
            //There are no links in the list
            current= negativeInfinity;
            return null;
        }
        //Starting the lookup with the far left link, "minus-infinity"
        current=negativeInfinity;


        while (index>0)
        {
            Double nextKey=current.getNext(index).getKey();
            if(nextKey>key)
            {
                //Looking too far to the right
                if(index-1>0)
                {
                    //Shorting the "looking range": the amount of values that will be passed on in the next lookup stage
                    //com.O++;
                    index--;
                }
                else
                {
                    if(current.getNext(index).getKey()==PlusInfinityValue)
                    {
                        //The next closest link is the Plus Infinity => the wanted link doesn't exist in the list
                        //com.O++;
                        return null;
                    }

                }

            }
            else if(nextKey==key)
            {
                //Found the wanted link, returning it
                //com.O++;
                return current.getNext(index);
            }
            else
            {
                //com.O++;
                //In this point, on the current level of the list, the value is smaller then the wanted value
                //Therefore, moving to the next link on the same height (Height = arrSize)
                current=current.getNext(index);
            }
        }
        //In this point index is smaller then 0, which means the wanted link doesn't exist, so returning null
        return null;
    }

    public void insert(double key, int arrSize) {
        FloorsArrayLink toInsert = new FloorsArrayLink(key, arrSize);

        int i=0;
        if(arrSize>maxArrFilled)
        {
            maxArrFilled=arrSize;
        }

        ///Fixing arrays from (-Infinity) --> fowrard
        int curr_index=maxArrFilled;
        FloorsArrayLink fixingNow= negativeInfinity;
        while (curr_index>0)
        {
            if(fixingNow.getNext(curr_index).getKey()<key)
            {
                //com.O++;
                //Moving on the same level (height) to a link with bigger value,
                //that is smaller then the new link's value.

                fixingNow=fixingNow.getNext(curr_index);
            }
            else
            {
                if(curr_index>arrSize)
                {
                    //Adjusting the looking level (height) to the new link's array size
                    curr_index--;
                }
                else
                {
                    //com.O++;
                    //In this point we are on a link that it's height is smaller or equals to the ne link's height
                    //and that is the closest in it's value size (from below) to the new link's value.
                    //So it is necessary to adjust the pointers to the new link and back
                    fixingNow.setNext(curr_index,toInsert);
                    toInsert.setPrev(curr_index,fixingNow);
                    curr_index--;
                }

            }
        }

        ///Fixing arrays from: <--(+Infinity) : backward
        curr_index=arrSize; //On the other side starting from the cell in the new array's size level

        fixingNow= positiveInfinity;
        while (curr_index>0)
        {
            if(fixingNow.getPrev(curr_index).getKey()>key)
            {
                //Moving on the same level (height) to a link with smaller value,
                //that is bigger then the new link's value.
                //com.O++;
                fixingNow=fixingNow.getPrev(curr_index);
            }
            else
            {
                //com.O++;
                //In this point we are on a link that it's height is smaller or equals to the new link's height
                //and that is the closest in it's value (from above) to the new link's value.
                //So it is necessary to adjust the pointers to the new link and back
                fixingNow.setPrev(curr_index,toInsert);
                toInsert.setNext(curr_index,fixingNow);
                curr_index--;
            }
        }
        size++;
        first=negativeInfinity.getNext(1);
    }


    public void remove(FloorsArrayLink toRemove) {
        int arrSize=toRemove.getArrSize();
        FloorsArrayLink prev = toRemove.getPrev(1);
        FloorsArrayLink next = toRemove.getNext(1);

        int i=0;


        for (i=0;i<arrSize;i++)
        {
            //com.O++;
            //Every link that is being removed should connect between it's both sides
            //A cell i on the link's array should be adjusted this way:
            //The link pointing to this cell on this level from below (left),
            //should point to the link in the same level i from above (right) and the opposite
            toRemove.getNext(i+1).setPrev(i+1,toRemove.getPrev(i+1));
            toRemove.getPrev(i+1).setNext(i+1,toRemove.getNext(i+1));
        }
        size--;
        first=negativeInfinity.getNext(1);


    }


    public double successor(FloorsArrayLink link) {
        if(link.getNext(0)==positiveInfinity)
            return negativeInfinity.getKey();
        else{
            return link.getNext(0).getKey();
        }
    }


    public double predecessor(FloorsArrayLink link) {
        if(link.getPrev(0)==negativeInfinity)
            return positiveInfinity.getKey();
        else
            return link.getPrev(0).getKey();
    }


    public double minimum() {
        return negativeInfinity.getNext(0).getKey();
    }


    public double maximum() {
        return positiveInfinity.getPrev(0).getKey();
    }

    public String toString()
    {
        String str="";
        FloorsArrayLink curr=first;
        while(curr!=positiveInfinity && curr!=negativeInfinity)
        {
            str+=curr.toString()+"\n\n";
            curr=curr.getNext(1);
        }

        return str;
    }
}
