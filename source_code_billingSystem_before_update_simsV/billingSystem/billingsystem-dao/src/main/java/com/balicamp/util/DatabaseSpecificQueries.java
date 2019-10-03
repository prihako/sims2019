package com.balicamp.util;

public abstract class DatabaseSpecificQueries {

	/**
	 * ORACLE
	 */
	/*
	public static String SEQUENCE_NEXTVAL(String sequenceName) {
		return "select " + sequenceName + ".NEXTVAL from DUAL";
	}

	public static final String RTM_GJRDS =
		"select t.request_date, u.user_name, u.user_full_name, t.token_serial_no," +
				" case t.register_status " +
					" when 'Y' then 'Terdaftar'" +
					" else 'Belum Terdaftar'" +
				" end REGISTER_STATUS," +
				" case t.activate_status" +
					" when 'Y' then 'Aktif'" +
					" else 'Belum Aktif'" +
				" end ACTIVATE_STATUS from s_user_token t" +
				" inner join s_user u on u.id=t.user_id" +
				" where t.request_date >= ?" +
				" and t.request_date < ? + interval '1' DAY";

	public static final String RTUP_QUERY_BASE =
		"select  " +
		"    rpad(tt.destination,14,' ') destination, " +
		"    lpad(round(tsp.amount_voucher),12,'0') amount,  " +
		"    rpad(nvl(tsp.serial_number,' '), 16, ' ') serial_number,  " +
		"    tt.status end_user_status,  " +
		"    rpad(nvl(tt.response_code,'50'),2,' ') response_code,  " +
		"    rpad(tsp.product,10,' ') name      " +
		"from  " +
		"    t_selular_prepaid tsp, " +
		"    t_transaction tt, " +
		"    t_schedule ts " +
		"where  " +
		"    tsp.id=tt.id  " +
		"    and ts.trx_id=tt.id " +
		"    and tt.status!='PENDING' ";

	public static final String RUA_GJRDS =
		"select a.created_date, u.user_name, a.info from t_audit_log a" +
			" left join s_user u on u.id=a.created_by" +
			" where a.created_date >= ?" +
			" and a.created_date < ? + interval '1' DAY";
*/
	/**
	 * POSTGRESQL
	 */
	public static String SEQUENCE_NEXTVAL(String sequenceName) {
		return "select nextval('" + sequenceName + "')";
	}

	public static final String RTM_GJRDS =
		"select t.request_date, u.user_name, u.user_full_name, t.token_serial_no," +
				" case t.register_status " +
					" when 'Y' then 'Terdaftar'" +
					" else 'Belum Terdaftar'" +
				" end REGISTER_STATUS," +
				" case t.activate_status" +
					" when 'Y' then 'Aktif'" +
					" else 'Belum Aktif'" +
				" end ACTIVATE_STATUS from s_user_token t" +
				" inner join s_user u on u.id=t.user_id" +
				" where t.request_date >= ?" +
				" and t.request_date < ? + interval '1 day'";

	/*public static final String RUA_GJRDS =
		"select a.created_date, u.user_name, a.info from t_audit_log a" +
			" left join s_user u on u.id=a.created_by" +
			" where a.created_date >= ?" +
			" and a.created_date < ? + interval '1 day'";*/

	public static final String RUA_GJRDS =
			"select a.created_date, u.user_name, a.info from t_audit_log a "
			+ "left join s_user u on u.id=a.created_by "
			+ "where a.created_date between ? and ? ";
}
