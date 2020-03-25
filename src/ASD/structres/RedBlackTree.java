package ASD.structres;


class RedBlackTree {
    private Node root;
    private RedBlackTree() {
        root = Node.nil;
    }
    private Node getRoot() {
        return root;
    }

    //METODY POMOCNICZE
    //Jako argument przyjmuje nowo wstawiony węzeł
    private void correctionTree(Node node) {
        while (node.parent.color == COLOR.RED) {
            Node uncle = Node.nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != Node.nil && uncle.color == COLOR.RED) {
                    node.parent.color = COLOR.BLACK;
                    uncle.color = COLOR.BLACK;
                    node.parent.parent.color = COLOR.RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.right) {
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = COLOR.BLACK;
                node.parent.parent.color = COLOR.RED;
                // jeśli kod „else if” nie został wykonany, to
                // to jest to przypadek, w którym potrzebujemy tylko jednej rotacji
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                if (uncle != Node.nil && uncle.color == COLOR.RED) {
                    node.parent.color = COLOR.BLACK;
                    uncle.color = COLOR.BLACK;
                    node.parent.parent.color = COLOR.RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = COLOR.BLACK;
                node.parent.parent.color = COLOR.RED;
                // jeśli kod „else if” nie został wykonany, to
                // to jest to przypadek, w którym potrzebujemy tylko jednej rotacji
                rotateLeft(node.parent.parent);
            }
        }
        root.color = COLOR.BLACK;
    }

    //rotacja w lewo
    private void rotateLeft(Node node) {
        if (node.parent != Node.nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != Node.nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = Node.nil;
            root = right;
        }
    }

    //rotacja w prawo
    private void rotateRight(Node node) {
        if (node.parent != Node.nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != Node.nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = Node.nil;
            root = left;
        }
    }

    //po usunieciu elementu musze sprawdzic czy zachowane sa zasady drzewa czerwono-czarnego
    private void postDeleteCorrection(Node x){
        while(x!=root && x.color == COLOR.BLACK){
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == COLOR.RED){
                    w.color = COLOR.BLACK;
                    x.parent.color = COLOR.RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == COLOR.BLACK && w.right.color == COLOR.BLACK){
                    w.color = COLOR.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == COLOR.BLACK){
                    w.left.color = COLOR.BLACK;
                    w.color = COLOR.RED;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == COLOR.RED){
                    w.color = x.parent.color;
                    x.parent.color = COLOR.BLACK;
                    w.right.color = COLOR.BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == COLOR.RED){
                    w.color = COLOR.BLACK;
                    x.parent.color = COLOR.RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == COLOR.BLACK && w.left.color == COLOR.BLACK){
                    w.color = COLOR.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == COLOR.BLACK){
                    w.right.color = COLOR.BLACK;
                    w.color = COLOR.RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == COLOR.RED){
                    w.color = x.parent.color;
                    x.parent.color = COLOR.BLACK;
                    w.left.color = COLOR.BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = COLOR.BLACK;
    }

    private void printTree(Node node) {
        if (node == Node.nil) {
            return;
        }
        printTree(node.left);
        if(node.parent.getKey()==-1){
            System.out.print(((node.color==COLOR.RED)?"Kolor: Czerwony ":"Kolor: Czarny ")+"Key: "+node.getKey()+" Parent: "+"START"+"\n");
        } else {
            System.out.print(((node.color == COLOR.RED) ? "Kolor: Czerwony " : "Kolor: Czarny ") + "Key: " + node.getKey() + " Parent: " + node.parent.getKey() + "\n");
        }
        printTree(node.right);
    }

    //Ta operacja nie ma wpływu na połączenia nowego węzła z lewym i prawym poprzednim węzłem
    private void transplant(Node target, Node with){
        if(target.parent == Node.nil){
            root = with;
        }else if(target == target.parent.left){
            target.parent.left = with;
        }else
            target.parent.right = with;
        with.parent = target.parent;
    }

    private void resetTree(){
        root = Node.nil;
    }



    //METODY GLOWNE
    private void insert(Node node) {
        Node temp = root;
        if (root == Node.nil) {
            root = node;
            node.color = COLOR.BLACK;
            node.parent = Node.nil;
        } else {
            node.color = COLOR.RED;
            while (true) {
                if (node.getKey() < temp.getKey()) {
                    if (temp.left == Node.nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.getKey() >= temp.getKey()) {
                    if (temp.right == Node.nil) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            correctionTree(node);
        }
    }

    private void delete(Node node){
        if((node = search(node, root))==null)return;
        Node toCorrect;
        Node tmpDelete = node;
        COLOR deleteColor = tmpDelete.color;

        if(node.left == Node.nil){
            toCorrect = node.right;
            transplant(node, node.right);
        }else if(node.right == Node.nil){
            toCorrect = node.left;
            transplant(node, node.left);
        }else{
            Node tmpNode = node.right;
            while(tmpNode.left!=Node.nil){ //szukam minimum
                tmpNode = tmpNode.left;
            }
            tmpDelete = tmpNode;
            deleteColor = tmpDelete.color;
            toCorrect = tmpDelete.right;
            if(tmpDelete.parent == node)
                toCorrect.parent = tmpDelete;
            else{
                transplant(tmpDelete, tmpDelete.right);
                tmpDelete.right = node.right;
                tmpDelete.right.parent = tmpDelete;
            }
            transplant(node, tmpDelete);
            tmpDelete.left = node.left;
            tmpDelete.left.parent = tmpDelete;
            tmpDelete.color = node.color;
        }
        if(deleteColor==COLOR.BLACK)
            postDeleteCorrection(toCorrect);
    }

    private Node search(Node findNode, Node tmpRoot) {
        if (root == Node.nil) {
            return null;
        }

        if (findNode.key < tmpRoot.key) {
            if (tmpRoot.left != Node.nil) {
                return search(findNode, tmpRoot.left);
            }
        } else if (findNode.key > tmpRoot.key) {
            if (tmpRoot.right != Node.nil) {
                return search(findNode, tmpRoot.right);
            }
        } else if (findNode.key == tmpRoot.key) {
            return tmpRoot;
        }

        return null;
    }

    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();

        //TEST 1 START
        System.out.println("TEST 1");
        int[] test1 = {10,24,50,25,35};
        Node[] myTree1 = new Node[test1.length];
        for(int i = 0; i < test1.length; i++) myTree1[i] = new Node(test1[i]);
        for (int i = 0; i < test1.length; i++) rbt.insert(myTree1[i]);
        rbt.printTree(rbt.getRoot());
        System.out.println(rbt.search(new Node(24),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(10),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(25),rbt.getRoot()).getKey());
        rbt.delete(new Node(10));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(25));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(20));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(30));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(35));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.resetTree();
        //KONIEC TEST1

        //TEST 2 START
        System.out.println("TEST 2");
        int[] test2 = {22, 33, 4, 5, 6, 7, 8, 55, 3, 2, 1, 6, 4};
        Node[] myTree2 = new Node[test2.length];
        for(int i = 0; i < test2.length; i++) myTree2[i] = new Node(test2[i]);
        for (int i = 0; i < test2.length; i++) rbt.insert(myTree2[i]);
        rbt.printTree(rbt.getRoot());
        System.out.println(rbt.search(new Node(22),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(1),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(5),rbt.getRoot()).getKey());
        rbt.delete(new Node(33));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(55));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(6));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(4));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(7));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.resetTree();
        //KONIEC TEST2

        //TEST 3 START
        System.out.println("TEST 3");
        int[] test3 = {99,88,11,22,33,55,77,66,44,111};
        Node[] myTree3 = new Node[test3.length];
        for(int i = 0; i < test3.length; i++) myTree3[i] = new Node(test3[i]);
        for (int i = 0; i < test3.length; i++) rbt.insert(myTree3[i]);
        rbt.printTree(rbt.getRoot());
        System.out.println(rbt.search(new Node(22),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(111),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(99),rbt.getRoot()).getKey());
        rbt.delete(new Node(99));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(55));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(111));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(44));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(77));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.resetTree();
        //KONIEC TEST3

        //TEST 4 START
        System.out.println("TEST 4");
        int[] test4 = {20,10,30,25,35};
        Node[] myTree4 = new Node[test4.length];
        for(int i = 0; i < test4.length; i++) myTree4[i] = new Node(test4[i]);
        for (int i = 0; i < test4.length; i++) rbt.insert(myTree4[i]);
        rbt.printTree(rbt.getRoot());
        System.out.println(rbt.search(new Node(35),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(30),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(25),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(20),rbt.getRoot()).getKey());
        System.out.println(rbt.search(new Node(10),rbt.getRoot()).getKey());
        rbt.delete(new Node(35));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(10));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(20));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(30));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.delete(new Node(25));
        rbt.printTree(rbt.getRoot());
        System.out.println();
        rbt.resetTree();
        //KONIEC TEST4

    }
}
//START to miejsce gdzie startuje drzewo, pod startem jest root
/*
W Y N I K I

TEST 1
Kolor: Czarny Key: 10 Parent: 24
Kolor: Czarny Key: 24 Parent: START
Kolor: Czerwony Key: 25 Parent: 35
Kolor: Czarny Key: 35 Parent: 24
Kolor: Czerwony Key: 50 Parent: 35
24
10
25
Kolor: Czarny Key: 24 Parent: 35
Kolor: Czerwony Key: 25 Parent: 24
Kolor: Czarny Key: 35 Parent: START
Kolor: Czarny Key: 50 Parent: 35

Kolor: Czarny Key: 24 Parent: 35
Kolor: Czarny Key: 35 Parent: START
Kolor: Czarny Key: 50 Parent: 35

Kolor: Czarny Key: 24 Parent: 35
Kolor: Czarny Key: 35 Parent: START
Kolor: Czarny Key: 50 Parent: 35

Kolor: Czarny Key: 24 Parent: 35
Kolor: Czarny Key: 35 Parent: START
Kolor: Czarny Key: 50 Parent: 35

Kolor: Czerwony Key: 24 Parent: 50
Kolor: Czarny Key: 50 Parent: START

TEST 2
Kolor: Czerwony Key: 1 Parent: 2
Kolor: Czarny Key: 2 Parent: 3
Kolor: Czarny Key: 3 Parent: 5
Kolor: Czarny Key: 4 Parent: 3
Kolor: Czerwony Key: 4 Parent: 4
Kolor: Czarny Key: 5 Parent: START
Kolor: Czarny Key: 6 Parent: 7
Kolor: Czerwony Key: 6 Parent: 6
Kolor: Czerwony Key: 7 Parent: 22
Kolor: Czarny Key: 8 Parent: 7
Kolor: Czarny Key: 22 Parent: 5
Kolor: Czarny Key: 33 Parent: 22
Kolor: Czerwony Key: 55 Parent: 33
22
1
5
Kolor: Czerwony Key: 1 Parent: 2
Kolor: Czarny Key: 2 Parent: 3
Kolor: Czarny Key: 3 Parent: 5
Kolor: Czarny Key: 4 Parent: 3
Kolor: Czerwony Key: 4 Parent: 4
Kolor: Czarny Key: 5 Parent: START
Kolor: Czarny Key: 6 Parent: 7
Kolor: Czerwony Key: 6 Parent: 6
Kolor: Czerwony Key: 7 Parent: 22
Kolor: Czarny Key: 8 Parent: 7
Kolor: Czarny Key: 22 Parent: 5
Kolor: Czarny Key: 55 Parent: 22

Kolor: Czerwony Key: 1 Parent: 2
Kolor: Czarny Key: 2 Parent: 3
Kolor: Czarny Key: 3 Parent: 5
Kolor: Czarny Key: 4 Parent: 3
Kolor: Czerwony Key: 4 Parent: 4
Kolor: Czarny Key: 5 Parent: START
Kolor: Czarny Key: 6 Parent: 7
Kolor: Czerwony Key: 6 Parent: 6
Kolor: Czarny Key: 7 Parent: 5
Kolor: Czerwony Key: 8 Parent: 22
Kolor: Czarny Key: 22 Parent: 7

Kolor: Czerwony Key: 1 Parent: 2
Kolor: Czarny Key: 2 Parent: 3
Kolor: Czarny Key: 3 Parent: 5
Kolor: Czarny Key: 4 Parent: 3
Kolor: Czerwony Key: 4 Parent: 4
Kolor: Czarny Key: 5 Parent: START
Kolor: Czarny Key: 6 Parent: 7
Kolor: Czarny Key: 7 Parent: 5
Kolor: Czerwony Key: 8 Parent: 22
Kolor: Czarny Key: 22 Parent: 7

Kolor: Czerwony Key: 1 Parent: 2
Kolor: Czarny Key: 2 Parent: 3
Kolor: Czarny Key: 3 Parent: 5
Kolor: Czarny Key: 4 Parent: 3
Kolor: Czarny Key: 5 Parent: START
Kolor: Czarny Key: 6 Parent: 7
Kolor: Czarny Key: 7 Parent: 5
Kolor: Czerwony Key: 8 Parent: 22
Kolor: Czarny Key: 22 Parent: 7

Kolor: Czerwony Key: 1 Parent: 2
Kolor: Czarny Key: 2 Parent: 3
Kolor: Czarny Key: 3 Parent: 5
Kolor: Czarny Key: 4 Parent: 3
Kolor: Czarny Key: 5 Parent: START
Kolor: Czarny Key: 6 Parent: 8
Kolor: Czarny Key: 8 Parent: 5
Kolor: Czarny Key: 22 Parent: 8

TEST 3
Kolor: Czarny Key: 11 Parent: 22
Kolor: Czerwony Key: 22 Parent: 55
Kolor: Czarny Key: 33 Parent: 22
Kolor: Czerwony Key: 44 Parent: 33
Kolor: Czarny Key: 55 Parent: START
Kolor: Czerwony Key: 66 Parent: 77
Kolor: Czarny Key: 77 Parent: 88
Kolor: Czerwony Key: 88 Parent: 55
Kolor: Czarny Key: 99 Parent: 88
Kolor: Czerwony Key: 111 Parent: 99
22
111
99
Kolor: Czarny Key: 11 Parent: 22
Kolor: Czerwony Key: 22 Parent: 55
Kolor: Czarny Key: 33 Parent: 22
Kolor: Czerwony Key: 44 Parent: 33
Kolor: Czarny Key: 55 Parent: START
Kolor: Czerwony Key: 66 Parent: 77
Kolor: Czarny Key: 77 Parent: 88
Kolor: Czerwony Key: 88 Parent: 55
Kolor: Czarny Key: 111 Parent: 88

Kolor: Czarny Key: 11 Parent: 22
Kolor: Czerwony Key: 22 Parent: 66
Kolor: Czarny Key: 33 Parent: 22
Kolor: Czerwony Key: 44 Parent: 33
Kolor: Czarny Key: 66 Parent: START
Kolor: Czarny Key: 77 Parent: 88
Kolor: Czerwony Key: 88 Parent: 66
Kolor: Czarny Key: 111 Parent: 88

Kolor: Czarny Key: 11 Parent: 22
Kolor: Czerwony Key: 22 Parent: 66
Kolor: Czarny Key: 33 Parent: 22
Kolor: Czerwony Key: 44 Parent: 33
Kolor: Czarny Key: 66 Parent: START
Kolor: Czerwony Key: 77 Parent: 88
Kolor: Czarny Key: 88 Parent: 66

Kolor: Czarny Key: 11 Parent: 22
Kolor: Czerwony Key: 22 Parent: 66
Kolor: Czarny Key: 33 Parent: 22
Kolor: Czarny Key: 66 Parent: START
Kolor: Czerwony Key: 77 Parent: 88
Kolor: Czarny Key: 88 Parent: 66

Kolor: Czarny Key: 11 Parent: 22
Kolor: Czerwony Key: 22 Parent: 66
Kolor: Czarny Key: 33 Parent: 22
Kolor: Czarny Key: 66 Parent: START
Kolor: Czarny Key: 88 Parent: 66

TEST 4
Kolor: Czarny Key: 10 Parent: 20
Kolor: Czarny Key: 20 Parent: START
Kolor: Czerwony Key: 25 Parent: 30
Kolor: Czarny Key: 30 Parent: 20
Kolor: Czerwony Key: 35 Parent: 30
35
30
25
20
10
Kolor: Czarny Key: 10 Parent: 20
Kolor: Czarny Key: 20 Parent: START
Kolor: Czerwony Key: 25 Parent: 30
Kolor: Czarny Key: 30 Parent: 20

Kolor: Czarny Key: 20 Parent: 25
Kolor: Czarny Key: 25 Parent: START
Kolor: Czarny Key: 30 Parent: 25

Kolor: Czarny Key: 25 Parent: START
Kolor: Czerwony Key: 30 Parent: 25

Kolor: Czarny Key: 25 Parent: START
 */
