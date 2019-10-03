package com.balicamp.webapp.constant;

import java.util.ArrayList;
import java.util.List;

import com.balicamp.webapp.model.priority.ObjectModel;

public final class MerchantConstant {
	private MerchantConstant() {
	}

	public static final String OBJ = "obj";
	public static final String NEW_STATUS = "newstatus";

	public static final class FromPage {
		public static final String ADD = "ADD";
		public static final String UPDATE = "UPDATE";
		public static final String DELETE = "DELETE";
	}

	public static final class Parameter {
		public static final String CODE = "code";
		public static final String DESC = "description";
		//
		public static final String DEFAULT_KEY = "%A%";
		public static final String DEFAULT_CAT = "code";

		public static final List<ObjectModel> getCriteria() {
			List<ObjectModel> list = new ArrayList<ObjectModel>();
			list.add(new ObjectModel(CODE, "Kode Merchant"));
			list.add(new ObjectModel(DESC, "Deskripsi Merchant"));
			return list;
		}

		//
		public static final String TERMID = "termid";
		public static final String CHANNEL_CODE = "channelCode";
		public static final String MERCHANT_CODE = "merchantGroup";

		//
		public static final List<ObjectModel> getDetailCriteria() {
			List<ObjectModel> list = new ArrayList<ObjectModel>();
			list.add(new ObjectModel(TERMID, "Terminal Id"));
			list.add(new ObjectModel(CHANNEL_CODE, "Kode Channel"));
			list.add(new ObjectModel(MERCHANT_CODE, "Kode Merchant"));
			return list;
		}
	}
}
