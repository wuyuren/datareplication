CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_AMORTIZED_FEES
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`AMORTIZED_FEE_ID`   ,
`ACCTREFNO`   ,
`TRANS_NO`   ,
`TRANS_DESCRIPTION`   ,
`TRANS_NOTES`   ,
`AMORT_TYPE`   ,
`AMORT_PERIOD`   ,
`AMORT_PREM_DISC_FLAG`   ,
`AMORT_NUMPERIODS`   ,
`AMORT_START_DATE`   ,
`AMORT_START_DATE_DAYVALUE`   ,
`AMORT_TRANSACTION_CODE`   ,
`ORIGINAL_BALANCE`   ,
`AMORTIZED_BALANCE`   ,
`UNAMORTIZED_BALANCE`   ,
`AMORT_PERIODIC_AMOUNT`   ,
`UNAMORTIZED_BALANCE_AVERAGE`   ,
`UNAMORTIZED_BALANCE_TOTAL`   ,
`EFFECTIVE_INTEREST_RATE`   ,
`USE_LOAN_INFO_FLAG`   ,
`UNAMORTIZED_BALANCE_DAYS`   ,
`LOAN_PRINCIPAL_BALANCE`   ,
`LOAN_PERIOD`   ,
`LOAN_TERM`   ,
`LOAN_TERM_CHAR`   ,
`LOAN_PAYMENT_AMOUNT`   ,
`LOAN_STARTING_DATE`   ,
`TRANS_LASTDATE`   ,
`TRANS_NEXTDATE`   ,
`AMORT_THROUGH_DATE`   ,
`AMORT_COUNTER`   ,
`AMORT_PRINCIPAL_AMOUNT`   ,
`INTEREST_OFFSET`   ,
`EXPECTED_LTD_INTEREST`   ,
`AMORT_EFFECTIVE_DATE`   ,
`OPTION_FLAGS`   ,
`PRINCIPAL_OFFSET`   ,
`REFUND_TYPE`   ,
`REFUND_TRANSACTION_CODE`   ,
`REFUND_MIN_AMOUNT`   ,
`REFUND_CALC_PERCENTAGE`   ,
`REBATE_CUTOFF_DAYS`   
FROM loan_mgmt_rt.rse_DBO_AMORTIZED_FEES

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_AMORTIZED_FEES_CUSTOM
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`AMORTIZED_FEE_ID`   ,
`PERIOD_NO`   ,
`PERIOD_DATE`   ,
`PERIOD_AMOUNT`   ,
`PERIOD_RECOGNIZED`   
FROM loan_mgmt_rt.rse_DBO_AMORTIZED_FEES_CUSTOM

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_BATCH_TRANSACTION
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`BATCH_NO`   ,
`BATCH_REFERENCE`   ,
`BATCH_TYPE`   ,
`BATCH_DATE`   ,
`DEPOSIT_DATE`   ,
`BATCH_COUNT`   ,
`BATCH_AMOUNT`   ,
`USERDEF01`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   
FROM loan_mgmt_rt.rse_DBO_BATCH_TRANSACTION

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_BATCH_TRANSACTION_ACH
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`BATCH_NO`   ,
`ACH_HEADER_ID`   ,
`ACH_FILE_STATUS`   ,
`FILE_CREATION_DATE`   ,
`FILE_ID_MODIFIER`   ,
`FILE_BINARY`   ,
`FILE_CREATION_UID`   
FROM loan_mgmt_rt.rse_DBO_BATCH_TRANSACTION_ACH

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_BATCH_TRANSACTION_DETAIL
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`BATCH_NO`   ,
`ITEM_NO`   ,
`ACCTREFNO`   ,
`ACH_TRACE_NUMBER`   ,
`TRANSACTION_DATE`   ,
`EFFECTIVE_DATE`   ,
`GL_DATE`   ,
`TRANSACTION_TYPE`   ,
`PAYMENT_METHOD_NO`   ,
`TRANSACTION_AMOUNT`   ,
`PAYMENT_METHOD_REFERENCE`   ,
`USER_REFERENCE`   ,
`STATUS`   ,
`ABA_NUMBER`   ,
`ACCOUNT_NUMBER`   ,
`LOANACCT_ACH_ROW_ID`   
FROM loan_mgmt_rt.rse_DBO_BATCH_TRANSACTION_DETAIL

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_CIF
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`CIFNO`   ,
`PORTFOLIO_CODE_ID`   ,
`CIFNUMBER`   ,
`SETUPDATE`   ,
`SHORTNAME`   ,
`LASTNAME1`   ,
`ADDRESS_TYPE`   ,
`COMPANY`   ,
`FIRSTNAME1`   ,
`SALUTATION1`   ,
`MIDDLENAME1`   ,
`LASTNAME2`   ,
`CITY`   ,
`SUFFIX1`   ,
`FIRSTNAME2`   ,
`STATE`   ,
`TITLE1`   ,
`MAIL_NAME1`   ,
`SALUTATION2`   ,
`MIDDLENAME2`   ,
`MAIL_NAME2`   ,
`STREET_ADDRESS1`   ,
`ZIP`   ,
`SUFFIX2`   ,
`COUNTY`   ,
`COUNTRY`   ,
`TITLE2`   ,
`STREET_ADDRESS2`   ,
`TIN`   ,
`TINTYPE`   ,
`ENTITY`   ,
`EMAIL`   ,
`OFFICER_NUMBER`   ,
`DOB`   ,
`EMAIL2`   ,
`DOB2`   ,
`WEB_ADDRESS`   ,
`TIN2`   ,
`OVERRIDE_MAILNAME`   ,
`DPC`   ,
`DPTYPE`   ,
`DPVDATESTAMP`   ,
`TIN_HASH`   ,
`TIN2_HASH`   ,
`FIPSCODE`   ,
`DPLATITUDE`   ,
`DPLONGITUDE`   ,
`BRANCH_CIFNO`   
FROM loan_mgmt_rt.rse_DBO_CIF

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_CIF_ADDRESSBOOK
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`CIFNO`   ,
`RELATIONSHIP_CODE_ID`   ,
`SETUPDATE`   ,
`ADDRESS_TYPE`   ,
`ADDRESS_DESC`   ,
`LASTNAME1`   ,
`COMPANY`   ,
`FIRSTNAME1`   ,
`SALUTATION1`   ,
`MIDDLENAME1`   ,
`LASTNAME2`   ,
`FIRSTNAME2`   ,
`MIDDLENAME2`   ,
`SUFFIX1`   ,
`STREET_ADDRESS1`   ,
`TITLE1`   ,
`MAIL_NAME1`   ,
`SALUTATION2`   ,
`STREET_ADDRESS2`   ,
`MAIL_NAME2`   ,
`CITY`   ,
`STATE`   ,
`SUFFIX2`   ,
`ZIP`   ,
`TITLE2`   ,
`COUNTY`   ,
`COUNTRY`   ,
`NOTES`   ,
`EMAIL`   ,
`ENTITY`   ,
`EMAIL2`   ,
`WEB_ADDRESS`   ,
`USERDEF01`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`USERDEF06`   ,
`USERDEF07`   ,
`USERDEF08`   ,
`USERDEF09`   ,
`USERDEF10`   ,
`USERDEF11`   ,
`USERDEF12`   ,
`USERDEF13`   ,
`USERDEF14`   ,
`USERDEF15`   ,
`USERDEF16`   ,
`DPC`   ,
`DPTYPE`   ,
`DPVDATESTAMP`   ,
`FIPSCODE`   ,
`DPLATITUDE`   ,
`DPLONGITUDE`   ,
`FORWARD_STATEMENT`   ,
`FORWARD_LATENOTICE`   
FROM loan_mgmt_rt.rse_DBO_CIF_ADDRESSBOOK

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_CIF_DEMOGRAPHICS
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`CIFNO`   ,
`USERDEF01`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`USERDEF06`   ,
`USERDEF07`   ,
`USERDEF08`   ,
`USERDEF09`   ,
`USERDEF10`   ,
`USERDEF11`   ,
`USERDEF12`   ,
`USERDEF13`   ,
`USERDEF14`   ,
`USERDEF15`   ,
`USERDEF16`   ,
`USERDEF17`   ,
`USERDEF18`   ,
`USERDEF19`   ,
`USERDEF20`   ,
`USERDEF21`   ,
`USERDEF22`   ,
`USERDEF23`   ,
`USERDEF24`   ,
`USERDEF25`   ,
`USERDEF26`   ,
`USERDEF27`   ,
`USERDEF28`   ,
`USERDEF29`   ,
`USERDEF30`   ,
`USERDEF31`   ,
`USERDEF32`   ,
`USERDEF33`   ,
`USERDEF34`   ,
`USERDEF35`   ,
`USERDEF36`   ,
`USERDEF37`   ,
`USERDEF38`   ,
`USERDEF39`   ,
`USERDEF40`   ,
`USERDEF41`   ,
`USERDEF42`   ,
`USERDEF43`   ,
`USERDEF44`   ,
`USERDEF45`   ,
`USERDEF46`   ,
`USERDEF47`   ,
`USERDEF48`   ,
`USERDEF49`   ,
`USERDEF50`   ,
`USERDEF51`   ,
`USERDEF52`   ,
`USERDEF53`   ,
`USERDEF54`   ,
`USERDEF55`   
FROM loan_mgmt_rt.rse_DBO_CIF_DEMOGRAPHICS

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_CIF_DETAIL
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`CIFNO`   ,
`USERDEF01`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`USERDEF06`   ,
`USERDEF07`   ,
`USERDEF08`   ,
`USERDEF09`   ,
`USERDEF10`   ,
`USERDEF11`   ,
`USERDEF12`   ,
`USERDEF13`   ,
`USERDEF14`   ,
`USERDEF15`   ,
`USERDEF16`   ,
`USERDEF17`   ,
`USERDEF18`   ,
`USERDEF19`   ,
`USERDEF20`   ,
`USERDEF21`   ,
`USERDEF22`   ,
`USERDEF23`   ,
`USERDEF24`   ,
`USERDEF25`   ,
`USERDEF26`   ,
`USERDEF27`   ,
`USERDEF28`   ,
`USERDEF29`   ,
`USERDEF30`   ,
`USERDEF31`   ,
`USERDEF32`   ,
`USERDEF33`   ,
`USERDEF34`   ,
`USERDEF35`   ,
`USERDEF36`   ,
`USERDEF37`   ,
`USERDEF38`   ,
`USERDEF39`   ,
`USERDEF40`   ,
`USERDEF41`   ,
`USERDEF42`   ,
`USERDEF43`   ,
`USERDEF44`   ,
`USERDEF45`   ,
`USERDEF46`   ,
`USERDEF47`   ,
`USERDEF48`   ,
`USERDEF49`   ,
`USERDEF50`   ,
`USERDEF51`   ,
`USERDEF52`   ,
`USERDEF53`   ,
`USERDEF54`   ,
`USERDEF55`   
FROM loan_mgmt_rt.rse_DBO_CIF_DETAIL

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_CIF_FINANCIALS
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`CIFNO`   ,
`USERDEF01`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`USERDEF06`   ,
`USERDEF07`   ,
`USERDEF08`   ,
`USERDEF09`   ,
`USERDEF10`   ,
`USERDEF11`   ,
`USERDEF12`   ,
`USERDEF13`   ,
`USERDEF14`   ,
`USERDEF15`   ,
`USERDEF16`   ,
`USERDEF17`   ,
`USERDEF18`   ,
`USERDEF19`   ,
`USERDEF20`   ,
`USERDEF21`   ,
`USERDEF22`   ,
`USERDEF23`   ,
`USERDEF24`   ,
`USERDEF25`   ,
`USERDEF26`   ,
`USERDEF27`   ,
`USERDEF28`   ,
`USERDEF29`   ,
`USERDEF30`   ,
`USERDEF31`   ,
`USERDEF32`   ,
`USERDEF33`   ,
`USERDEF34`   ,
`USERDEF35`   ,
`USERDEF36`   ,
`USERDEF37`   ,
`USERDEF38`   ,
`USERDEF39`   ,
`USERDEF40`   ,
`USERDEF41`   ,
`USERDEF42`   ,
`USERDEF43`   ,
`USERDEF44`   ,
`USERDEF45`   ,
`USERDEF46`   ,
`USERDEF47`   ,
`USERDEF48`   ,
`USERDEF49`   ,
`USERDEF50`   ,
`USERDEF51`   ,
`USERDEF52`   ,
`USERDEF53`   ,
`USERDEF54`   ,
`USERDEF55`   
FROM loan_mgmt_rt.rse_DBO_CIF_FINANCIALS

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_CIF_PHONE_NUMS
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`CIFNO`   ,
`SETUPDATE`   ,
`PHONE_DESCRIPTION`   ,
`COUNTRY_CODE`   ,
`PHONE_AREA_CODE`   ,
`PHONE_NUMBER`   ,
`PHONE_EXTENSION`   ,
`PRIMARY_FLAG`   ,
`DO_NOT_CALL_FLAG`   ,
`BAD_NUMBER_FLAG`   ,
`NO_DIALER_FLAG`   ,
`COMMENTS`   ,
`PHONE_RAW`   ,
`MOBILE_NUMBER_FLAG`   ,
`CONSENT_DATE`   ,
`SCRUB_DATE`   ,
`DO_NOT_TEXT_FLAG`   
FROM loan_mgmt_rt.rse_DBO_CIF_PHONE_NUMS

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ACCTREFNO`   ,
`LOAN_TYPE`   ,
`PORTFOLIO_CODE_ID`   ,
`LOAN_GROUP_NO`   ,
`MASTER_ACCTREFNO`   ,
`POOL_ACCTREFNO`   ,
`RESTRUCTURED_ACCTREFNO`   ,
`SHADOW_LOAN_EXISTS`   ,
`CIFNO`   ,
`DEALER_CIFNO`   ,
`NAME`   ,
`LOAN_CLASS1_NO`   ,
`LOAN_CLASS2_NO`   ,
`SHORTNAME`   ,
`RISK_RATING_NO`   ,
`LOAN_NUMBER`   ,
`TIN`   ,
`ENTITY`   ,
`INPUT_DATE`   ,
`INPUT_GL_DATE`   ,
`STATUS_CODE_NO`   ,
`LOAN_OFFICER_NO`   ,
`OPEN_DATE`   ,
`COLLECTION_OFFICER_NO`   ,
`CURR_DATE`   ,
`OPEN_MATURITY_DATE`   ,
`CURR_MATURITY_DATE`   ,
`PAYOFF_DATE`   ,
`CLOSED_DATE`   ,
`LAST_ACTIVITY_DATE`   ,
`PROCESSING_START_DATE`   ,
`INTEREST_PAID_THRU_DATE`   ,
`PRINCIPAL_PAID_THRU_DATE`   ,
`INTEREST_ACCRUED_THRU_DATE`   ,
`ORIGINAL_NOTE_AMOUNT`   ,
`CURRENT_NOTE_AMOUNT`   ,
`CURRENT_PRINCIPAL_BALANCE`   ,
`CURRENT_INTEREST_BALANCE`   ,
`CURRENT_FEES_BALANCE`   ,
`CURRENT_DEF_INTEREST_BALANCE`   ,
`CURRENT_LATE_CHARGE_BALANCE`   ,
`CURRENT_PAYOFF_BALANCE`   ,
`CURRENT_UDF1_BALANCE`   ,
`CURRENT_PERDIEM`   ,
`CURRENT_SUSPENSE`   ,
`CURRENT_UDF2_BALANCE`   ,
`CURRENT_INTEREST_RATE`   ,
`CURRENT_UDF3_BALANCE`   ,
`TOTAL_PAST_DUE_BALANCE`   ,
`CURRENT_UDF4_BALANCE`   ,
`TOTAL_CURRENT_DUE_BALANCE`   ,
`CURRENT_UDF5_BALANCE`   ,
`CURRENT_UDF6_BALANCE`   ,
`STARTING_INTEREST_RATE`   ,
`TIMES_RENEWED`   ,
`CURRENT_DEF_PERDIEM`   ,
`CURRENT_IMPOUND_BALANCE`   ,
`CURRENT_PENDING`   ,
`CURRENT_OL1_BALANCE`   ,
`CURRENT_UDF7_BALANCE`   ,
`CURRENT_OL2_BALANCE`   ,
`STARTING_YEAR_INTEREST_RATE`   ,
`CURRENT_OL3_BALANCE`   ,
`CURRENT_UDF8_BALANCE`   ,
`LAST_RATE_CHANGE_DATE`   ,
`CURRENT_UDF9_BALANCE`   ,
`TIMES_EXTENDED`   ,
`CURRENT_UDF10_BALANCE`   ,
`NEXT_RATE_CHANGE_DATE`   ,
`NEXT_INTEREST_STEP_DATE`   ,
`DEFAULT_INTEREST_INDICATOR`   ,
`ADDONINT_TOTAL`   ,
`ADDONINT_REMAINING`   ,
`STARTING_YEAR_DEF_INT_RATE`   ,
`STARTING_DEF_INTEREST_RATE`   ,
`NEXT_RATE_CHANGE_NOTICE_DATE`   ,
`COMPOUND_INTEREST_BALANCE`   ,
`CURRENT_DEF_INTEREST_RATE`   ,
`NEXT_ACCRUAL_CUTOFF`   ,
`LAST_DEF_RATE_CHANGE_DATE`   ,
`NEXT_DEF_RATE_CHANGE_DATE`   ,
`NEXT_BILLING_DATE`   ,
`NEXT_RECURRING_TRANS_DATE`   ,
`NEXT_DEF_INTEREST_STEP_DATE`   ,
`NEXT_STATEMENT1_DATE`   ,
`NEXT_DEF_RATE_CHANGE_NOTICE`   ,
`DAYS_IN_CURRENT_PERIOD`   ,
`DEFAULT_DEF_INTEREST_INDICATOR`   ,
`NEXT_STATEMENT2_DATE`   ,
`NEXT_AMORT_FEES_DATE`   ,
`AMORTIZED_FEES_EIM_FLAG`   ,
`DAYS_PAST_DUE`   ,
`NEXT_PENDING_DATE`   ,
`COMPOUND_DEF_INTEREST_BALANCE`   ,
`COMPOUND_INTEREST_INDICATOR`   ,
`COMPOUND_DEF_INT_INDICATOR`   ,
`CURRENT_SERVICING_BALANCE`   ,
`CURRENT_RESERVE_BALANCE`   ,
`SAC_EXPIRATION_DATE`   ,
`SAC_INELIGIBLE_DATE`   ,
`CURRENCYID`   ,
`INTRO_INTEREST_EXPIRATION`   ,
`INTRO_DEF_INTEREST_EXPIRATION`   ,
`TIN_HASH`   ,
`CURRENT_ADB_INTEREST_BALANCE`   ,
`BRANCH_CIFNO`   ,
`STATEMENT1_BILLING_END_DATE`   ,
`STATEMENT2_BILLING_END_DATE`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_ACH
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`ACCTREFNO`   ,
`ACH_COMPANY_ID`   ,
`ACH_OPTIONS_ID`   ,
`ITEM_SEQUENCE`   ,
`STATUS`   ,
`INPUT_DATE`   ,
`DESCRIPTION`   ,
`DRCR_FLAG`   ,
`PAYMENT_TYPE`   ,
`ABA_NUMBER`   ,
`ACCOUNT_NUMBER`   ,
`ACCOUNT_TYPE`   ,
`BILLING_TYPE`   ,
`CHECK_NUMBER`   ,
`BILLING_PERIOD`   ,
`BILLING_NUMPERIODS`   ,
`EFT_TYPE`   ,
`BILLING_DAYVALUE`   ,
`BILLING_DAYVALUE_FLAG`   ,
`BILLING_START_DATE`   ,
`BILLING_NEXT_DATE`   ,
`BILLING_EXPIRE_DATE`   ,
`MAX_NUM_OF_DRAWS`   ,
`GRACE_PERIOD`   ,
`NUM_OF_DRAWS`   ,
`AMOUNT_TYPE`   ,
`LAST_DRAW_DATE`   ,
`MIN_AMOUNT`   ,
`MAX_AMOUNT`   ,
`AMOUNT_PERCENTAGE`   ,
`AMOUNT_FIXED`   ,
`NSF_COUNTER`   ,
`MAX_AMOUNT_OF_DRAWS`   ,
`SEND_PRENOTIFICATION_FLAG`   ,
`NOTES`   ,
`OPTION_FLAGS`   ,
`ADVANCE_TRANSACTION_CODE`   ,
`TOKEN`   ,
`LASTFOURDIGITS`   ,
`EXPIRATIONDATE`   ,
`ORIGREPRESENTMENTTRACENUMBER`   ,
`ORIGREPRESENTMENTTRACEDATA`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_ACH

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_COLLECTIONS
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`ACCTREFNO`   ,
`CATEGORY_ID`   ,
`ACTION_CODE_NO`   ,
`COLLECTION_CODE_NO`   ,
`RESULT_CODE_NO`   ,
`CREATED`   ,
`CREATED_BY`   ,
`USERDEF01`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`USERDEF06`   ,
`USERDEF07`   ,
`USERDEF08`   ,
`USERDEF09`   ,
`USERDEF10`   ,
`COMMENTS`   ,
`USERDEF11`   ,
`USERDEF12`   ,
`USERDEF13`   ,
`USERDEF14`   ,
`USERDEF15`   ,
`USERDEF16`   ,
`USERDEF17`   ,
`USERDEF18`   ,
`USERDEF19`   ,
`USERDEF20`   ,
`CONTACT_NAME`   ,
`CONTACT_NUMBER`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_COLLECTIONS

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_DETAIL
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`USERDEF01`   ,
`ACCTREFNO`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`USERDEF06`   ,
`USERDEF07`   ,
`USERDEF08`   ,
`USERDEF09`   ,
`USERDEF10`   ,
`USERDEF11`   ,
`USERDEF12`   ,
`USERDEF13`   ,
`USERDEF14`   ,
`USERDEF15`   ,
`USERDEF16`   ,
`USERDEF17`   ,
`USERDEF18`   ,
`USERDEF19`   ,
`USERDEF20`   ,
`USERDEF21`   ,
`USERDEF22`   ,
`USERDEF23`   ,
`USERDEF24`   ,
`USERDEF25`   ,
`USERDEF26`   ,
`USERDEF27`   ,
`USERDEF28`   ,
`USERDEF29`   ,
`USERDEF30`   ,
`USERDEF31`   ,
`USERDEF32`   ,
`USERDEF33`   ,
`USERDEF34`   ,
`USERDEF35`   ,
`USERDEF36`   ,
`USERDEF37`   ,
`USERDEF38`   ,
`USERDEF39`   ,
`USERDEF40`   ,
`USERDEF41`   ,
`USERDEF42`   ,
`USERDEF43`   ,
`USERDEF44`   ,
`USERDEF45`   ,
`USERDEF46`   ,
`USERDEF47`   ,
`USERDEF48`   ,
`USERDEF49`   ,
`USERDEF50`   ,
`USERDEF51`   ,
`USERDEF52`   ,
`USERDEF53`   ,
`USERDEF54`   ,
`USERDEF55`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_DETAIL

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_DETAIL_2
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`USERDEF01`   ,
`ACCTREFNO`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`USERDEF06`   ,
`USERDEF07`   ,
`USERDEF08`   ,
`USERDEF09`   ,
`USERDEF10`   ,
`USERDEF11`   ,
`USERDEF12`   ,
`USERDEF13`   ,
`USERDEF14`   ,
`USERDEF15`   ,
`USERDEF16`   ,
`USERDEF17`   ,
`USERDEF18`   ,
`USERDEF19`   ,
`USERDEF20`   ,
`USERDEF21`   ,
`USERDEF22`   ,
`USERDEF23`   ,
`USERDEF24`   ,
`USERDEF25`   ,
`USERDEF26`   ,
`USERDEF27`   ,
`USERDEF28`   ,
`USERDEF29`   ,
`USERDEF30`   ,
`USERDEF31`   ,
`USERDEF32`   ,
`USERDEF33`   ,
`USERDEF34`   ,
`USERDEF35`   ,
`USERDEF36`   ,
`USERDEF37`   ,
`USERDEF38`   ,
`USERDEF39`   ,
`USERDEF40`   ,
`USERDEF41`   ,
`USERDEF42`   ,
`USERDEF43`   ,
`USERDEF44`   ,
`USERDEF45`   ,
`USERDEF46`   ,
`USERDEF47`   ,
`USERDEF48`   ,
`USERDEF49`   ,
`USERDEF50`   ,
`USERDEF51`   ,
`USERDEF52`   ,
`USERDEF53`   ,
`USERDEF54`   ,
`USERDEF55`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_DETAIL_2

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_MOD_HISTORY
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ACCTREFNO`   ,
`ROW_ID`   ,
`ITEM_CHANGED`   ,
`MOD_DATESTAMP`   ,
`ITEM_CHANGED_REF`   ,
`MOD_UID`   ,
`OLD_VALUE`   ,
`NEW_VALUE`   ,
`MODIFICATION_DESCRIPTION`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_MOD_HISTORY

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_PAYMENT
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ACCTREFNO`   ,
`PRINCIPAL_PAYMENT_DAYVALUE`   ,
`INTEREST_PAYMENT_DAYVALUE`   ,
`FIRST_PRINCIPAL_PAYMENT_DATE`   ,
`INTEREST_ONLY_PAYMENT_DAYVALUE`   ,
`FIRST_INTEREST_PAYMENT_DATE`   ,
`CURRENT_PRINCIPAL_PAYMENT_DATE`   ,
`CURRENT_INTEREST_PAYMENT_DATE`   ,
`FIRST_INT_ONLY_PAYMENT_DATE`   ,
`NEXT_PRINCIPAL_PAYMENT_DATE`   ,
`NEXT_INTEREST_PAYMENT_DATE`   ,
`CURRENT_INT_ONLY_PAYMENT_DATE`   ,
`NEXT_PRINCIPAL_DUE_AMOUNT`   ,
`TOTAL_PRINCIPAL_PAYMENTS`   ,
`TOTAL_INTEREST_PAYMENTS`   ,
`NEXT_INTEREST_DUE_AMOUNT`   ,
`TOTAL_PAYMENTS`   ,
`PRINCIPAL_PAYMENTS_MADE`   ,
`NEXT_INT_ONLY_PAYMENT_DATE`   ,
`INTEREST_PAYMENTS_MADE`   ,
`ON_PRINCIPAL_PAYMENT`   ,
`TOTAL_PAYMENTS_MADE`   ,
`MINIMUM_PAYMENT_AMOUNT`   ,
`ON_INTEREST_PAYMENT`   ,
`FIXED_PAYMENT_AMOUNT`   ,
`ON_PAYMENT`   ,
`NEXT_PAYMENT_TOTAL_AMOUNT`   ,
`MAXIMUM_PAYMENT_AMOUNT`   ,
`LAST_PAYMENT_DATE`   ,
`LAST_PAYMENT_AMOUNT`   ,
`BALLOON_PAYMENT_DATE`   ,
`BALLOON_PAYMENT_AMOUNT`   ,
`AMORTIZED_PAYMENT_AMOUNT`   ,
`LAST_NSF_DATE`   ,
`LAST_NSF_AMOUNT`   ,
`NEXT_PTP_DATE`   ,
`LAST_BROKEN_PTP_DATE`   ,
`LAST_KEPT_PTP_DATE`   ,
`FIPD_TRUE_DATE`   ,
`FPPD_TRUE_DATE`   ,
`FIOPD_TRUE_DATE`   ,
`NIPD_TRUE_DATE`   ,
`NPPD_TRUE_DATE`   ,
`NIOPD_TRUE_DATE`   ,
`BPD_TRUE_DATE`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_PAYMENT

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_PAYMENTS_DUE
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`ACCTREFNO`   ,
`PAYMENT_REFERENCE_NO`   ,
`LATE_FEE_CODE`   ,
`PARTICIPANT_REFERENCE_NO`   ,
`IMPOUND_ID`   ,
`PAST_DUE_INDICATOR`   ,
`DATE_DUE`   ,
`PAYMENT_NUMBER`   ,
`PAYMENT_TYPE`   ,
`PAYMENT_AMOUNT`   ,
`PAYMENT_DESCRIPTION`   ,
`PAYMENT_PAID`   ,
`PAYMENT_REMAINING`   ,
`TRANSACTION_CODE`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_PAYMENTS_DUE

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_PAYMENT_HISTORY
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`ACCTREFNO`   ,
`PAYMENT_REFERENCE_NO`   ,
`TRANSACTION_REFERENCE_NO`   ,
`DATE_DUE`   ,
`PARTICIPANT_REFERENCE_NO`   ,
`CONSOLIDATED_REFERENCE_NO`   ,
`IMPOUND_ID`   ,
`DATE_PAID`   ,
`PAYMENT_NUMBER`   ,
`GL_DATE`   ,
`PAYMENT_TYPE`   ,
`PAYMENT_AMOUNT`   ,
`PAYMENT_DESCRIPTION`   ,
`TRANSACTION_CODE`   ,
`USER_REFERENCE`   ,
`MEMOENTRY`   ,
`PAYMENT_METHOD_NO`   ,
`PAYMENT_METHOD_REFERENCE`   ,
`LATE_FEE_CODE`   ,
`BATCH_NO`   ,
`USERDEF01`   ,
`ACH_TRACE_NUMBER`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`NSF_FLAG`   ,
`NSF_DATE`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_PAYMENT_HISTORY

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_SETUP
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ACCTREFNO`   ,
`LOAN_TEMPLATE_NO`   ,
`INTEREST_METHOD`   ,
`PRINCIPAL_PERIOD`   ,
`INTEREST_PERIOD`   ,
`INTEREST_YEAR_CALC`   ,
`INTEREST_YEAR_APPLIED`   ,
`INTEREST_PLAN_NO`   ,
`TERM_CHAR`   ,
`TERM`   ,
`TERM_DUE`   ,
`AMORT_PAYMENT_METHOD`   ,
`CUSTOM_PAYMENT_SCHEDULE`   ,
`PRINCIPAL_PAYMENT_METHOD`   ,
`PRINCIPAL_PAYMENT_PERCENTAGE`   ,
`PRINCIPAL_PAYMENT_FIXEDAMOUNT`   ,
`INTEREST_PAYMENT_METHOD`   ,
`AMORT_REPAYMENT_METHOD`   ,
`PR_AND_INT_RECALC_METHOD`   ,
`PREPAYMENT_TYPE`   ,
`PREPAYMENT_THRESHOLD_TYPE`   ,
`PREPAYMENT_THRESHOLD`   ,
`PREPAYMENT_TRANSCODE`   ,
`PREPAYMENT_AMOUNT`   ,
`INTEREST_FREE_TYPE`   ,
`INTEREST_FREE_PERIOD`   ,
`INTEREST_FREE_NUM_PERIODS`   ,
`INTEREST_FREE_MAX_DPD`   ,
`INTEREST_FREE_PAYOFF_GRACE`   ,
`PREPAYMENT_PERCENTAGE`   ,
`LATE_FEE_CODE`   ,
`BILLING_CUTOFF`   ,
`NSF_CODE`   ,
`STATEMENT1_CODE`   ,
`STATEMENT2_CODE`   ,
`ACCRUAL_CUTOFF`   ,
`PAYMENT_DISTRIBUTION_ORDER`   ,
`ACCRUAL_FLD`   ,
`ACCRUAL_BED`   ,
`BEG_INT_PERIOD_FLAG`   ,
`BEG_INT_INT_YEAR_CALC`   ,
`BEG_INT_INT_YEAR_APPLIED`   ,
`BEG_INT_PERIOD`   ,
`BEG_INT_TERM_CHAR`   ,
`AMORTIZATION_OPTION_FLAGS`   ,
`BEG_INT_TERM`   ,
`BILL_AFTER_MATURITY_FLAG`   ,
`SECOND_INTEREST_STREAM_FLAG`   ,
`DAILY_FEE_ACCRUAL_FLAG`   ,
`OPTION_FLAGS`   ,
`MINIMUM_ACCRUAL_BALANCE`   ,
`PAYMENT_RULES_NO`   ,
`PAYMENT_OPTION_FLAGS`   ,
`INTEREST_FREE_RG_MAX_DPD`   ,
`INTEREST_FREE_RG_PAYOFF_GRACE`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_SETUP

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_STATISTICS
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`ACCTREFNO`   ,
`YEAR_NUMBER`   ,
`MASTER_RECORD`   ,
`PRINCIPAL_ADVANCED`   ,
`MONTH_NUMBER`   ,
`PRINCIPAL_ADVANCED_NUM`   ,
`PRINCIPAL_ADVANCED_AVERAGE`   ,
`PRINCIPAL_PAID`   ,
`INTEREST_EARNED`   ,
`INTEREST_PAID`   ,
`INTEREST_WAIVED`   ,
`LATE_CHARGES_EARNED`   ,
`LATE_CHARGES_PAID`   ,
`DEF_INTEREST_EARNED`   ,
`LATE_CHARGES_WAIVED`   ,
`DEF_INTEREST_PAID`   ,
`DEF_INTEREST_WAIVED`   ,
`FEES_EARNED`   ,
`FEES_PAID`   ,
`FEES_WAIVED`   ,
`ESCROW_INTEREST_EARNED`   ,
`ESCROW_INTEREST_PAID`   ,
`LOAN_BALANCE_TOTAL`   ,
`LOAN_BALANCE_DAYS`   ,
`LOAN_BALANCE_AVERAGE`   ,
`LOAN_BALANCE_HIGH`   ,
`LOAN_BALANCE_LOW`   ,
`DAYS_LATE_10`   ,
`DAYS_LATE_30`   ,
`DAYS_LATE_60`   ,
`DAYS_LATE_90`   ,
`DAYS_LATE_OVER`   ,
`NSF_PAYMENTS`   ,
`USER_DEFINED_1`   ,
`USER_DEFINED_2`   ,
`USER_DEFINED_3`   ,
`USER_DEFINED_4`   ,
`USER_DEFINED_5`   ,
`USER_DEFINED_6`   ,
`USER_DEFINED_7`   ,
`USER_DEFINED_8`   ,
`USER_DEFINED_9`   ,
`USER_DEFINED_10`   ,
`USER_DEFINED_11`   ,
`USER_DEFINED_12`   ,
`USER_DEFINED_13`   ,
`USER_DEFINED_14`   ,
`USER_DEFINED_15`   ,
`USER_DEFINED_16`   ,
`USER_DEFINED_17`   ,
`USER_DEFINED_18`   ,
`USER_DEFINED_19`   ,
`USER_DEFINED_20`   ,
`USER_DEFINED_21`   ,
`USER_DEFINED_22`   ,
`USER_DEFINED_23`   ,
`USER_DEFINED_24`   ,
`USER_DEFINED_25`   ,
`KEPT_PROMISE_COUNTER`   ,
`BROKEN_PROMISE_COUNTER`   ,
`MADE_PROMISE_COUNTER`   ,
`SERVICING_FEE_EARNED`   ,
`SERVICING_FEE_PAID`   ,
`ACTUARIAL_INTEREST_EARNED`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_STATISTICS

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_STATUSES
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`ACCTREFNO`   ,
`STATUS_CODE_NO`   ,
`EFFECTIVE_DATE`   ,
`ENTRY_DATE`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_STATUSES

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_LOANACCT_TRANS_HISTORY
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`TRANSREFNO`   ,
`ACCTREFNO`   ,
`TRANSACTION_REFERENCE_NO`   ,
`AMORTIZED_FEE_ID`   ,
`VOUCHER_ID`   ,
`IMPOUND_ID`   ,
`CREDITLINE_ID`   ,
`DEPOSIT_ID`   ,
`TRANSACTION_CODE`   ,
`ASSOCIATED_TRANSREFNO`   ,
`REVERSAL_TRANSREFNO`   ,
`PARTICIPANT_REFERENCE_NO`   ,
`LOAN_GROUP_NO`   ,
`PARTICIPANT_DETAIL_FLAG`   ,
`PARTICIPANTREFNO`   ,
`ACH_TRACE_NUMBER`   ,
`TRANSACTION_DATE`   ,
`EFFECTIVE_DATE`   ,
`BATCH_NO`   ,
`TRANSACTION_AMOUNT`   ,
`ENTEREDBY_UID`   ,
`USER_REFERENCE`   ,
`COMMENT_INDICATOR`   ,
`TRANSACTION_TYPE`   ,
`GL_DATE`   ,
`TRANSACTION_DESCRIPTION`   ,
`PAYMENT_METHOD_NO`   ,
`PAYMENT_METHOD_REFERENCE`   ,
`USERDEF01`   ,
`MEMOENTRY`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`DATE_DUE`   ,
`PAYMENT_NUMBER`   ,
`PAYMENTGATEWAYTRANSACTIONID`   
FROM loan_mgmt_rt.rse_DBO_LOANACCT_TRANS_HISTORY

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_TASK
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`TASK_REFNO`   ,
`TASK_TEMPLATE_NO`   ,
`WORK_PACKET_REFNO`   ,
`WORK_PACKET_ORDER_NO`   ,
`WORK_QUEUE_CODE_ID`   ,
`STATUS_CODE_ID`   ,
`PRIORITY_CODE_ID`   ,
`NLS_TYPE`   ,
`NLS_REFNO`   ,
`CREATOR_UID`   ,
`OWNER_UID`   ,
`CREATION_DATE`   ,
`MODIFIED_DATE`   ,
`START_DATE`   ,
`DUE_DATE`   ,
`COMPLETION_DATE`   ,
`PERCENTAGE_COMPLETE`   ,
`OPTION_FLAGS`   ,
`SUBJECT`   ,
`NOTES`   ,
`QUEUE_ENTRY_TIME`   ,
`QUEUE_EXIT_TIME`   ,
`BRANCH_CIFNO`   
FROM loan_mgmt_rt.rse_DBO_TASK

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_TASK_DETAIL
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`TASK_REFNO`   ,
`USERDEF01`   ,
`USERDEF02`   ,
`USERDEF03`   ,
`USERDEF04`   ,
`USERDEF05`   ,
`USERDEF06`   ,
`USERDEF07`   ,
`USERDEF08`   ,
`USERDEF09`   ,
`USERDEF10`   ,
`USERDEF11`   ,
`USERDEF12`   ,
`USERDEF13`   ,
`USERDEF14`   ,
`USERDEF15`   ,
`USERDEF16`   ,
`USERDEF17`   ,
`USERDEF18`   ,
`USERDEF19`   ,
`USERDEF20`   ,
`USERDEF21`   ,
`USERDEF22`   ,
`USERDEF23`   ,
`USERDEF24`   ,
`USERDEF25`   ,
`USERDEF26`   ,
`USERDEF27`   ,
`USERDEF28`   ,
`USERDEF29`   ,
`USERDEF30`   ,
`USERDEF31`   ,
`USERDEF32`   ,
`USERDEF33`   ,
`USERDEF34`   ,
`USERDEF35`   ,
`USERDEF36`   ,
`USERDEF37`   ,
`USERDEF38`   ,
`USERDEF39`   ,
`USERDEF40`   ,
`USERDEF41`   ,
`USERDEF42`   ,
`USERDEF43`   ,
`USERDEF44`   ,
`USERDEF45`   ,
`USERDEF46`   ,
`USERDEF47`   ,
`USERDEF48`   ,
`USERDEF49`   ,
`USERDEF50`   ,
`USERDEF51`   ,
`USERDEF52`   ,
`USERDEF53`   ,
`USERDEF54`   ,
`USERDEF55`   ,
`USERDEF56`   ,
`USERDEF57`   ,
`USERDEF58`   ,
`USERDEF59`   ,
`USERDEF60`   ,
`USERDEF61`   ,
`USERDEF62`   ,
`USERDEF63`   ,
`USERDEF64`   ,
`USERDEF65`   ,
`USERDEF66`   ,
`USERDEF67`   ,
`USERDEF68`   ,
`USERDEF69`   ,
`USERDEF70`   ,
`USERDEF71`   ,
`USERDEF72`   ,
`USERDEF73`   ,
`USERDEF74`   ,
`USERDEF75`   ,
`USERDEF76`   ,
`USERDEF77`   ,
`USERDEF78`   ,
`USERDEF79`   ,
`USERDEF80`   ,
`USERDEF81`   ,
`USERDEF82`   ,
`USERDEF83`   ,
`USERDEF84`   ,
`USERDEF85`   ,
`USERDEF86`   ,
`USERDEF87`   ,
`USERDEF88`   ,
`USERDEF89`   ,
`USERDEF90`   ,
`USERDEF91`   ,
`USERDEF92`   ,
`USERDEF93`   ,
`USERDEF94`   ,
`USERDEF95`   ,
`USERDEF98`   ,
`USERDEF99`   ,
`USERDEF100`   ,
`USERDEF96`   ,
`USERDEF97`   
FROM loan_mgmt_rt.rse_DBO_TASK_DETAIL

CREATE OR REPLACE VIEW loan_mgmt_rv.rse_DBO_TASK_MODIFICATION_HISTORY
SELECT rowkey,
SYS_CHANGE_OPERATION,
 HBASE_UPD_TS
`ROW_ID`   ,
`TASK_REFNO`   ,
`ITEM_CHANGED`   ,
`MOD_DATESTAMP`   ,
`MOD_UID`   ,
`OLD_VALUE`   ,
`NEW_VALUE`   
FROM loan_mgmt_rt.rse_DBO_TASK_MODIFICATION_HISTORY
