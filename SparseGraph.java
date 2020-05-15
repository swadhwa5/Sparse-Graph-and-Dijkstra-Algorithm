package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * An implementation of a directed graph using incidence lists
 * for sparse graphs where most things aren't connected.
 *
 * @param <V> Vertex element type.
 * @param <E> Edge element type.
 */
public class SparseGraph<V, E> implements Graph<V, E> {

  private Set<Vertex<V>> vertices;
  private Set<Edge<E>> edges;//can be a hashtable

  /**
   * Constructor for instantiating a graph.
   */
  public SparseGraph() {
    this.vertices = new LinkedHashSet<>();
    this.edges = new LinkedHashSet<>();
  }

  // Checks vertex belongs to this graph
  private void checkOwner(VertexNode<V> toTest) {
    if (toTest.owner != this) {
      throw new PositionException();
    }
  }

  // Checks edge belongs to this graph
  private void checkOwner(EdgeNode<E> toTest) {
    if (toTest.owner != this) {
      throw new PositionException();
    }
  }

  // Converts the vertex back to a VertexNode to use internally
  private VertexNode<V> convert(Vertex<V> v) throws PositionException {
    try {
      VertexNode<V> gv = (VertexNode<V>) v;
      this.checkOwner(gv);
      return gv;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  // Converts and edge back to a EdgeNode to use internally
  private EdgeNode<E> convert(Edge<E> e) throws PositionException {
    try {
      EdgeNode<E> ge = (EdgeNode<E>) e;
      this.checkOwner(ge);
      return ge;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  private boolean has(Set<Vertex<V>> s, VertexNode<V> vert) {
    for (Vertex<V> ve: vertices) {
      VertexNode<V> vertex = convert(ve);
      if (vertex.data.equals(vert.data)) {
        return true;
      }
    }
    return false;
  }

  private boolean has(Set<Edge<E>> s, EdgeNode<E> edg) {
    for (Edge<E> ed: edges) {
      EdgeNode<E> edge = convert(ed);
      if (edge.from.data.equals(edg.from.data)
          & edge.to.data.equals(edg.to.data)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Vertex<V> insert(V v) {
    if (v == null) {
      return null;
    }
    VertexNode<V> vert = new VertexNode<>(v);
    if (has(vertices, vert)) {
      return vert;
    }
    vert.owner = this;
    vertices.add(vert);
    return vert;
  }

  @Override
  public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
      throws PositionException, InsertionException {

    VertexNode<V> f = convert(from);
    VertexNode<V> t = convert(to);
    insertEdgeExceptionCheck(from, to, f, t);
    EdgeNode<E> edg = new EdgeNode<>(f, t, e);
    if (has(edges, edg)) {
      throw new InsertionException();
    }
    edg.owner = this;
    edges.add(edg);
    t.incoming.add(edg);
    f.outgoing.add(edg);
    return edg;
  }

  /**
   * Checks for exceptions in insert Edge.
   * @param from is the start vertex.
   * @param to is the end vertex.
   * @param f is the start node.
   * @param t is the end node.
   */
  public void insertEdgeExceptionCheck(Vertex<V> from, Vertex<V> to,
                                       VertexNode<V> f, VertexNode<V> t) {
    if (from == null | to == null
        | !has(vertices, f) | !has(vertices, t)) {
      throw new PositionException();
    }
    if (f.data.equals(t.data)) {
      throw new InsertionException();
    }
  }

  @Override
  public V remove(Vertex<V> v) throws PositionException,
      RemovalException {
    VertexNode<V> vert = convert(v);
    if (v == null | !has(vertices, vert)) {
      throw new PositionException();
    }
    if (!vert.outgoing.isEmpty() | !vert.incoming.isEmpty()) {
      throw new RemovalException();
    }

    vertices.remove(vert);
    return vert.data;
  }

  @Override
  public E remove(Edge<E> e) throws PositionException {
    EdgeNode<E> edg = convert(e);
    if (e == null | !has(edges, edg)) {
      throw new PositionException();
    }

    edges.remove(edg);
    edg.from.outgoing.remove(edg);
    edg.to.incoming.remove(edg);
    return edg.data;
  }

  @Override
  public Iterable<Vertex<V>> vertices() {
    return Collections.unmodifiableSet(vertices);
  }

  @Override
  public Iterable<Edge<E>> edges() {
    return Collections.unmodifiableSet(edges);
  }

  @Override
  public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
    VertexNode<V> vert = convert(v);
    if (v == null | !has(vertices, vert)) {
      throw new PositionException();
    }
    return Collections.unmodifiableSet(vert.outgoing);
  }

  @Override
  public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
    VertexNode<V> vert = convert(v);
    if (v == null | !has(vertices, vert)) {
      throw new PositionException();
    }
    return Collections.unmodifiableSet(vert.incoming);
  }

  @Override
  public Vertex<V> from(Edge<E> e) throws PositionException {
    EdgeNode<E> edg = convert(e);
    if (e == null | !has(edges, edg)) {
      throw new PositionException();
    }
    return edg.from;
  }

  @Override
  public Vertex<V> to(Edge<E> e) throws PositionException {
    EdgeNode<E> edg = convert(e);
    if (e == null | !has(edges, edg)) {
      throw new PositionException();
    }
    return edg.to;
  }

  @Override
  public void label(Vertex<V> v, Object l) throws PositionException {
    VertexNode<V> vert = convert(v);
    if (v == null | !has(vertices, vert)) {
      throw new PositionException();
    }
    vert.label = l;
  }

  @Override
  public void label(Edge<E> e, Object l) throws PositionException {
    EdgeNode<E> edg = convert(e);
    if (e == null | !has(edges, edg)) {
      throw new PositionException();
    }
    edg.label = l;
  }

  @Override
  public Object label(Vertex<V> v) throws PositionException {
    VertexNode<V> vert = convert(v);
    if (v == null | !has(vertices, vert)) {
      throw new PositionException();
    }
    return vert.label;
  }

  @Override
  public Object label(Edge<E> e) throws PositionException {
    EdgeNode<E> edg = convert(e);
    if (e == null | !has(edges, edg)) {
      throw new PositionException();
    }
    return edg.label;
  }

  @Override
  public void clearLabels() {
    for (Edge<E> e : edges) {
      EdgeNode<E> edg = convert(e);
      edg.label = null;
    }

    for (Vertex<V> v : vertices) {
      VertexNode<V> vert = convert(v);
      vert.label = null;
    }
  }

  private String vertexString(Vertex<V> v) {
    return "\"" + v.get() + "\"";
  }

  private String verticesToString() {
    StringBuilder sb = new StringBuilder();
    for (Vertex<V> v : this.vertices) {
      sb.append("  ").append(vertexString(v)).append("\n");
    }
    return sb.toString();
  }

  private String edgeString(Edge<E> e) {
    return String.format("%s -> %s [label=\"%s\"]",
        this.vertexString(this.from(e)),
        this.vertexString(this.to(e)),
        e.get());
  }

  private String edgesToString() {
    String edgs = "";
    for (Edge<E> e : this.edges) {
      edgs += "  " + this.edgeString(e) + ";\n";
    }
    return edgs;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("digraph {\n")
        .append(this.verticesToString())
        .append(this.edgesToString())
        .append("}");
    return sb.toString();
  }

  // Class for a vertex of type V
  private final class VertexNode<V>  implements Vertex<V> {
    V data;
    Graph<V, E> owner;
    Set<Edge<E>> outgoing;//can be a hashmap
    Set<Edge<E>> incoming;//can be a hashmap
    Object label;
    private double distance;
    private boolean explored;
    private String prev;

    VertexNode(V v) {
      this.data = v;
      this.outgoing = new LinkedHashSet<>();
      this.incoming = new LinkedHashSet<>();
      this.label = null;
    }

    @Override
    public V get() {

      return this.data;
    }

    @Override
    public void put(V v) {

      this.data = v;
    }

  }

  //Class for an edge of type E
  private final class EdgeNode<E> implements Edge<E> {
    E data;
    Graph<V, E> owner;
    VertexNode<V> from;
    VertexNode<V> to;
    Object label;

    // Constructor for a new edge
    EdgeNode(VertexNode<V> f, VertexNode<V> t, E e) {
      this.from = f;
      this.to = t;
      this.data = e;
      this.label = null;
    }

    @Override
    public E get() {

      return this.data;
    }

    @Override
    public void put(E e) {

      this.data = e;
    }
  }
}
