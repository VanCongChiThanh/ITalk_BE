package com.chithanh.italk.common.constant;

import java.util.UUID;

public final class CommonConstant {

  public static final String SUCCESS = "success";
  public static final String FAILURE = "failure";
  public static final UUID UNKNOWN = UUID.fromString("00000000-0000-0000-0000-000000000000");
  public static final String ROLE_PREFIX = "ROLE_";
  public static final String RULE_PASSWORD =
      "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,32})"; // (?=.*[@#$%])
  public static final String RULE_ROLE = "ADMIN|USER";
  public static final String RULE_EMAIL =
      "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
          + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
  public static final UUID SYSTEM_ID = new UUID(0, 0);

  public static final String NOTIFICATION_EXCHANGE_NAME_PREFIX = "notifications/";
  public static final String ADMIN_EXCHANGE_NAME_PREFIX = "admin/notifications/";

  public static final String BASE_PACKAGE_ENDPOINT = "com.chithanh.italk.web";

  public static final String REMINDER_TYPE="NO_REPEAT|DAILY|WEEKLY|MONTHLY|YEARLY";

  private CommonConstant() {}
}