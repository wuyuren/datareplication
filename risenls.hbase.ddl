create_namespace 'nls_rise'
disable 'nls_rise:DBO.AMORTIZED_FEES'
drop 'nls_rise:DBO.AMORTIZED_FEES'
create 'nls_rise:DBO.AMORTIZED_FEES', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.AMORTIZED_FEES_CUSTOM'
drop 'nls_rise:DBO.AMORTIZED_FEES_CUSTOM'
create 'nls_rise:DBO.AMORTIZED_FEES_CUSTOM', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.BATCH_TRANSACTION'
drop 'nls_rise:DBO.BATCH_TRANSACTION'
create 'nls_rise:DBO.BATCH_TRANSACTION', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.BATCH_TRANSACTION_ACH'
drop 'nls_rise:DBO.BATCH_TRANSACTION_ACH'
create 'nls_rise:DBO.BATCH_TRANSACTION_ACH', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.BATCH_TRANSACTION_DETAIL'
drop 'nls_rise:DBO.BATCH_TRANSACTION_DETAIL'
create 'nls_rise:DBO.BATCH_TRANSACTION_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.CIF'
drop 'nls_rise:DBO.CIF'
create 'nls_rise:DBO.CIF', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.CIF_ADDRESSBOOK'
drop 'nls_rise:DBO.CIF_ADDRESSBOOK'
create 'nls_rise:DBO.CIF_ADDRESSBOOK', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.CIF_DEMOGRAPHICS'
drop 'nls_rise:DBO.CIF_DEMOGRAPHICS'
create 'nls_rise:DBO.CIF_DEMOGRAPHICS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.CIF_DETAIL'
drop 'nls_rise:DBO.CIF_DETAIL'
create 'nls_rise:DBO.CIF_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.CIF_FINANCIALS'
drop 'nls_rise:DBO.CIF_FINANCIALS'
create 'nls_rise:DBO.CIF_FINANCIALS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.CIF_PHONE_NUMS'
drop 'nls_rise:DBO.CIF_PHONE_NUMS'
create 'nls_rise:DBO.CIF_PHONE_NUMS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT'
drop 'nls_rise:DBO.LOANACCT'
create 'nls_rise:DBO.LOANACCT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_ACH'
drop 'nls_rise:DBO.LOANACCT_ACH'
create 'nls_rise:DBO.LOANACCT_ACH', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_COLLECTIONS'
drop 'nls_rise:DBO.LOANACCT_COLLECTIONS'
create 'nls_rise:DBO.LOANACCT_COLLECTIONS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_DETAIL'
drop 'nls_rise:DBO.LOANACCT_DETAIL'
create 'nls_rise:DBO.LOANACCT_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_DETAIL_2'
drop 'nls_rise:DBO.LOANACCT_DETAIL_2'
create 'nls_rise:DBO.LOANACCT_DETAIL_2', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_MOD_HISTORY'
drop 'nls_rise:DBO.LOANACCT_MOD_HISTORY'
create 'nls_rise:DBO.LOANACCT_MOD_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_PAYMENT'
drop 'nls_rise:DBO.LOANACCT_PAYMENT'
create 'nls_rise:DBO.LOANACCT_PAYMENT', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_PAYMENTS_DUE'
drop 'nls_rise:DBO.LOANACCT_PAYMENTS_DUE'
create 'nls_rise:DBO.LOANACCT_PAYMENTS_DUE', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_PAYMENT_HISTORY'
drop 'nls_rise:DBO.LOANACCT_PAYMENT_HISTORY'
create 'nls_rise:DBO.LOANACCT_PAYMENT_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_SETUP'
drop 'nls_rise:DBO.LOANACCT_SETUP'
create 'nls_rise:DBO.LOANACCT_SETUP', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_STATISTICS'
drop 'nls_rise:DBO.LOANACCT_STATISTICS'
create 'nls_rise:DBO.LOANACCT_STATISTICS', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_STATUSES'
drop 'nls_rise:DBO.LOANACCT_STATUSES'
create 'nls_rise:DBO.LOANACCT_STATUSES', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.LOANACCT_TRANS_HISTORY'
drop 'nls_rise:DBO.LOANACCT_TRANS_HISTORY'
create 'nls_rise:DBO.LOANACCT_TRANS_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.TASK'
drop 'nls_rise:DBO.TASK'
create 'nls_rise:DBO.TASK', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.TASK_DETAIL'
drop 'nls_rise:DBO.TASK_DETAIL'
create 'nls_rise:DBO.TASK_DETAIL', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
disable 'nls_rise:DBO.TASK_MODIFICATION_HISTORY'
drop 'nls_rise:DBO.TASK_MODIFICATION_HISTORY'
create 'nls_rise:DBO.TASK_MODIFICATION_HISTORY', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}
