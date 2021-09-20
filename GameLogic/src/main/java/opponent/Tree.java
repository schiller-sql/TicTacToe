package opponent;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @param <T>
 * @author https://stackoverflow.com/a/4054711
 */
public class Tree<T extends Tree.TreePrintable> {

    interface TreePrintable {
        String toString(String padding);

        String asString();
    }

    //TODO: new method: search T by params of T in Collections of T returns T

    final private T head;
    final private ArrayList<Tree<T>> leafs = new ArrayList<>();
    private Tree<T> parent = null;
    public HashMap<T, Tree<T>> locate = new HashMap<>();

    public Tree(T head) {
        this.head = head;
        locate.put(head, this);
    }

    //TODO: ???
    public Collection<Tree<T>> allTrees() {
        return locate.values();
    }

    public int length() {
        return locate.values().size();
    }

   /* public Collection<T> getLowestChildren() {
        return locate
                .values()
                .stream()
                .filter(tree -> tree.getSubTrees().size() == 0)
                .map(Tree::getHead)
                .toList();
    }*/

    public void addLeaf(T root, T leaf) {
        //gib einem bestimmtem Child ein Leaf
        if (locate.containsKey(root)) {
            locate.get(root).addLeaf(leaf);
        } else {
            addLeaf(root).addLeaf(leaf);
        }
    }

    public Tree<T> addLeaf(T leaf) {
        //Gib .this ein leaf
        Tree<T> t = new Tree<>(leaf);
        leafs.add(t);
        t.parent = this;
        t.locate = this.locate;
        locate.put(leaf, t);
        return t; //returns the tree of the leaf
    }

    public Tree<T> setAsParent(T parentRoot) {
        //.this Tree wird Leaf von parentRoot
        Tree<T> t = new Tree<>(parentRoot);
        t.leafs.add(this);
        this.parent = t;
        t.locate = this.locate;
        t.locate.put(head, this);
        t.locate.put(parentRoot, t);
        return t;
    }

    public T getHead() {
        return head;
    }

    public Tree<T> getTree(T element) {
        //zugriff auf generalisierte HashMap
        return locate.get(element);
    }

    public Tree<T> getParent() {
        //gib parent von .this zurück
        return parent;
    }

    public Collection<T> getSuccessors(T root) {
        //suche anhand des root die heads der childs als T
        Collection<T> successors = new ArrayList<>();
        Tree<T> tree = getTree(root);
        if (null != tree) {
            for (Tree<T> leaf : tree.leafs) {
                successors.add(leaf.head);
            }
        }
        return successors;
    }

    public Collection<Tree<T>> getSubTrees() {
        return leafs;
    }

    public static <T extends TreePrintable> Collection<T> getSuccessors(T of, Collection<Tree<T>> in) {
        //überprüft für jedes Element aus der Collection ob T of als Head enthalten ist
        for (Tree<T> tree : in) {
            if (tree.locate.containsKey(of)) {
                return tree.getSuccessors(of);
            }
        }
        return new ArrayList<>();
    }

    public int getDepthOfTree() { //max dept is 9
        //TODO: complete
        return 10;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        printTreeAlternativ(buffer, "", "", head.toString());
        //return printTree(0);
        return buffer.toString();
    }

    private static final int indent = 4;

    private String printTree(int increment) {
        StringBuilder s;
        StringBuilder inc = new StringBuilder();

        for (int i = 0; i < increment; ++i) {
            inc.append(" ");
        }
        s = new StringBuilder(head.toString(inc.toString()));
        for (Tree<T> child : leafs) {
            s.append("\n").append(child.printTree(increment + indent));
        }
        return s.toString();
    }

    public void printTreeAlternativ(StringBuilder buffer, String s1, String s, String name) {
        buffer.append(s1);
        buffer.append(name); //if (head) => opponent.Node@560123dc
        buffer.append('\n');
        for (Iterator<Tree<T>> it = leafs.iterator(); it.hasNext(); ) {
            Tree<T> next = it.next();
            if (it.hasNext()) {
                next.printTreeAlternativ(buffer, s + "├── ", s + "│   ", head.asString());
            } else {
                next.printTreeAlternativ(buffer, s + "└── ", s + "    ", head.asString());
            }
        }
    }

    /* Intelij Solution
    private String printTree(int increment) {
    StringBuilder s;
    s = new StringBuilder(" ".repeat(Math.max(0, increment)) + head);
        for (Tree<T> child : leafs) {
        s.append("\n").append(child.printTree(increment + indent)); }
        return s.toString();  }
     */
}
