class HashList {

    private HashListElement root;

    public HashList()
    {
        root=null;
    }

    public void Add(String value)
    {
        HashListElement newLink= new HashListElement(value);
        newLink.SetNext(root);
        this.root=newLink;
    }

    public boolean isExist(String pass) {
        HashListElement current = root;
        while(current!=null) {
            if (current.getValue().equals(pass))
                return true;
            current = current.getNext();
        }
        return false;
    }
}
