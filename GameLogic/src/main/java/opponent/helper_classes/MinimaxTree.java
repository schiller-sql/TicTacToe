package opponent.helper_classes;

import java.util.*;

/**
 * This class represents a generic tree data structure
 * This tree is used by the MinimaxOpponent.java
 *
 * @param <T>
 * @author https://stackoverflow.com/a/4054711
 */
public class MinimaxTree<T extends MinimaxTree.TreePrintable> {

    public interface TreePrintable {
        String toString(String padding);

        String asString();
    }

    //TODO: new method: search T by params of T in Collections of T returns T
    //TODO: new method: getDepth()
    //TODO: new method: calculateDepth()

    final private T head;
    final private ArrayList<MinimaxTree<T>> leafs = new ArrayList<>();
    private MinimaxTree<T> parent = null;
    public HashMap<T, MinimaxTree<T>> locate = new HashMap<>();

    /**
     * @param head the root of the tree
     */
    public MinimaxTree(T head) {
        this.head = head;
        locate.put(head, this);
    }

    //TODO: ???
    public Collection<MinimaxTree<T>> allTrees() {
        return locate.values();
    }

    public int length() {
        return locate.values().size();
    }

    public Collection<T> getLowestChildren() { //TODO: unchecked
        return locate
                .values()
                .stream()
                .filter(tree -> tree.getSubTrees().size() == 0)
                .map(MinimaxTree::getHead)
                .toList();
    }

    public void addLeaf(T root, T leaf) { //TODO: unused
        //gib einem bestimmtem Child ein Leaf
        if (locate.containsKey(root)) {
            locate.get(root).addLeaf(leaf);
        } else {
            addLeaf(root).addLeaf(leaf);
        }
    }

    public MinimaxTree<T> addLeaf(T leaf) {
        //Gib .this ein leaf
        MinimaxTree<T> t = new MinimaxTree<>(leaf);
        leafs.add(t);
        t.parent = this;
        t.locate = this.locate;
        locate.put(leaf, t);
        return t; //returns the tree of the leaf
    }

    public MinimaxTree<T> setAsParent(T parentRoot) { //TODO: unused
        //.this Tree wird Leaf von parentRoot
        MinimaxTree<T> t = new MinimaxTree<>(parentRoot);
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

    public MinimaxTree<T> getTree(T element) {
        //zugriff auf generalisierte HashMap
        return locate.get(element);
    }

    public MinimaxTree<T> getParent() {
        //gib parent von .this zurück
        return parent;
    }

    public Collection<T> getSuccessors(T root) {
        //suche anhand des root die heads der childs als T
        Collection<T> successors = new ArrayList<>();
        MinimaxTree<T> tree = getTree(root);
        if (null != tree) {
            for (MinimaxTree<T> leaf : tree.leafs) {
                successors.add(leaf.head);
            }
        }
        return successors;
    }

    public Collection<MinimaxTree<T>> getSubTrees() {
        return leafs;
    }

    public static <T extends TreePrintable> Collection<T> getSuccessors(T of, Collection<MinimaxTree<T>> in) {
        //überprüft für jedes Element aus der Collection ob T of als Head enthalten ist
        for (MinimaxTree<T> tree : in) {
            if (tree.locate.containsKey(of)) {
                return tree.getSuccessors(of);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return printTree(4);
    }

    private static final int indent = 4;

    private String printTree(int increment) {
        StringBuilder s;
        StringBuilder inc = new StringBuilder();
        for (int i = 0; i < increment; ++i) {
            inc.append(" ");
        }
        s = new StringBuilder(head.toString(inc.toString()));
        for (MinimaxTree<T> child : leafs) {
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
