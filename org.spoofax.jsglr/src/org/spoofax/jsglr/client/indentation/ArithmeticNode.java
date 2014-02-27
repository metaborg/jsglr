package org.spoofax.jsglr.client.indentation;

public abstract class ArithmeticNode<V> implements CompilableLayoutNode<V> {

  protected final IntegerNode[] operands;
  
  public ArithmeticNode(IntegerNode[] operands) {
    super();
    this.operands = operands;
  }
  
}
