package com.orientechnologies.orient.core.storage.impl.local.paginated.wal.component.sbtree.page.sbtreebucket;

import com.orientechnologies.common.serialization.types.OLongSerializer;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OLogSequenceNumber;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OPageOperation;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.WALRecordTypes;

import java.nio.ByteBuffer;

public class OSBTreeBucketSetLeftSiblingOperation extends OPageOperation {
  private long leftSibling;

  public OSBTreeBucketSetLeftSiblingOperation() {
  }

  public OSBTreeBucketSetLeftSiblingOperation(OLogSequenceNumber pageLSN, long fileId, long pageIndex, long leftSibling) {
    super(pageLSN, fileId, pageIndex);
    this.leftSibling = leftSibling;
  }

  @Override
  public byte getId() {
    return WALRecordTypes.SBTREE_BUCKET_SET_LEFT_SIBLING_OPERATION;
  }

  @Override
  public int serializedSize() {
    return super.serializedSize() + OLongSerializer.LONG_SIZE;
  }

  @Override
  public int toStream(byte[] content, int offset) {
    offset = super.toStream(content, offset);

    OLongSerializer.INSTANCE.serializeNative(leftSibling, content, offset);
    offset += OLongSerializer.LONG_SIZE;

    return offset;
  }

  @Override
  public void toStream(ByteBuffer buffer) {
    super.toStream(buffer);

    buffer.putLong(leftSibling);
  }

  @Override
  public int fromStream(byte[] content, int offset) {
    offset = super.fromStream(content, offset);

    leftSibling = OLongSerializer.INSTANCE.deserializeNative(content, offset);
    offset += OLongSerializer.LONG_SIZE;

    return offset;
  }

  public long getLeftSibling() {
    return leftSibling;
  }
}