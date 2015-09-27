package com.ourdream.utils;

import org.apache.commons.dbutils.QueryRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JdbcUtil {

	private static ComboPooledDataSource cpds;

	static {
		cpds = new ComboPooledDataSource();
	}

	public static QueryRunner getQueryRunner() {
		QueryRunner qr = new QueryRunner(cpds);
		return qr;
	}

}
