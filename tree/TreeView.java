package tree;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import exceptions.IsEmpty;
import exceptions.ItemNotFound;

/**
 * TreeViewGraphStream pinta un B+ Tree usando GraphStream.
 * Requiere agregar GraphStream al classpath.
 *
 * Maven dependency:
 * <dependency>
 *   <groupId>org.graphstream</groupId>
 *   <artifactId>gs-core</artifactId>
 *   <version>2.0</version>
 * </dependency>
 *
 * @param <T> tipo de clave Comparable
 */
public class TreeView<T extends Comparable<T>> {
    private final BPlusTree<T> tree;
    private final Graph graph;
    private int counter = 0;

    public TreeView(BPlusTree<T> tree) {
        this.tree = tree;
        this.graph = new SingleGraph("BPlusTree");
        graph.setAttribute("ui.stylesheet",
            "node { fill-color: lightblue; text-size: 16px; } " +
            "edge { fill-color: gray; }"
        );
    }

    /**
     * Construye el grafo a partir del B+ Tree y lo muestra.
     */
    public void display() throws IsEmpty, ItemNotFound {
        build(tree.getRoot(), null);
        graph.display();
    }

    private String build(BPlusTree<T>.Node node, String parentId)
            throws IsEmpty, ItemNotFound {
        String myId = "N" + (counter++);
        Node gNode = graph.addNode(myId);
        gNode.setAttribute("ui.label", node.keys.toString());

        if (parentId != null) {
            String edgeId = parentId + "-" + myId;
            graph.addEdge(edgeId, parentId, myId, true);
        }

        if (!node.isLeaf()) {
            BPlusTree<T>.InternalNode in = (BPlusTree<T>.InternalNode) node;
            for (int i = 0; i < in.children.size(); i++) {
                build(in.children.get(i), myId);
            }
        }
        return myId;
    }
}
