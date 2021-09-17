package opponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author https://stackoverflow.com/a/4054711
 * @param <T>
 */
public class Tree<T> {

    final private T head;
    final private ArrayList<Tree<T>> leafs = new ArrayList<Tree<T>>();
    private Tree<T> parent = null;
    private HashMap<T, Tree<T>> locate = new HashMap<T, Tree<T>>();

    public Tree(T head) {
        this.head = head;
        locate.put(head, this);
    }

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
        return t;
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

    public static <T> Collection<T> getSuccessors(T of, Collection<Tree<T>> in) {
        //überprüft für jedes Element aus der Collection ob T of als Head enthalten ist
        for (Tree<T> tree : in) {
            if (tree.locate.containsKey(of)) {
                return tree.getSuccessors(of);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return printTree(0);
    }

    private static final int indent = 2;

    private String printTree(int increment) {
        StringBuilder s;
        StringBuilder inc = new StringBuilder();
        for (int i = 0; i < increment; ++i) {
            inc.append(" ");
        }
        s = new StringBuilder(inc.toString() + head);
        for (Tree<T> child : leafs) {
            s.append("\n").append(child.printTree(increment + indent));
        }
        return s.toString();
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
