/* Generated By:JJTree: Do not edit this line. OHaSetStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.ODatabaseInternal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.enterprise.OEnterpriseEndpoint;
import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.sql.executor.OInternalResultSet;
import com.orientechnologies.orient.core.sql.executor.OResultInternal;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.util.Map;

public class OHaSetStatement extends OSimpleExecStatement {
  protected OIdentifier operation;
  protected OExpression key;
  protected OExpression value;

  public OHaSetStatement(int id) {
    super(id);
  }

  public OHaSetStatement(OrientSql p, int id) {
    super(p, id);
  }

  @Override
  public OResultSet executeSimple(OCommandContext ctx) {
    OInternalResultSet result = new OInternalResultSet();

    String operation = this.operation.getStringValue();
    Object key = this.key.execute(new OResultInternal(), ctx);
    if (key == null) {
      key = this.key.getDefaultAlias();
    }

    Object value = this.value.execute(new OResultInternal(), ctx);
    if (value == null) {
      value = this.key.getDefaultAlias();
      if (value.equals("null")) {
        value = null;
      }
    }
    ODatabaseInternal db = (ODatabaseInternal) ctx.getDatabase();

    OEnterpriseEndpoint ee = db.getEnterpriseEndpoint();
    if (ee == null) {
      throw new OCommandExecutionException("HA SET statements are only supported in OrientDB Enterprise Edition");
    }
    if (operation.equalsIgnoreCase("status")) {
      String finalResult;
      try {
        ee.haSetDbStatus((ODatabaseDocument) db, String.valueOf(key), String.valueOf(value));
        finalResult = "OK";
      } catch (UnsupportedOperationException e) {
        finalResult = e.getMessage();
      }
      OResultInternal item = new OResultInternal();
      item.setProperty("operation", "ha set status");
      item.setProperty("result", finalResult);
      result.add(item);
    } else if (operation.equalsIgnoreCase("owner")) {
      String finalResult;
      try {
        ee.haSetOwner((ODatabaseDocument) db, String.valueOf(key), String.valueOf(value));
        finalResult = "OK";
      } catch (UnsupportedOperationException e) {
        finalResult = e.getMessage();
      }
      OResultInternal item = new OResultInternal();
      item.setProperty("operation", "ha set owner");
      item.setProperty("result", finalResult);
      result.add(item);
    } else if (operation.equalsIgnoreCase("role")) {
      String finalResult;
      try {
        ee.haSetRole((ODatabaseDocument) db, String.valueOf(key), String.valueOf(value));
        finalResult = "OK";
      } catch (UnsupportedOperationException e) {
        finalResult = e.getMessage();
      }
      OResultInternal item = new OResultInternal();
      item.setProperty("operation", "ha set role");
      item.setProperty("result", finalResult);
      result.add(item);
    }

    return result;
  }

  @Override
  public void toString(Map<Object, Object> params, StringBuilder builder) {
    builder.append("HA SET ");
    operation.toString(params, builder);
    builder.append(" ");
    key.toString(params, builder);
    builder.append(" = ");
    value.toString(params, builder);
  }
}
/* JavaCC - OriginalChecksum=21dffd729680550a5deb24492465084d (do not edit this line) */
