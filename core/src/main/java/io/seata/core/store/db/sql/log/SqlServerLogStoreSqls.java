package io.seata.core.store.db.sql.log;

import io.seata.common.loader.LoadLevel;
import io.seata.core.constants.ServerTableColumnsName;

/**
 * Database log store ms-sqlserver sql
 *
 * @author GoodBoyCoder
 */
@LoadLevel(name = "sqlserver")
public class SqlServerLogStoreSqls extends AbstractLogStoreSqls {

    /**
     * The constant INSERT_GLOBAL_TRANSACTION_SQLSERVER.
     */
    public static final String INSERT_GLOBAL_TRANSACTION_SQLSERVER = "insert into " + GLOBAL_TABLE_PLACEHOLD
            + "(" + ALL_GLOBAL_COLUMNS + ")"
            + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";

    /**
     * The constant UPDATE_GLOBAL_TRANSACTION_STATUS_SQLSERVER.
     */
    public static final String UPDATE_GLOBAL_TRANSACTION_STATUS_SQLSERVER = "update " + GLOBAL_TABLE_PLACEHOLD
            + "   set " + ServerTableColumnsName.GLOBAL_TABLE_STATUS + " = ?,"
            + "       " + ServerTableColumnsName.GLOBAL_TABLE_GMT_MODIFIED + " = GETDATE()"
            + " where " + ServerTableColumnsName.GLOBAL_TABLE_XID + " = ?";

    /**
     * The constant QUERY_GLOBAL_TRANSACTION_BY_STATUS.
     */
    public static final String QUERY_GLOBAL_TRANSACTION_BY_STATUS_SQLSERVER = "select top (?) " + ALL_GLOBAL_COLUMNS
            + "  from " + GLOBAL_TABLE_PLACEHOLD
            + " where " + ServerTableColumnsName.GLOBAL_TABLE_STATUS + " in (" + PRAMETER_PLACEHOLD + ")"
            + " order by " + ServerTableColumnsName.GLOBAL_TABLE_GMT_MODIFIED;

    /**
     * The constant QUERY_GLOBAL_TRANSACTION_FOR_RECOVERY_SQLSERVER.
     */
    public static final String QUERY_GLOBAL_TRANSACTION_FOR_RECOVERY_SQLSERVER = "select top (?) " + ALL_GLOBAL_COLUMNS
            + "  from " + GLOBAL_TABLE_PLACEHOLD
            + " where " + ServerTableColumnsName.GLOBAL_TABLE_STATUS + " in (0, 2, 3, 4, 5, 6, 7, 8, 10 ,12, 14)"
            + " order by " + ServerTableColumnsName.GLOBAL_TABLE_GMT_MODIFIED;

    /**
     * The constant INSERT_BRANCH_TRANSACTION_SQLSERVER.
     */
    public static final String INSERT_BRANCH_TRANSACTION_SQLSERVER = "insert into " + BRANCH_TABLE_PLACEHOLD
            + "(" + ALL_BRANCH_COLUMNS + ")"
            + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";

    /**
     * The constant UPDATE_BRANCH_TRANSACTION_STATUS_SQLSERVER.
     */
    public static final String UPDATE_BRANCH_TRANSACTION_STATUS_SQLSERVER = "update " + BRANCH_TABLE_PLACEHOLD
            + "   set " + ServerTableColumnsName.BRANCH_TABLE_STATUS + " = ?,"
            + "       " + ServerTableColumnsName.BRANCH_TABLE_GMT_MODIFIED + " = GETDATE()"
            + " where " + ServerTableColumnsName.BRANCH_TABLE_XID + " = ?"
            + "   and " + ServerTableColumnsName.BRANCH_TABLE_BRANCH_ID + " = ?";

    public static final String UPDATE_BRANCH_STATUS_APPLICATION_DATA_SQLSERVER = "update " + BRANCH_TABLE_PLACEHOLD
            + "   set " + ServerTableColumnsName.BRANCH_TABLE_STATUS + " = ?,"
            + "       " + ServerTableColumnsName.BRANCH_TABLE_APPLICATION_DATA + " = ?,"
            + "       " + ServerTableColumnsName.BRANCH_TABLE_GMT_MODIFIED + " = GETDATE()"
            + " where " + ServerTableColumnsName.BRANCH_TABLE_XID + " = ?"
            + "   and " + ServerTableColumnsName.BRANCH_TABLE_BRANCH_ID + " = ?";

    @Override
    public String getInsertGlobalTransactionSQL(String globalTable) {
        return INSERT_GLOBAL_TRANSACTION_SQLSERVER.replace(GLOBAL_TABLE_PLACEHOLD, globalTable);
    }

    @Override
    public String getUpdateGlobalTransactionStatusSQL(String globalTable) {
        return UPDATE_GLOBAL_TRANSACTION_STATUS_SQLSERVER.replace(GLOBAL_TABLE_PLACEHOLD, globalTable);
    }

    @Override
    public String getQueryGlobalTransactionSQLByStatus(String globalTable, String paramsPlaceHolder) {
        return QUERY_GLOBAL_TRANSACTION_BY_STATUS_SQLSERVER.replace(GLOBAL_TABLE_PLACEHOLD, globalTable)
                .replace(PRAMETER_PLACEHOLD, paramsPlaceHolder);
    }

    @Override
    public String getQueryGlobalTransactionForRecoverySQL(String globalTable) {
        return QUERY_GLOBAL_TRANSACTION_FOR_RECOVERY_SQLSERVER.replace(GLOBAL_TABLE_PLACEHOLD, globalTable);
    }

    @Override
    public String getInsertBranchTransactionSQL(String branchTable) {
        return INSERT_BRANCH_TRANSACTION_SQLSERVER.replace(BRANCH_TABLE_PLACEHOLD, branchTable);
    }

    @Override
    public String getUpdateBranchTransactionStatusSQL(String branchTable) {
        return UPDATE_BRANCH_TRANSACTION_STATUS_SQLSERVER.replace(BRANCH_TABLE_PLACEHOLD, branchTable);
    }

    @Override
    public String getUpdateBranchTransactionStatusAppDataSQL(String branchTable) {
        return UPDATE_BRANCH_STATUS_APPLICATION_DATA_SQLSERVER.replace(BRANCH_TABLE_PLACEHOLD, branchTable);
    }
}
