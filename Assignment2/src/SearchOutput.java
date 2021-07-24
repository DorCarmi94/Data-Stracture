class SearchOutput {

    private final BTreeNode node;
    private final int index;

    public SearchOutput(int index, BTreeNode node){
        this.node=node;
        this.index=index;
    }

    public BTreeNode getNode() {
        return node;
    }

    public int getIndex() {
        return index;
    }
}
