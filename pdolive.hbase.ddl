create_namespace 'pdo_live'
disable 'pdo_live:DBO.ACCOUNT'
drop 'pdo_live:DBO.ACCOUNT'
create 'pdo_live:DBO.ACCOUNT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.ACCOUNTATTRIBUTE'
drop 'pdo_live:DBO.ACCOUNTATTRIBUTE'
create 'pdo_live:DBO.ACCOUNTATTRIBUTE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.ACCOUNT_STATUS'
drop 'pdo_live:DBO.ACCOUNT_STATUS'
create 'pdo_live:DBO.ACCOUNT_STATUS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.ACCOUNT_STATUS_HISTORY'
drop 'pdo_live:DBO.ACCOUNT_STATUS_HISTORY'
create 'pdo_live:DBO.ACCOUNT_STATUS_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.ACH'
drop 'pdo_live:DBO.ACH'
create 'pdo_live:DBO.ACH', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.ACHMERCHANTLENDER'
drop 'pdo_live:DBO.ACHMERCHANTLENDER'
create 'pdo_live:DBO.ACHMERCHANTLENDER', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.ACH_RECEIPT'
drop 'pdo_live:DBO.ACH_RECEIPT'
create 'pdo_live:DBO.ACH_RECEIPT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.ACH_RETURN_CODE'
drop 'pdo_live:DBO.ACH_RETURN_CODE'
create 'pdo_live:DBO.ACH_RETURN_CODE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.ADJUSTMENT'
drop 'pdo_live:DBO.ADJUSTMENT'
create 'pdo_live:DBO.ADJUSTMENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.APPLICATION'
drop 'pdo_live:DBO.APPLICATION'
create 'pdo_live:DBO.APPLICATION', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.APPLICATION_REASON'
drop 'pdo_live:DBO.APPLICATION_REASON'
create 'pdo_live:DBO.APPLICATION_REASON', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.APPLICATION_STATUS'
drop 'pdo_live:DBO.APPLICATION_STATUS'
create 'pdo_live:DBO.APPLICATION_STATUS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.APPLICATION_STATUS_HISTORY'
drop 'pdo_live:DBO.APPLICATION_STATUS_HISTORY'
create 'pdo_live:DBO.APPLICATION_STATUS_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.BANK'
drop 'pdo_live:DBO.BANK'
create 'pdo_live:DBO.BANK', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.CAMPAIGNEVENT'
drop 'pdo_live:DBO.CAMPAIGNEVENT'
create 'pdo_live:DBO.CAMPAIGNEVENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.CAMPAIGNTRACKING'
drop 'pdo_live:DBO.CAMPAIGNTRACKING'
create 'pdo_live:DBO.CAMPAIGNTRACKING', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.CAMPAIGNTRACKINGVALUE'
drop 'pdo_live:DBO.CAMPAIGNTRACKINGVALUE'
create 'pdo_live:DBO.CAMPAIGNTRACKINGVALUE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.COLLECTION'
drop 'pdo_live:DBO.COLLECTION'
create 'pdo_live:DBO.COLLECTION', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.COLLECTION_EVENT'
drop 'pdo_live:DBO.COLLECTION_EVENT'
create 'pdo_live:DBO.COLLECTION_EVENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.COLLECTION_EVENT_TYPE'
drop 'pdo_live:DBO.COLLECTION_EVENT_TYPE'
create 'pdo_live:DBO.COLLECTION_EVENT_TYPE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.COLLECTION_PROMISE'
drop 'pdo_live:DBO.COLLECTION_PROMISE'
create 'pdo_live:DBO.COLLECTION_PROMISE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.COLLECTION_WORKED'
drop 'pdo_live:DBO.COLLECTION_WORKED'
create 'pdo_live:DBO.COLLECTION_WORKED', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.COMMENTS'
drop 'pdo_live:DBO.COMMENTS'
create 'pdo_live:DBO.COMMENTS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.COMMENT_CLASS'
drop 'pdo_live:DBO.COMMENT_CLASS'
create 'pdo_live:DBO.COMMENT_CLASS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.DECISIONENGINEREJECTREASON'
drop 'pdo_live:DBO.DECISIONENGINEREJECTREASON'
create 'pdo_live:DBO.DECISIONENGINEREJECTREASON', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.EMPLOYMENT'
drop 'pdo_live:DBO.EMPLOYMENT'
create 'pdo_live:DBO.EMPLOYMENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.EXCEPTIONRETURNDETAIL'
drop 'pdo_live:DBO.EXCEPTIONRETURNDETAIL'
create 'pdo_live:DBO.EXCEPTIONRETURNDETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.IOVATIONHISTORY'
drop 'pdo_live:DBO.IOVATIONHISTORY'
create 'pdo_live:DBO.IOVATIONHISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LENDERTYPE'
drop 'pdo_live:DBO.LENDERTYPE'
create 'pdo_live:DBO.LENDERTYPE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOAN'
drop 'pdo_live:DBO.LOAN'
create 'pdo_live:DBO.LOAN', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOANBALANCE'
drop 'pdo_live:DBO.LOANBALANCE'
create 'pdo_live:DBO.LOANBALANCE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOANPARTICIPATION'
drop 'pdo_live:DBO.LOANPARTICIPATION'
create 'pdo_live:DBO.LOANPARTICIPATION', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOANPAYMENTTRANSACTION'
drop 'pdo_live:DBO.LOANPAYMENTTRANSACTION'
create 'pdo_live:DBO.LOANPAYMENTTRANSACTION', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOANPLANNEDPAYMENT'
drop 'pdo_live:DBO.LOANPLANNEDPAYMENT'
create 'pdo_live:DBO.LOANPLANNEDPAYMENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOAN_AGREEMENT'
drop 'pdo_live:DBO.LOAN_AGREEMENT'
create 'pdo_live:DBO.LOAN_AGREEMENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOAN_ROLLBACK'
drop 'pdo_live:DBO.LOAN_ROLLBACK'
create 'pdo_live:DBO.LOAN_ROLLBACK', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOAN_SERIES'
drop 'pdo_live:DBO.LOAN_SERIES'
create 'pdo_live:DBO.LOAN_SERIES', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOAN_STATUS'
drop 'pdo_live:DBO.LOAN_STATUS'
create 'pdo_live:DBO.LOAN_STATUS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOAN_STATUS_HISTORY'
drop 'pdo_live:DBO.LOAN_STATUS_HISTORY'
create 'pdo_live:DBO.LOAN_STATUS_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.LOAN_TYPE'
drop 'pdo_live:DBO.LOAN_TYPE'
create 'pdo_live:DBO.LOAN_TYPE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.PERSONAL'
drop 'pdo_live:DBO.PERSONAL'
create 'pdo_live:DBO.PERSONAL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.PROMOCODEUSAGE'
drop 'pdo_live:DBO.PROMOCODEUSAGE'
create 'pdo_live:DBO.PROMOCODEUSAGE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.REFERRAL'
drop 'pdo_live:DBO.REFERRAL'
create 'pdo_live:DBO.REFERRAL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.RPTTRIALBALANCEDAILY'
drop 'pdo_live:DBO.RPTTRIALBALANCEDAILY'
create 'pdo_live:DBO.RPTTRIALBALANCEDAILY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.STATES_RATE'
drop 'pdo_live:DBO.STATES_RATE'
create 'pdo_live:DBO.STATES_RATE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.STATE_RULE'
drop 'pdo_live:DBO.STATE_RULE'
create 'pdo_live:DBO.STATE_RULE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.TRANSACTIONACTION'
drop 'pdo_live:DBO.TRANSACTIONACTION'
create 'pdo_live:DBO.TRANSACTIONACTION', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.TRANSACTIONDETAIL'
drop 'pdo_live:DBO.TRANSACTIONDETAIL'
create 'pdo_live:DBO.TRANSACTIONDETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.TRANSACTIONRETURN'
drop 'pdo_live:DBO.TRANSACTIONRETURN'
create 'pdo_live:DBO.TRANSACTIONRETURN', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.TRANSACTIONSCHEDULED'
drop 'pdo_live:DBO.TRANSACTIONSCHEDULED'
create 'pdo_live:DBO.TRANSACTIONSCHEDULED', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.TRANSACTIONSETTLEMENT'
drop 'pdo_live:DBO.TRANSACTIONSETTLEMENT'
create 'pdo_live:DBO.TRANSACTIONSETTLEMENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.TRANSACTIONSTATUS'
drop 'pdo_live:DBO.TRANSACTIONSTATUS'
create 'pdo_live:DBO.TRANSACTIONSTATUS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.TRANSACTIONSUMMARY'
drop 'pdo_live:DBO.TRANSACTIONSUMMARY'
create 'pdo_live:DBO.TRANSACTIONSUMMARY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'pdo_live:DBO.TRANSACTIONTENDER'
drop 'pdo_live:DBO.TRANSACTIONTENDER'
create 'pdo_live:DBO.TRANSACTIONTENDER', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
