package com.balicamp.webapp.constant;

import java.util.ArrayList;
import java.util.List;

import com.balicamp.webapp.model.priority.ObjectModel;

public final class PriorityConstant {
	private PriorityConstant() {
	}

	public static final class Parameter {
		public static final String TRX_CODE = "transactionCode";
		public static final String PROJECT_CODE = "projectCode";
		public static final String DESC = "description";
		public static final String PRODUCT_CODE = "productCode";
		public static final String ROUTING_CODE = "routingCode";
		//
		public static final String DEFAULT_KEY = "%A%";
		public static final String DEFAULT_CAT = "transactionCode";

		public static final List<ObjectModel> getCriteria() {
			List<ObjectModel> list = new ArrayList<ObjectModel>();
			list.add(new ObjectModel("transactionCode", "Kode Transaksi"));
			list.add(new ObjectModel("projectCode", "Nama Biller"));
			list.add(new ObjectModel("description", "Description"));
			list.add(new ObjectModel("productCode", "Kode Produk"));
			list.add(new ObjectModel("routingCode", "Kode Routing"));
			return list;
		}

		public static final List<ObjectModel> getCriteria2() {
			List<ObjectModel> list = new ArrayList<ObjectModel>();
			list.add(new ObjectModel("projectCode", "Nama Biller"));
			list.add(new ObjectModel("productCode", "Kode Produk"));
			return list;
		}
	}

	public static final class FromPage {
		public static final String UPDATE = "UPDATE";
	}

	public static final class ApprovalStatus {
		public static final int WAITING = 0;
		public static final int APPROVE = 1;
		public static final int REJECT = 2;

		public static final String getAppStatus(int status) {
			switch (status) {
			case REJECT:
				return "2 - Ditolak";

			case APPROVE:
				return "1 - Sudah Disetujui";

			default:
				return "0 - Menunggu Persetujuan";
			}
		}
	}
	
	public static final class UserType {
		public static final Long ROOT = new Long(0);
	}
	
	public static final class ProcessFlag {
		public static final int UPDATE = 0;
		public static final int DELETE = 1;
	}
}