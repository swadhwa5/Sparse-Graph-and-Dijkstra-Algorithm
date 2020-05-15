package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.*;
import static junit.framework.TestCase.assertEquals;

public abstract class GraphTest {

  protected Graph<String, String> graph;
  protected Graph<String, String> graph1;

  @Before
  public void setupGraph() {

    this.graph = createGraph();
    this.graph1 = createGraph();
  }


  protected abstract Graph<String, String> createGraph();

  @Test
  public void testInsertVertex() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(v1.get(), "v1");
  }

  @Test
  public void testCannotInsertVertexIfNull()  {
    assertNull(graph.insert(null));
  }

  @Test
  public void testCannotInsertVertexIfAlreadyPresent()  {
    int count = 0;
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v1");
    for (Vertex<String> v: graph.vertices()) {
      count++;
    }
    assertEquals(1, count);
  }

  @Test(expected = PositionException.class)
  public void testFromThrowsPositionExceptionIfEdgeNotInGraph() {
    Vertex<String> v1 = graph1.insert("v1");
    Vertex<String> v2 = graph1.insert("v2");
    Edge<String> e1 = graph1.insert(v1, v2, "v1-v2");
    graph.from(e1);
  }

  @Test
  public void testFrom() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(graph.from(e1).get(), "v1");
  }

  @Test(expected = PositionException.class)
  public void testToThrowsPositionExceptionIfEdgeNotInGraph() {
    Vertex<String> v1 = graph1.insert("v1");
    Vertex<String> v2 = graph1.insert("v2");
    Edge<String> e1 = graph1.insert(v1, v2, "v1-v2");
    graph.to(e1);
  }

  @Test
  public void testTo() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(graph.to(e1).get(), "v2");
  }

  @Test
  public void testInsertEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(e1.get(), "v1-v2");
    assertEquals(v1.get(), graph.from(e1).get());
    assertEquals(v2.get(), graph.to(e1).get());
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenFirstVertexIsNull() {
    Vertex<String> v = graph.insert("v");
    Edge<String> e = graph.insert(null, v, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenSecondVertexIsNull() {
    Vertex<String> v = graph.insert("v");
    Edge<String> e = graph.insert(v, null, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenFirstVertexIsInvalid() {
    Graph<String, String> otherGraph = createGraph();
    Vertex<String> v1 = otherGraph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e = graph.insert(v1, v2, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenSecondVertexIsInvalid() {
    Graph<String, String> otherGraph = createGraph();
    Vertex<String> v1 = otherGraph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e = graph.insert(v2, v1, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenFirstVertexWasRemoved() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    graph.remove(v1);
    graph.insert(v1, v2, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenSecondVertexWasRemoved() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    graph.remove(v2);
    graph.insert(v1, v2, "e");
  }

  @Test(expected = InsertionException.class)
  public void testInsertEdgeThrowsInsertionExceptionForSelfLoopEdge() {
    Vertex<String> v = graph.insert("v");
    graph.insert(v, v, "e");
  }

  @Test(expected = InsertionException.class)
  public void testInsertEdgeThrowsInsertionExceptionForMultipleParallelEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    graph.insert(v1, v2, "v1-v2");
    graph.insert(v1, v2, "v1-v2");
  }

  @Test
  public void testInsertMultipleEdgeOppositeDirection() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v2, v1, "v2-v1");
    assertEquals(e1.get(), "v1-v2");
    assertEquals(e2.get(), "v2-v1");
  }

  @Test(expected = PositionException.class)
  public void testRemoveVertexThrowsPositionExceptionWhenVertexIsNull() {
    Vertex<String> v = graph.insert(null);
    graph.remove(v);
  }

  @Test(expected = PositionException.class)
  public void testRemoveVertexThrowsPositionExceptionWhenVertexIsNotPresentInGraph() {
    Vertex<String> v3 = graph1.insert("v3");
    graph.remove(v3);
  }

  @Test(expected = RemovalException.class)
  public void testRemoveVertexThrowsRemovalExceptionWhenVertexHasIncidentOutgoingEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.remove(v1);
  }

  @Test(expected = RemovalException.class)
  public void testRemoveVertexThrowsRemovalExceptionWhenVertexHasIncidentIncomingEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.remove(v2);
  }

  @Test
  public void testRemoveVertex() {
    int count = 0;
    Vertex<String> v1 = graph.insert("v1");
    assertEquals("v1", graph.remove(v1));
    for (Vertex<String> v: graph.vertices()) {
      count++;
    }
    assertEquals(0, count);
  }

  @Test(expected = PositionException.class)
  public void testRemoveEdgeThrowsPositionExceptionWhenEdgeIsNotPresentInGraph() {
    Vertex<String> v3 = graph1.insert("v3");
    Vertex<String> v4 = graph1.insert("v4");
    Edge<String> e2 = graph1.insert(v3, v4, "v3-v4");
    graph.remove(e2);
  }

  @Test
  public void testRemoveEdge() {
    int edgeCount = 0;
    int outCount = 0;
    int inCount = 0;
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals("v1-v2",graph.remove(e1));
    for (Edge<String> e : graph.edges()) {
      edgeCount++;
    }
    assertEquals(0, edgeCount);
    for (Edge<String> e : graph.outgoing(v1)) {
      outCount++;
    }
    assertEquals(0, outCount);
    for (Edge<String> e: graph.incoming(v2)) {
      inCount++;
    }
    assertEquals(0, inCount);
  }

  @Test
  public void testEdgesIterable() {
    ArrayList<String> a = new ArrayList<>();
    a.add("v1-v2");
    a.add("v2-v3");
    a.add("v3-v4");
    a.add("v1-v5");
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    Edge<String> e1 = (graph.insert(v1, v2, "v1-v2"));
    Edge<String> e2 = (graph.insert(v2, v3, "v2-v3"));
    Edge<String> e3 = (graph.insert(v3, v4, "v3-v4"));
    Edge<String> e4 = (graph.insert(v1, v5, "v1-v5"));
    int i = 0;
    for (Edge<String> e : graph.edges()) {
      assertEquals(e.get(), a.get(i));
      i++;
    }
  }

  @Test
  public void testVerticesIterable() {
    ArrayList<String> a = new ArrayList<>();
    a.add("v1");
    a.add("v2");
    a.add("v3");
    a.add("v4");
    a.add("v5");
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    int i = 0;
    for (Vertex<String> v : graph.vertices()) {
      assertEquals(v.get(), a.get(i));
      i++;
    }
  }

  @Test(expected = PositionException.class)
  public void testOutgoingIterableThrowsPositionExceptionIfVertexNull() {
    graph.outgoing(null);
  }

  @Test(expected = PositionException.class)
  public void testOutgoingIterableThrowsPositionExceptionIfVertexNotPresentInGraph() {
    Vertex<String> v1 = graph1.insert("v1");
    graph.outgoing(v1);
  }

  @Test
  public void testOutgoingIterable() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Edge<String> e1 = (graph.insert(v1, v2, "v1-v2"));
    Edge<String> e2 = (graph.insert(v2, v3, "v2-v3"));
    Edge<String> e3 = (graph.insert(v3, v1, "v3-v1"));
    Edge<String> e4 = (graph.insert(v1, v4, "v1-v4"));
    ArrayList<String> v1Out = new ArrayList<>();
    v1Out.add("v1-v2");
    v1Out.add("v1-v4");
    ArrayList<String> v2Out = new ArrayList<>();
    v2Out.add("v2-v3");
    ArrayList<String> v3Out = new ArrayList<>();
    v3Out.add("v3-v1");
    int i = 0;
    for (Edge<String> e : graph.outgoing(v1)) {
      assertEquals(e.get(), v1Out.get(i));
      i++;
    }
    int j = 0;
    for (Edge<String> e : graph.outgoing(v2)) {
      assertEquals(e.get(), v2Out.get(j));
      j++;
    }
    int k = 0;
    for (Edge<String> e : graph.outgoing(v3)) {
      assertEquals(e.get(), v3Out.get(k));
      k++;
    }
  }

  @Test(expected = PositionException.class)
  public void testIncomingIterableThrowsPositionExceptionIfVertexNull() {
    graph.incoming(null);
  }

  @Test(expected = PositionException.class)
  public void testIncomingIterableThrowsPositionExceptionIfVertexNotPresentInGraph() {
    Vertex<String> v1 = graph1.insert("v1");
    graph.incoming(v1);
  }

  @Test
  public void testIncomingIterable() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Edge<String> e1 = (graph.insert(v1, v2, "v1-v2"));
    Edge<String> e2 = (graph.insert(v2, v3, "v2-v3"));
    Edge<String> e3 = (graph.insert(v3, v1, "v3-v1"));
    Edge<String> e4 = (graph.insert(v4, v1, "v4-v1"));
    ArrayList<String> v1In = new ArrayList<>();
    v1In.add("v3-v1");
    v1In.add("v4-v1");
    ArrayList<String> v2In = new ArrayList<>();
    v2In.add("v1-v2");
    ArrayList<String> v3In = new ArrayList<>();
    v3In.add("v2-v3");
    int i = 0;
    for (Edge<String> e : graph.incoming(v1)) {
      assertEquals(e.get(), v1In.get(i));
      i++;
    }
    int j = 0;
    for (Edge<String> e : graph.incoming(v2)) {
      assertEquals(e.get(), v2In.get(j));
      j++;
    }
    int k = 0;
    for (Edge<String> e : graph.incoming(v3)) {
      assertEquals(e.get(), v3In.get(k));
      k++;
    }
  }


  @Test
  public void testVertexLabel() {
    Vertex<String> v1 = graph.insert("v1");
    graph.label(v1, 1);
    Vertex<String> v2 = graph.insert("v2");
    graph.label(v2, "3");
    assertEquals(graph.label(v1), 1);
    assertEquals(graph.label(v2), "3");
  }

  @Test(expected = PositionException.class)
  public void testVertexLabellingThrowsExceptionifVertexisNull() {
    Vertex<String> v1 = graph.insert(null);
    graph.label(v1, 1);
  }

  @Test(expected = PositionException.class)
  public void testVertexLabellingThrowsExceptionifVertexisNotPresentInGraph() {
    Vertex<String> v2 = graph1.insert("v2");
    graph.label(v2, 1);
  }

  @Test(expected = PositionException.class)
  public void testVertexLabelThrowsExceptionifVertexisNull() {
    Vertex<String> v1 = graph.insert(null);
    graph.label(v1);
  }

  @Test(expected = PositionException.class)
  public void testVertexLabelThrowsExceptionifVertexisNotPresentInGraph() {
    Vertex<String> v2 = graph1.insert("v2");
    graph.label(v2);
  }

  @Test
  public void testEdgeLabel() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(e1, 1);
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Edge<String> e2 = graph.insert(v3, v4, "v3-v4");
    graph.label(e2, "3");
    assertEquals(graph.label(e1), 1);
    assertEquals(graph.label(e2), "3");
  }

  @Test(expected = PositionException.class)
  public void testEdgeLabellingThrowsExceptionifEdgeisNotPresentInGraph() {
    Vertex<String> v1 = graph1.insert("v1");
    Vertex<String> v2 = graph1.insert("v2");
    Edge<String> e1 = graph1.insert(v1, v2, "v1-v2");
    graph.label(e1, 1);
  }


  @Test(expected = PositionException.class)
  public void testEdgeLabelThrowsExceptionifEdgeisNotPresentInGraph() {
    Vertex<String> v1 = graph1.insert("v1");
    Vertex<String> v2 = graph1.insert("v2");
    Edge<String> e1 = graph1.insert(v1, v2, "v1-v2");
    graph.label(e1);
  }

  @Test
  public void testClearLabels() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(v1, "3");
    graph.label(e1, 1);
    graph.clearLabels();
    assertNull(graph.label(v1));
    assertNull(graph.label(e1));
  }

}
