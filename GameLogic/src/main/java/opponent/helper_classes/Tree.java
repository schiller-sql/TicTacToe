package opponent.helper_classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Tree<T extends Tree.TreePrintable> {

    private final T root;
    private final int depth;
    private Tree<T> parent;
    private final ArrayList<Tree<T>> leafs = new ArrayList<>(9);
    private final HashMap<T, Tree<T>> locate = new HashMap<>();

    public interface TreePrintable {
        String toString(String padding);
    }

    public Tree(T root, int depth) {
        this.root = root;
        locate.put(root, this);
        this.depth = depth;
        parent = null;
    }

    public T getRoot() {
        return root;
    }

    public Tree<T> getParent() {
        return parent;
    }

    public Collection<Tree<T>> getSubTrees() {
        return leafs;
    }

    public Tree<T> getAnySubTree(T type) {
        return locate.get(type);
    }

    public Tree<T> addLeaf(T leaf) {
        Tree<T> tree = new Tree<>(leaf, this.depth+1);
        leafs.add(tree);
        tree.setParent(this);
        locate.put(leaf, tree);
        return tree;
    }

    private void setParent(Tree<T> parent) {
        this.parent = parent;
    }

    public void cutLeaf(T leaf) {
        leafs.remove(leaf);
        locate.remove(leaf);
    }

    public Collection<T> getSuccessors(T root) {
        Collection<T> successors = new ArrayList<>();
        Tree<T> tree = getAnySubTree(root);
        if (null != tree) {
            for (Tree<T> leaf : tree.getSubTrees()) {
                successors.add(leaf.getRoot());
            }
        }
        return successors;
    }

    /*private String printTree(int increment) {
        StringBuilder s;
        s = new StringBuilder(" ".repeat(Math.max(0, increment)) + getRoot());
        for (Tree<T> child : getSubTrees()) {
            s.append("\n").append(child.printTree(increment + 4));
        }
        return s.toString();
    }*/


    @Override
    public String toString() {
        return "Tree: " + "\n\n" + printTree(0) + "\n" + "---------------------------------";
    }

    /*private String printTree(int increment) {
        StringBuilder s;
        StringBuilder inc = new StringBuilder();
        for (int i = 0; i < increment; ++i) {
            inc.append(" ");
        }
        s = new StringBuilder(root.toString(inc.toString()));
        for (Tree<T> child : leafs) {
            s.append("\n").append(child.printTree(increment + 4));
        }
        return s.toString();
    }*/

    private String printTree(int increment) {
        StringBuilder s;
        StringBuilder inc = new StringBuilder(" ".repeat(Math.max(0, increment)));
        s = new StringBuilder(root.toString(inc.toString()));
        for (Tree<T> child : leafs) {
            s.append("\n").append(child.printTree(increment + 4));
        }
        return s.toString();
    }
}