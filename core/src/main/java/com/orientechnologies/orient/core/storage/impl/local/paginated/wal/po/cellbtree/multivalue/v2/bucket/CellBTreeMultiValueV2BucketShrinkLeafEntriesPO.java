package com.orientechnologies.orient.core.storage.impl.local.paginated.wal.po.cellbtree.multivalue.v2.bucket;

import com.orientechnologies.common.serialization.types.*;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.serialization.serializer.binary.OBinarySerializerFactory;
import com.orientechnologies.orient.core.storage.cache.OCacheEntry;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.WALRecordTypes;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.po.PageOperationRecord;
import com.orientechnologies.orient.core.storage.index.sbtree.multivalue.v2.CellBTreeMultiValueV2Bucket;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class CellBTreeMultiValueV2BucketShrinkLeafEntriesPO extends PageOperationRecord {
  private int                                         newSize;
  private List<CellBTreeMultiValueV2Bucket.LeafEntry> removedEntries;
  private OBinarySerializer                           keySerializer;
  private boolean                                     isEncrypted;

  public CellBTreeMultiValueV2BucketShrinkLeafEntriesPO() {
  }

  public CellBTreeMultiValueV2BucketShrinkLeafEntriesPO(int newSize, List<CellBTreeMultiValueV2Bucket.LeafEntry> removedEntries,
      OBinarySerializer keySerializer, boolean isEncrypted) {
    this.newSize = newSize;
    this.removedEntries = removedEntries;
    this.keySerializer = keySerializer;
    this.isEncrypted = isEncrypted;
  }

  public int getNewSize() {
    return newSize;
  }

  public List<CellBTreeMultiValueV2Bucket.LeafEntry> getRemovedEntries() {
    return removedEntries;
  }

  public OBinarySerializer getKeySerializer() {
    return keySerializer;
  }

  public boolean isEncrypted() {
    return isEncrypted;
  }

  @Override
  public void redo(OCacheEntry cacheEntry) {
    final CellBTreeMultiValueV2Bucket bucket = new CellBTreeMultiValueV2Bucket(cacheEntry);
    //noinspection unchecked
    bucket.shrink(newSize, keySerializer, isEncrypted);
  }

  @Override
  public void undo(OCacheEntry cacheEntry) {
    final CellBTreeMultiValueV2Bucket bucket = new CellBTreeMultiValueV2Bucket(cacheEntry);
    //noinspection unchecked
    bucket.addAll(removedEntries, keySerializer, isEncrypted);
  }

  @Override
  public int getId() {
    return WALRecordTypes.CELL_BTREE_BUCKET_MULTI_VALUE_V2_SHRINK_LEAF_ENTRIES_PO;
  }

  @Override
  public int serializedSize() {
    int size = OIntegerSerializer.INT_SIZE;

    for (final CellBTreeMultiValueV2Bucket.LeafEntry leafEntry : removedEntries) {
      size += OLongSerializer.LONG_SIZE + 3 * OIntegerSerializer.INT_SIZE;
      size += leafEntry.key.length;
      size += leafEntry.values.size() * (OLongSerializer.LONG_SIZE + OShortSerializer.SHORT_SIZE);
    }

    size += 2 * OByteSerializer.BYTE_SIZE + OIntegerSerializer.INT_SIZE;

    return super.serializedSize() + size;
  }

  @Override
  protected void serializeToByteBuffer(ByteBuffer buffer) {
    super.serializeToByteBuffer(buffer);

    buffer.putInt(removedEntries.size());
    for (final CellBTreeMultiValueV2Bucket.LeafEntry leafEntry : removedEntries) {
      buffer.putInt(leafEntry.entriesCount);
      buffer.putLong(leafEntry.mId);

      buffer.putInt(leafEntry.values.size());
      for (final ORID rid : leafEntry.values) {
        buffer.putShort((short) rid.getClusterId());
        buffer.putLong(rid.getClusterPosition());
      }

      buffer.putInt(leafEntry.key.length);
      buffer.put(leafEntry.key);
    }

    buffer.put(keySerializer.getId());
    buffer.put(isEncrypted ? (byte) 1 : 0);
    buffer.putInt(newSize);
  }

  @Override
  protected void deserializeFromByteBuffer(ByteBuffer buffer) {
    super.deserializeFromByteBuffer(buffer);

    final int entriesSize = buffer.getInt();
    removedEntries = new ArrayList<>(entriesSize);
    for (int i = 0; i < entriesSize; i++) {
      final int entriesCount = buffer.getInt();
      final long mId = buffer.getLong();

      final int valuesSize = buffer.getInt();
      final List<ORID> values = new ArrayList<>(valuesSize);

      for (int n = 0; n < valuesSize; n++) {
        final int clusterId = buffer.getShort();
        final long clusterPosition = buffer.getLong();

        values.add(new ORecordId(clusterId, clusterPosition));
      }

      final int keyLen = buffer.getInt();
      final byte[] key = new byte[keyLen];
      buffer.get(key);

      removedEntries.add(new CellBTreeMultiValueV2Bucket.LeafEntry(key, mId, values, entriesCount));
    }

    keySerializer = OBinarySerializerFactory.getInstance().getObjectSerializer(buffer.get());
    isEncrypted = buffer.get() > 0;
    newSize = buffer.getInt();
  }
}
