package com.kakao.hotire.schedulerconversion.config;

public enum Mode {
  INCLUDE, EXCLUDE;

  public static Mode lookup(String mode) {
    try {
      return Mode.valueOf(mode.toUpperCase());
    } catch (IllegalArgumentException e) {
      return Mode.INCLUDE;
    }
  }
}