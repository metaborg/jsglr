package org.spoofax.jsglr.client.indentation;

public abstract class LogicalNode implements BooleanNode {
  
  protected BooleanNode[] operands;
  
  public LogicalNode(BooleanNode[] operands) {
    super();
    this.operands = operands;
  }

}
