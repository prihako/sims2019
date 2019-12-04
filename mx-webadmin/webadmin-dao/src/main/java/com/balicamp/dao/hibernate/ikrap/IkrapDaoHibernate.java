package com.balicamp.dao.hibernate.ikrap;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.ikrap.IkrapDao;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mx.ReconcileDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Repository
@Transactional
public class IkrapDaoHibernate extends IkrapGenericDaoHibernate<BaseAdminModel, String> implements IkrapDao {

	public IkrapDaoHibernate() {
		super(BaseAdminModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] findBillingByInvoiceAndDate(String invoice, Date trxDate,
			Object[] mt940Data) {
		// TODO Auto-generated method stub
		String sql = "select "
					+"inv.invoice_number, "
					+"pay.payment_date, "
					+"pay.due_date, "
					+"pay.payment_date, "
					+"nama_registrant, "
					+"inv.amount "
					+"from "
					+"invoices inv join payments pay on "
					+"inv.id_invoice = pay.id_invoice "
					+"join registrants reg "
					+"on pay.id_registrant = reg.id_registrant "
					+"where inv.invoice_number = :invoice "; 
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("invoice", invoice);
			
		Object obj = new Object();
		Object[] objectArray = null;
		
		List<Object> result = query.list();
		if (result.size()>0) {
			obj = result.get(0);
			objectArray = (Object[]) obj;
		}
		return objectArray;
	}

	@Override
	public boolean updateInvoice(ReconcileDto reconcile,
			Date paymentDate, String remarks) {
		
		int status = 2;
		int keanggotaan = 0;
		int created_by = 1;
		
		Date start = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date validityStart = cal.getTime();

		cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 5);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		Date validityEnd = cal.getTime();
		
//		$this->no_ikrap = Ikrap::createIkrapNumber($this);
		
		String sqlIkrapId = "select " 
				+ "pay.id_ikrap, "
				+ "ikrap.provinsi,"
				+ "ikrap.kabupaten "
				+ "from " 
				+ "invoices inv join payments pay " 
				+ "on inv.id_invoice = pay.id_invoice "
				+ "join ikrap ikrap "
				+ "on pay.id_ikrap = ikrap.id_ikrap"
				+ "where inv.invoice_number = :invoiceNo " ; 
		
		Query queryIkrap = getSession().createSQLQuery(sqlIkrapId);
		queryIkrap.setParameter("invoiceNo", reconcile.getInvoiceNo());
		
		List<Object> result = queryIkrap.list();
		Object obj = new Object();
		Object[] objectArray = null;
		if (result.size() > 0) {

			obj = result.get(0);
			objectArray = (Object[]) obj;
			String provinsi = (String) objectArray[1];
			String kabupaten = (String) objectArray[2];

		}
		
		return true;
	}
	
	private String GenerateCallsign(String provinsi, String kabupaten) {
		String abjad = "ABCDEFGHIJKLMNOPQRSTUVWXY";
		char[] abjadArray = abjad.toCharArray();
		Boolean isValid = false;
		int length = abjad.length() - 1;
		String callSign = null;
		while (isValid == false) {
			String prefix = "JZ";
			String kodeProvinsi = getKodeProvinsi(provinsi);
			String kodeKota = getKodeKota(kabupaten);
			if (kodeKota == null) {
				kodeKota = String.valueOf(abjadArray[((int) (Math.random() * ((length) + 1)))]);
			}

			int lengthToTake = 2;
			String suffix = "";
			for (int i = 1; i <= lengthToTake; i++) {
				suffix += String.valueOf(abjadArray[((int) (Math.random() * ((length) + 1)))]);
			}
			int i = 0;
			while (isValid == false && i < kodeKota.length()) {
				callSign = prefix + kodeProvinsi + kodeKota.toCharArray()[i] + suffix;
				String callSignFromDb = getCallSign(callSign);
				if ((callSignFromDb != null) && !(callSignFromDb.substring(4).equals("ZZZ"))
						&& !(callSignFromDb.substring(4, 1).equals("Z"))) {
					isValid = true;
				} else {
					i++;
				}
			}
		}
		
		return callSign;
	}

	public String getKodeProvinsi(String namaProvinsi) {
		// TODO Auto-generated method stub
		String sql = "select kode_provinsi_ikrap " + "from " + "provinsi " + "where nama_provinsi = :namaProvinsi ";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("namaProvinsi", namaProvinsi);

		Object obj = new Object();
		Object[] objectArray = null;

		String kodeProvinsi = null;
		List<Object> result = query.list();
		if (result.size() > 0) {
			obj = result.get(0);
			objectArray = (Object[]) obj;
			kodeProvinsi = (String) objectArray[0];
		}
		return kodeProvinsi;
	}

	public String getKodeKota(String kabupaten) {
		Gson gson = new Gson();
		String sql = "select kode_kota_ikrap " + "from " + "kota " + "where nama_kota = :kabupaten ";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("kabupaten", kabupaten);

		Object obj = new Object();
		Object[] objectArray = null;

		String kodeKota = null;
		List<Object> result = query.list();
		if (result.size() > 0) {
			obj = result.get(0);
			objectArray = (Object[]) obj;
			kodeKota = (String) objectArray[0];

			Type type = new TypeToken<Map<String, String>>() {
			}.getType();
			Map<String, String> kodeKotaMap = gson.fromJson(kodeKota, type);
			kodeKota = kodeKotaMap.get("suffix");

		}
		return kodeKota;
	}

	public String getCallSign(String callSign) {
		String sql = "select call_sign " + "from " + "ikrap " + "where call_sign = :callSign ";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("callSign", callSign);

		Object obj = new Object();
		Object[] objectArray = null;

		String callSignResult = null;
		List<Object> result = query.list();
		if (result.size() > 0) {
			obj = result.get(0);
			objectArray = (Object[]) obj;
			callSignResult = (String) objectArray[0];
		}
		return callSignResult;
	}
	
	private String createIkrapNumber(String provinsi, String kabupaten){
		String zeroFill = "0000000";
		/*NO SERI*/
		String sql = "SELECT max(CAST(SUBSTR(no_ikrap,1,7) AS UNSIGNED)) as max "
				+ " FROM ikrap";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Object> result = query.list();
		if (result.size() > 0) {
			Object obj = result.get(0);
			Object[] objectArray = (Object[]) obj;
			String noSeri = (String) objectArray[0];
			
			if (noSeri.length() < 7) {
				noSeri = zeroFill.substring(0, zeroFill.length() - noSeri.length());
			}

			/*KODE PROVINSI*/
			String kodeProvinsi = getKodeProvinsi(provinsi);

			/*JENIS IKRAP*/
//			$perpanjangan = RequestIkrap::model()->find("callsign = '".$ikrap->call_sign."' AND status = ".RequestIkrap::STATUS_PAID);

	        
		}
		
		return null;

    }
}
