package com.balicamp.dao.hibernate.unar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.unar.UnarDao;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mx.ReconcileDto;

@Repository
@Transactional
public class UnarDaoHibernate extends UnarGenericDaoHibernate<BaseAdminModel, String> implements UnarDao {
	
	private static final Logger LOGGER = Logger
			.getLogger(UnarDaoHibernate.class.getName());

	public UnarDaoHibernate() {
		super(BaseAdminModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] findBillingByInvoiceAndDate(String invoice, Date trxDate,
			Object[] mt940Data) {
		// TODO Auto-generated method stub
		String sql = "select "
				+"inv.invoice_number, "
				+"reg.id_registrant, "
				+"DATE_FORMAT(pay.due_date,  '%d/%c/%Y %H:%i:%k'), "
				+"DATE_FORMAT(pay.payment_date,  '%d/%c/%Y %H:%i:%k'), "
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

		String invoiceNo = reconcile.getInvoiceNo();
		String clientId = reconcile.getClientId();
		Character type = invoiceNo.charAt(0);
		
		Object[] objectInvoice = getInvoices(invoiceNo);
		
		if(objectInvoice != null) {
			if(type.equals('8')) {
				String idInvoice = (String) objectInvoice[0];
				Object[] objectRegistrants  = getRegistrants(idInvoice, clientId);
				
				if(objectRegistrants != null) {
					Object [] objectPayments = getPayments(idInvoice);
					if(objectPayments[5] != null) { //Id_request_iar
						//msh blm kelar ini, 
					}
				}
			}
		}
		
		return true;
		
	}
	
	public Object[] getInvoices(String invoiceNo) {
		String sql = "select  "
				+"id_invoice, "
				+"id_exam, "
				+"status, "
				+"invoice_number, "
				+"amount, "
				+"remark, "
				+"due_date, "
				+"payment_date, "
				+"created_on, "
				+"catatan"
				+"from "
				+"invoices  "
				+"where invoice_number = :invoice "; 
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("invoice", invoiceNo);
			
		Object obj = new Object();
		Object[] objectInvoice = null;
		
		List<Object> result = query.list();
		if (result.size()>0) {
			obj = result.get(0);
			objectInvoice = (Object[]) obj;
		}
		
		return objectInvoice;
	}
	
	@SuppressWarnings("unchecked")
	public Object[] getRegistrants(String idInvoice, String clientId) {
		
		String sqlPayments = "select  * "
				+"from "
				+"payments  "
				+"where id_registrant = :clientId "
				+"and id_invoice = :idInvoice "; 
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sqlPayments);
		query.setParameter("idInvoice", idInvoice);
			
		Object[] objectRegistrants = null;
		
		List<Object> result = query.list();
		if (result.size()>0) {
			String sqlRegistrants = "select "
					+"id_registrant, "
					+"id_exam, "
					+"id_jenis_sertifikat, "
					+"call_sign, "
					+"provinsi, "
					+"kabupaten, "
					+"status, "
					+"nama_registrant, "
					+"kode_pos, "
					+"alamat, "
					+"email, "
					+"no_handphone, "
					+"agama, "
					+"tempat_lahir, "
					+"tanggal_lahir, "
					+"jenis_kelamin, "
					+"pekerjaan, "
					+"foto, "
					+"scan_ktp, "
					+"masa_berlaku_ktp, "
					+"scan_npwp, "
					+"scan_iar, "
					+"skck, "
					+"si_orangtua, "
					+"scan_skar, "
					+"surat_izin_naik_tingkat, "
					+"sk_orari, "
					+"no_ktp, "
					+"no_skar, "
					+"tanggal_unar, "
					+"jenis_sertifikat_reor, "
					+"scan_reor, "
					+"remark, "
					+"created_by, "
					+"created_on, "
					+"modified_by, "
					+"modified_on "
					+"from "
					+"registrants  "
					+"where id_registrant = :clientId "; 
			
			Query queryRegistrants = getSessionFactory().getCurrentSession().createSQLQuery(sqlRegistrants);
			query.setParameter("clientId", clientId);
			
			List<Object> resultRegistrants = queryRegistrants.list();
			Object obj = new Object();
			if (resultRegistrants.size()>0) {
				obj = result.get(0);
				objectRegistrants = (Object[]) obj;
			}
		}
		
		return objectRegistrants;
	}
	
	public Object[] getPayments(String idInvoice) {
		String sql = "select  "
				+"id_payment, "
				+"id_registrant, "
				+"id_iar_khusus, "
				+"id_request_iar_khusus, "
				+"id_ikrap, "
				+"id_request_iar, "
				+"id_request_ikrap, "
				+"type, "
				+"status, "
				+"id_invoice, "
				+"amount, "
				+"remark, "
				+"due_date, "
				+"payment_date, "
				+"created_on "
				+"from "
				+"payments  "
				+"where id_invoice = :idInvoice "; 
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("idInvoice", idInvoice);
			
		Object obj = new Object();
		Object[] objectInvoice = null;
		
		List<Object> result = query.list();
		if (result.size()>0) {
			obj = result.get(0);
			objectInvoice = (Object[]) obj;
		}
		
		return objectInvoice;
	}
	
	public Object[] getRequestIar(String idRequestIar) {
		String sql = "select  "
				+"id_request_iar, "
				+"id_registrant, "
				+"callsign, "
				+"status, "
				+"tanggal_terbit, "
				+"masa_berlaku, "
				+"jenis_permohonan, "
				+"alasan, "
				+"provinsi, "
				+"kabupaten, "
				+"alamat, "
				+"surat_hilang, "
				+"foto_terbaru, "
				+"surat_pindah, "
				+"ktp_npwp_terbaru, "
				+"sertifikat_lama, "
				+"remark, "
				+"request_by, "
				+"request_on, "
				+"approve_by, "
				+"approve_on, "
				+"sk_perpanjangan, "
				+"prestasi_orari, "
				+"anggota_aktif "
				+"from "
				+"request_iar  "
				+"where id_request_iar = :idRequestIar "; 
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("idRequestIar", idRequestIar);
			
		Object obj = new Object();
		Object[] objectInvoice = null;
		
		List<Object> result = query.list();
		if (result.size()>0) {
			obj = result.get(0);
			objectInvoice = (Object[]) obj;
		}
		
		return objectInvoice;
	}
	
	public Object[] getCallsign(String callsign) {
		String sql = "select  "
				+"id_callsign, "
				+"id_registrant, "
				+"id_log_exam, "
				+"id_request_iar, "
				+"id_iar_khusus, "
				+"id_request_iar_khusus, "
				+"id_iar_asing, "
				+"id_ikrap, "
				+"id_iar_wni, "
				+"id_request_ikrap, "
				+"id_pembaruan, "
				+"id_reor, "
				+"value, "
				+"status, "
				+"validity_start, "
				+"validity_end, "
				+"created_by, "
				+"created_on, "
				+"modified_by, "
				+"modified_on, "
				+"nri, "
				+"keanggotaan, "
				+"no_iar, "
				+"no_kta, "
				+"masa_laku_kta, "
				+"catatan "
				+"from "
				+"callsign  "
				+"where id_callsign = :callsign "; 
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("callsign", callsign);
			
		Object obj = new Object();
		Object[] objectInvoice = null;
		
		List<Object> result = query.list();
		if (result.size()>0) {
			obj = result.get(0);
			objectInvoice = (Object[]) obj;
		}
		
		return objectInvoice;
	}
	
	public Boolean payPerpanjanganIar(Object [] objectInvoices, Object [] objectRegistrants,
			Object [] objectPayments) throws HibernateException, ParseException {
		Boolean result = false;
		
		String queryString = "update "
				+"payments "
				+"set status = 1, "
				+"payment_date = :paymentDate "
				+"where "
				+"id_payment = :idPayment ";
		
		Query query = getSession().createSQLQuery(queryString);
		query.setParameter("idPayment", objectPayments[0]);
		query.setParameter("idPayment", new Date());
		
		if (query.executeUpdate() > 0) {
			LOGGER.info("pay perpanjangan IAR : {}" + Arrays.toString(objectPayments));
			
			Object[] objectRequestIar = getRequestIar((String) objectPayments[5]); // ID_request_IAR
			
			Object[] objectCallSign = getCallsign((String) objectRequestIar[2]); // callsign
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			
			String tanggalTerbit = (String) objectRequestIar[4];
			String masaBerlaku = (String) objectRequestIar[5];
			
			String sqlUpdateCallsign =  "update "
					+"callsign "
					+"set id_request_iar = :idRequestIar, "
					+"validity_start = :validityStart, "
					+"validity_end = :validityEnd, "
					+"jenis_permohonan = 3, "
					+"status = 3 , "
					+"where "
					+"id_callsign = :idCallsign ";
			
			Query queryUpdateCallsign = getSession().createSQLQuery(sqlUpdateCallsign);
			query.setParameter("idRequestIar", objectRequestIar[0]);
			query.setParameter("validityStart", dateFormat.parse(tanggalTerbit));
			query.setParameter("validityEnd", dateFormat.parse(masaBerlaku));
			
		} else {
			return false;
		}
		
		return result;
		
	}
	
	public String createIarNumber(Object [] callsign)
    {
		return null;
    }
}
