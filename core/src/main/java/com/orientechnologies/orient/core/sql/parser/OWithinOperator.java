/* Generated By:JJTree: Do not edit this line. OWithinOperator.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class OWithinOperator extends SimpleNode implements OBinaryCompareOperator {
  public OWithinOperator(int id) {
    super(id);
  }

  public OWithinOperator(OrientSql p, int id) {
    super(p, id);
  }

  @Override
  public boolean execute(Object left, Object right) {
    throw new UnsupportedOperationException(toString() + " operator cannot be evaluated in this context");
  }

  @Override
  public String toString() {
    return "WITHIN";
  }

  @Override
  public boolean supportsBasicCalculation() {
    return true;
  }

  @Override
  public OWithinOperator copy() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && obj.getClass().equals(this.getClass());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
/* JavaCC - OriginalChecksum=e627b2d87bdac6de681d462e4b764288 (do not edit this line) */
