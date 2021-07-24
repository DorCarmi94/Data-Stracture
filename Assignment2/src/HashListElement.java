public class HashListElement {


    private final String value;
    private HashListElement next;

    public HashListElement(String value)
    {
        this.value=value;
        next=null;

    }

    public String getValue() {
        return value;
    }

    public HashListElement getNext(){
        return next;
    }

    public void SetNext(HashListElement newLink)
    {
        this.next=newLink;
    }

}
