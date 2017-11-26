create_namespace 'nls_elastic'
disable 'nls_elastic:DBO.BATCH_TRANSACTION_DETAIL'
drop 'nls_elastic:DBO.BATCH_TRANSACTION_DETAIL'
create 'nls_elastic:DBO.BATCH_TRANSACTION_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CAMPAIGN_LIST_DETAIL'
drop 'nls_elastic:DBO.CAMPAIGN_LIST_DETAIL'
create 'nls_elastic:DBO.CAMPAIGN_LIST_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF'
drop 'nls_elastic:DBO.CIF'
create 'nls_elastic:DBO.CIF', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_COMMENTS'
drop 'nls_elastic:DBO.CIF_COMMENTS'
create 'nls_elastic:DBO.CIF_COMMENTS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_COMMENTS_DOCS'
drop 'nls_elastic:DBO.CIF_COMMENTS_DOCS'
create 'nls_elastic:DBO.CIF_COMMENTS_DOCS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_DEMOGRAPHICS'
drop 'nls_elastic:DBO.CIF_DEMOGRAPHICS'
create 'nls_elastic:DBO.CIF_DEMOGRAPHICS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_DETAIL'
drop 'nls_elastic:DBO.CIF_DETAIL'
create 'nls_elastic:DBO.CIF_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_FINANCIALS'
drop 'nls_elastic:DBO.CIF_FINANCIALS'
create 'nls_elastic:DBO.CIF_FINANCIALS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_GROUPS'
drop 'nls_elastic:DBO.CIF_GROUPS'
create 'nls_elastic:DBO.CIF_GROUPS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_MODIFICATION_HISTORY'
drop 'nls_elastic:DBO.CIF_MODIFICATION_HISTORY'
create 'nls_elastic:DBO.CIF_MODIFICATION_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_PHONE_NUMS'
drop 'nls_elastic:DBO.CIF_PHONE_NUMS'
create 'nls_elastic:DBO.CIF_PHONE_NUMS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_USERS'
drop 'nls_elastic:DBO.CIF_USERS'
create 'nls_elastic:DBO.CIF_USERS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_WEB'
drop 'nls_elastic:DBO.CIF_WEB'
create 'nls_elastic:DBO.CIF_WEB', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.CIF_WEB_ENTRY_LOG'
drop 'nls_elastic:DBO.CIF_WEB_ENTRY_LOG'
create 'nls_elastic:DBO.CIF_WEB_ENTRY_LOG', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.DAILY_TRIAL_BALANCE'
drop 'nls_elastic:DBO.DAILY_TRIAL_BALANCE'
create 'nls_elastic:DBO.DAILY_TRIAL_BALANCE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.DTB_STATUSES'
drop 'nls_elastic:DBO.DTB_STATUSES'
create 'nls_elastic:DBO.DTB_STATUSES', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.ENTRYLOG'
drop 'nls_elastic:DBO.ENTRYLOG'
create 'nls_elastic:DBO.ENTRYLOG', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT'
drop 'nls_elastic:DBO.LOANACCT'
create 'nls_elastic:DBO.LOANACCT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_ACH'
drop 'nls_elastic:DBO.LOANACCT_ACH'
create 'nls_elastic:DBO.LOANACCT_ACH', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_COLLECTIONS'
drop 'nls_elastic:DBO.LOANACCT_COLLECTIONS'
create 'nls_elastic:DBO.LOANACCT_COLLECTIONS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_COLL_STATUS'
drop 'nls_elastic:DBO.LOANACCT_COLL_STATUS'
create 'nls_elastic:DBO.LOANACCT_COLL_STATUS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_COMMENTS'
drop 'nls_elastic:DBO.LOANACCT_COMMENTS'
create 'nls_elastic:DBO.LOANACCT_COMMENTS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_COMMENTS_DOCS'
drop 'nls_elastic:DBO.LOANACCT_COMMENTS_DOCS'
create 'nls_elastic:DBO.LOANACCT_COMMENTS_DOCS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_CREDITLINE'
drop 'nls_elastic:DBO.LOANACCT_CREDITLINE'
create 'nls_elastic:DBO.LOANACCT_CREDITLINE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_CREDITLINE_TC'
drop 'nls_elastic:DBO.LOANACCT_CREDITLINE_TC'
create 'nls_elastic:DBO.LOANACCT_CREDITLINE_TC', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_CREDIT_BUREAU'
drop 'nls_elastic:DBO.LOANACCT_CREDIT_BUREAU'
create 'nls_elastic:DBO.LOANACCT_CREDIT_BUREAU', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_DETAIL'
drop 'nls_elastic:DBO.LOANACCT_DETAIL'
create 'nls_elastic:DBO.LOANACCT_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_DETAIL_2'
drop 'nls_elastic:DBO.LOANACCT_DETAIL_2'
create 'nls_elastic:DBO.LOANACCT_DETAIL_2', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_GL_TRANS'
drop 'nls_elastic:DBO.LOANACCT_GL_TRANS'
create 'nls_elastic:DBO.LOANACCT_GL_TRANS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_GROUPS'
drop 'nls_elastic:DBO.LOANACCT_GROUPS'
create 'nls_elastic:DBO.LOANACCT_GROUPS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_INTACCRUAL'
drop 'nls_elastic:DBO.LOANACCT_INTACCRUAL'
create 'nls_elastic:DBO.LOANACCT_INTACCRUAL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_MOD_HISTORY'
drop 'nls_elastic:DBO.LOANACCT_MOD_HISTORY'
create 'nls_elastic:DBO.LOANACCT_MOD_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_PAYMENT'
drop 'nls_elastic:DBO.LOANACCT_PAYMENT'
create 'nls_elastic:DBO.LOANACCT_PAYMENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_PAYMENTS_DUE'
drop 'nls_elastic:DBO.LOANACCT_PAYMENTS_DUE'
create 'nls_elastic:DBO.LOANACCT_PAYMENTS_DUE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_PAYMENT_HISTORY'
drop 'nls_elastic:DBO.LOANACCT_PAYMENT_HISTORY'
create 'nls_elastic:DBO.LOANACCT_PAYMENT_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_RATE'
drop 'nls_elastic:DBO.LOANACCT_RATE'
create 'nls_elastic:DBO.LOANACCT_RATE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_RATE_HISTORY'
drop 'nls_elastic:DBO.LOANACCT_RATE_HISTORY'
create 'nls_elastic:DBO.LOANACCT_RATE_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_RECURRING_TRANS'
drop 'nls_elastic:DBO.LOANACCT_RECURRING_TRANS'
create 'nls_elastic:DBO.LOANACCT_RECURRING_TRANS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_SETUP'
drop 'nls_elastic:DBO.LOANACCT_SETUP'
create 'nls_elastic:DBO.LOANACCT_SETUP', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_STATEMENT'
drop 'nls_elastic:DBO.LOANACCT_STATEMENT'
create 'nls_elastic:DBO.LOANACCT_STATEMENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_STATEMENT_DETAIL'
drop 'nls_elastic:DBO.LOANACCT_STATEMENT_DETAIL'
create 'nls_elastic:DBO.LOANACCT_STATEMENT_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_STATISTICS'
drop 'nls_elastic:DBO.LOANACCT_STATISTICS'
create 'nls_elastic:DBO.LOANACCT_STATISTICS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_STATUSES'
drop 'nls_elastic:DBO.LOANACCT_STATUSES'
create 'nls_elastic:DBO.LOANACCT_STATUSES', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_TRANS_HISTORY'
drop 'nls_elastic:DBO.LOANACCT_TRANS_HISTORY'
create 'nls_elastic:DBO.LOANACCT_TRANS_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.LOANACCT_USERS'
drop 'nls_elastic:DBO.LOANACCT_USERS'
create 'nls_elastic:DBO.LOANACCT_USERS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.NLSGROUPPRIV'
drop 'nls_elastic:DBO.NLSGROUPPRIV'
create 'nls_elastic:DBO.NLSGROUPPRIV', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.NLSUSERPRIV'
drop 'nls_elastic:DBO.NLSUSERPRIV'
create 'nls_elastic:DBO.NLSUSERPRIV', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.TASK'
drop 'nls_elastic:DBO.TASK'
create 'nls_elastic:DBO.TASK', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.TASK_DETAIL'
drop 'nls_elastic:DBO.TASK_DETAIL'
create 'nls_elastic:DBO.TASK_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.TASK_DETAIL_GRID'
drop 'nls_elastic:DBO.TASK_DETAIL_GRID'
create 'nls_elastic:DBO.TASK_DETAIL_GRID', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.TASK_MODIFICATION_HISTORY'
drop 'nls_elastic:DBO.TASK_MODIFICATION_HISTORY'
create 'nls_elastic:DBO.TASK_MODIFICATION_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.TASK_ROUTING_HISTORY'
drop 'nls_elastic:DBO.TASK_ROUTING_HISTORY'
create 'nls_elastic:DBO.TASK_ROUTING_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.TASK_TEMPLATE_GROUPS_PRIV'
drop 'nls_elastic:DBO.TASK_TEMPLATE_GROUPS_PRIV'
create 'nls_elastic:DBO.TASK_TEMPLATE_GROUPS_PRIV', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.TMR'
drop 'nls_elastic:DBO.TMR'
create 'nls_elastic:DBO.TMR', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_elastic:DBO.TMR_UDF'
drop 'nls_elastic:DBO.TMR_UDF'
create 'nls_elastic:DBO.TMR_UDF', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
