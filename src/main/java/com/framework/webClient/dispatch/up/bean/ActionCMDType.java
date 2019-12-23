/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.framework.webClient.dispatch.up.bean;


public enum ActionCMDType implements org.apache.thrift.TEnum {
  FULLGPS_INSERT(1),
  ARRIVESTATION_INSERT(2),
  LEAVESTATION_INSERT(3),
  UPMSG_INSERT(4),
  READCARD_INSERT(5),
  WARNINFO_INSERT(6),
  LINKGPRS_INSERT(7),
  BUSLOGIN_INSERT(8),
  BUSLOGIN_UPDATE(9),
  CLKLSC_INSERT(10),
  UPDATE_INSERT(11),
  UPDATEYD_INSERT(12),
  LINE_BASE(13),
  STATION_BASE(14),
  ROUT_BASE(15),
  YJD_INSERT(16),
  CAN_INSERT(17),
  CLKLNEWSC_INSERT(18);

  private final int value;

  private ActionCMDType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static ActionCMDType findByValue(int value) { 
    switch (value) {
      case 1:
        return FULLGPS_INSERT;
      case 2:
        return ARRIVESTATION_INSERT;
      case 3:
        return LEAVESTATION_INSERT;
      case 4:
        return UPMSG_INSERT;
      case 5:
        return READCARD_INSERT;
      case 6:
        return WARNINFO_INSERT;
      case 7:
        return LINKGPRS_INSERT;
      case 8:
        return BUSLOGIN_INSERT;
      case 9:
        return BUSLOGIN_UPDATE;
      case 10:
        return CLKLSC_INSERT;
      case 11:
        return UPDATE_INSERT;
      case 12:
        return UPDATEYD_INSERT;
      case 13:
        return LINE_BASE;
      case 14:
        return STATION_BASE;
      case 15:
        return ROUT_BASE;
      case 16:
        return YJD_INSERT;
      case 17:
        return CAN_INSERT;
      case 18:
        return CLKLNEWSC_INSERT;
      default:
        return null;
    }
  }
}
