package com.dicarlo;

public interface ErrorCodes {
   static final String DATABASE_ERROR = "E001";
   static final String DUPLICATE_SERIAL_ERROR = "E002";
   static final String EMPTY_LINE_ERROR = "E003";
   static final String SERIAL_NOTFOUND1_ERROR = "E004";
   static final String SERIAL_NOTFOUND2_ERROR = "E005";
   static final String SID_NOTMATCH_ERROR = "E006";
   static final String FILES_LENGTH_NOTMATCH_ERROR = "E007";
   static final String FILES_LINE_NOTMATCH_ERROR = "E008";
   static final String FILES_LOADING_ERROR = "E009";

   
   static final String UNKNOWN_ERROR = "E999";

}
