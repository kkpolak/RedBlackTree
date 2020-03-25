package ASD.structres;

class Node {
    //wezel pusty nil jest zawsze czarny (wedlug teorii Rudolfa Bayer'a)
    public static final Node nil = new Node(-1);
    int key;
    COLOR color;
    Node left;
    Node right;
    Node parent;

    Node(int key) {
        this.key = key;
        color = COLOR.BLACK;
        left = nil;
        right = nil;
        parent = nil;
    }

    int getKey() { return key;}
}
