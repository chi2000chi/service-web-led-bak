/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.framework.webClient.dispatch.up.bean;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-04-24")
public class SJYDSJDTO implements org.apache.thrift.TBase<SJYDSJDTO, SJYDSJDTO._Fields>, java.io.Serializable, Cloneable, Comparable<SJYDSJDTO> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SJYDSJDTO");

  private static final org.apache.thrift.protocol.TField ACTION_CMDTYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("actionCMDType", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField YDLSH_FIELD_DESC = new org.apache.thrift.protocol.TField("ydlsh", org.apache.thrift.protocol.TType.I16, (short)2);
  private static final org.apache.thrift.protocol.TField YDMLH_FIELD_DESC = new org.apache.thrift.protocol.TField("ydmlh", org.apache.thrift.protocol.TType.I16, (short)3);
  private static final org.apache.thrift.protocol.TField YDJG_FIELD_DESC = new org.apache.thrift.protocol.TField("ydjg", org.apache.thrift.protocol.TType.BYTE, (short)4);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new SJYDSJDTOStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new SJYDSJDTOTupleSchemeFactory();

  /**
   * 
   * @see ActionCMDType
   */
  public ActionCMDType actionCMDType; // required
  public short ydlsh; // required
  public short ydmlh; // required
  public byte ydjg; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see ActionCMDType
     */
    ACTION_CMDTYPE((short)1, "actionCMDType"),
    YDLSH((short)2, "ydlsh"),
    YDMLH((short)3, "ydmlh"),
    YDJG((short)4, "ydjg");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ACTION_CMDTYPE
          return ACTION_CMDTYPE;
        case 2: // YDLSH
          return YDLSH;
        case 3: // YDMLH
          return YDMLH;
        case 4: // YDJG
          return YDJG;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __YDLSH_ISSET_ID = 0;
  private static final int __YDMLH_ISSET_ID = 1;
  private static final int __YDJG_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ACTION_CMDTYPE, new org.apache.thrift.meta_data.FieldMetaData("actionCMDType", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, ActionCMDType.class)));
    tmpMap.put(_Fields.YDLSH, new org.apache.thrift.meta_data.FieldMetaData("ydlsh", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    tmpMap.put(_Fields.YDMLH, new org.apache.thrift.meta_data.FieldMetaData("ydmlh", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    tmpMap.put(_Fields.YDJG, new org.apache.thrift.meta_data.FieldMetaData("ydjg", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SJYDSJDTO.class, metaDataMap);
  }

  public SJYDSJDTO() {
  }

  public SJYDSJDTO(
    ActionCMDType actionCMDType,
    short ydlsh,
    short ydmlh,
    byte ydjg)
  {
    this();
    this.actionCMDType = actionCMDType;
    this.ydlsh = ydlsh;
    setYdlshIsSet(true);
    this.ydmlh = ydmlh;
    setYdmlhIsSet(true);
    this.ydjg = ydjg;
    setYdjgIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SJYDSJDTO(SJYDSJDTO other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetActionCMDType()) {
      this.actionCMDType = other.actionCMDType;
    }
    this.ydlsh = other.ydlsh;
    this.ydmlh = other.ydmlh;
    this.ydjg = other.ydjg;
  }

  public SJYDSJDTO deepCopy() {
    return new SJYDSJDTO(this);
  }

  @Override
  public void clear() {
    this.actionCMDType = null;
    setYdlshIsSet(false);
    this.ydlsh = 0;
    setYdmlhIsSet(false);
    this.ydmlh = 0;
    setYdjgIsSet(false);
    this.ydjg = 0;
  }

  /**
   * 
   * @see ActionCMDType
   */
  public ActionCMDType getActionCMDType() {
    return this.actionCMDType;
  }

  /**
   * 
   * @see ActionCMDType
   */
  public SJYDSJDTO setActionCMDType(ActionCMDType actionCMDType) {
    this.actionCMDType = actionCMDType;
    return this;
  }

  public void unsetActionCMDType() {
    this.actionCMDType = null;
  }

  /** Returns true if field actionCMDType is set (has been assigned a value) and false otherwise */
  public boolean isSetActionCMDType() {
    return this.actionCMDType != null;
  }

  public void setActionCMDTypeIsSet(boolean value) {
    if (!value) {
      this.actionCMDType = null;
    }
  }

  public short getYdlsh() {
    return this.ydlsh;
  }

  public SJYDSJDTO setYdlsh(short ydlsh) {
    this.ydlsh = ydlsh;
    setYdlshIsSet(true);
    return this;
  }

  public void unsetYdlsh() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __YDLSH_ISSET_ID);
  }

  /** Returns true if field ydlsh is set (has been assigned a value) and false otherwise */
  public boolean isSetYdlsh() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __YDLSH_ISSET_ID);
  }

  public void setYdlshIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __YDLSH_ISSET_ID, value);
  }

  public short getYdmlh() {
    return this.ydmlh;
  }

  public SJYDSJDTO setYdmlh(short ydmlh) {
    this.ydmlh = ydmlh;
    setYdmlhIsSet(true);
    return this;
  }

  public void unsetYdmlh() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __YDMLH_ISSET_ID);
  }

  /** Returns true if field ydmlh is set (has been assigned a value) and false otherwise */
  public boolean isSetYdmlh() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __YDMLH_ISSET_ID);
  }

  public void setYdmlhIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __YDMLH_ISSET_ID, value);
  }

  public byte getYdjg() {
    return this.ydjg;
  }

  public SJYDSJDTO setYdjg(byte ydjg) {
    this.ydjg = ydjg;
    setYdjgIsSet(true);
    return this;
  }

  public void unsetYdjg() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __YDJG_ISSET_ID);
  }

  /** Returns true if field ydjg is set (has been assigned a value) and false otherwise */
  public boolean isSetYdjg() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __YDJG_ISSET_ID);
  }

  public void setYdjgIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __YDJG_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case ACTION_CMDTYPE:
      if (value == null) {
        unsetActionCMDType();
      } else {
        setActionCMDType((ActionCMDType)value);
      }
      break;

    case YDLSH:
      if (value == null) {
        unsetYdlsh();
      } else {
        setYdlsh((java.lang.Short)value);
      }
      break;

    case YDMLH:
      if (value == null) {
        unsetYdmlh();
      } else {
        setYdmlh((java.lang.Short)value);
      }
      break;

    case YDJG:
      if (value == null) {
        unsetYdjg();
      } else {
        setYdjg((java.lang.Byte)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case ACTION_CMDTYPE:
      return getActionCMDType();

    case YDLSH:
      return getYdlsh();

    case YDMLH:
      return getYdmlh();

    case YDJG:
      return getYdjg();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case ACTION_CMDTYPE:
      return isSetActionCMDType();
    case YDLSH:
      return isSetYdlsh();
    case YDMLH:
      return isSetYdmlh();
    case YDJG:
      return isSetYdjg();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof SJYDSJDTO)
      return this.equals((SJYDSJDTO)that);
    return false;
  }

  public boolean equals(SJYDSJDTO that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_actionCMDType = true && this.isSetActionCMDType();
    boolean that_present_actionCMDType = true && that.isSetActionCMDType();
    if (this_present_actionCMDType || that_present_actionCMDType) {
      if (!(this_present_actionCMDType && that_present_actionCMDType))
        return false;
      if (!this.actionCMDType.equals(that.actionCMDType))
        return false;
    }

    boolean this_present_ydlsh = true;
    boolean that_present_ydlsh = true;
    if (this_present_ydlsh || that_present_ydlsh) {
      if (!(this_present_ydlsh && that_present_ydlsh))
        return false;
      if (this.ydlsh != that.ydlsh)
        return false;
    }

    boolean this_present_ydmlh = true;
    boolean that_present_ydmlh = true;
    if (this_present_ydmlh || that_present_ydmlh) {
      if (!(this_present_ydmlh && that_present_ydmlh))
        return false;
      if (this.ydmlh != that.ydmlh)
        return false;
    }

    boolean this_present_ydjg = true;
    boolean that_present_ydjg = true;
    if (this_present_ydjg || that_present_ydjg) {
      if (!(this_present_ydjg && that_present_ydjg))
        return false;
      if (this.ydjg != that.ydjg)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetActionCMDType()) ? 131071 : 524287);
    if (isSetActionCMDType())
      hashCode = hashCode * 8191 + actionCMDType.getValue();

    hashCode = hashCode * 8191 + ydlsh;

    hashCode = hashCode * 8191 + ydmlh;

    hashCode = hashCode * 8191 + (int) (ydjg);

    return hashCode;
  }

  @Override
  public int compareTo(SJYDSJDTO other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetActionCMDType()).compareTo(other.isSetActionCMDType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetActionCMDType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.actionCMDType, other.actionCMDType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetYdlsh()).compareTo(other.isSetYdlsh());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetYdlsh()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ydlsh, other.ydlsh);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetYdmlh()).compareTo(other.isSetYdmlh());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetYdmlh()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ydmlh, other.ydmlh);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetYdjg()).compareTo(other.isSetYdjg());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetYdjg()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ydjg, other.ydjg);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("SJYDSJDTO(");
    boolean first = true;

    sb.append("actionCMDType:");
    if (this.actionCMDType == null) {
      sb.append("null");
    } else {
      sb.append(this.actionCMDType);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("ydlsh:");
    sb.append(this.ydlsh);
    first = false;
    if (!first) sb.append(", ");
    sb.append("ydmlh:");
    sb.append(this.ydmlh);
    first = false;
    if (!first) sb.append(", ");
    sb.append("ydjg:");
    sb.append(this.ydjg);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class SJYDSJDTOStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SJYDSJDTOStandardScheme getScheme() {
      return new SJYDSJDTOStandardScheme();
    }
  }

  private static class SJYDSJDTOStandardScheme extends org.apache.thrift.scheme.StandardScheme<SJYDSJDTO> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SJYDSJDTO struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ACTION_CMDTYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.actionCMDType = ActionCMDType.findByValue(iprot.readI32());
              struct.setActionCMDTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // YDLSH
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.ydlsh = iprot.readI16();
              struct.setYdlshIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // YDMLH
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.ydmlh = iprot.readI16();
              struct.setYdmlhIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // YDJG
            if (schemeField.type == org.apache.thrift.protocol.TType.BYTE) {
              struct.ydjg = iprot.readByte();
              struct.setYdjgIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, SJYDSJDTO struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.actionCMDType != null) {
        oprot.writeFieldBegin(ACTION_CMDTYPE_FIELD_DESC);
        oprot.writeI32(struct.actionCMDType.getValue());
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(YDLSH_FIELD_DESC);
      oprot.writeI16(struct.ydlsh);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(YDMLH_FIELD_DESC);
      oprot.writeI16(struct.ydmlh);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(YDJG_FIELD_DESC);
      oprot.writeByte(struct.ydjg);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SJYDSJDTOTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SJYDSJDTOTupleScheme getScheme() {
      return new SJYDSJDTOTupleScheme();
    }
  }

  private static class SJYDSJDTOTupleScheme extends org.apache.thrift.scheme.TupleScheme<SJYDSJDTO> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SJYDSJDTO struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetActionCMDType()) {
        optionals.set(0);
      }
      if (struct.isSetYdlsh()) {
        optionals.set(1);
      }
      if (struct.isSetYdmlh()) {
        optionals.set(2);
      }
      if (struct.isSetYdjg()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetActionCMDType()) {
        oprot.writeI32(struct.actionCMDType.getValue());
      }
      if (struct.isSetYdlsh()) {
        oprot.writeI16(struct.ydlsh);
      }
      if (struct.isSetYdmlh()) {
        oprot.writeI16(struct.ydmlh);
      }
      if (struct.isSetYdjg()) {
        oprot.writeByte(struct.ydjg);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SJYDSJDTO struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.actionCMDType = ActionCMDType.findByValue(iprot.readI32());
        struct.setActionCMDTypeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.ydlsh = iprot.readI16();
        struct.setYdlshIsSet(true);
      }
      if (incoming.get(2)) {
        struct.ydmlh = iprot.readI16();
        struct.setYdmlhIsSet(true);
      }
      if (incoming.get(3)) {
        struct.ydjg = iprot.readByte();
        struct.setYdjgIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
