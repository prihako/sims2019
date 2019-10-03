package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.concurrent.SessionRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.mastermaintenance.license.LicenseSpectra;
import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.mastermaintenance.service.Services;
import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.operational.Invoices;
import com.balicamp.service.mastermaintenance.license.LicenseManager;
import com.balicamp.service.mastermaintenance.license.LicenseSpectraManager;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.operational.InvoiceManagerObsolete;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.operational.invoice.invoiceMain.InvoiceCreateDetail;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class LicenseSearch extends AdminBasePage implements
		PageBeginRenderListener {

	protected final Log log = LogFactory.getLog(LicenseSearch.class);

	private static final String INPUT_FILE_NAME = "C:\\Users\\ALDERI\\Desktop\\ISO8583.pdf";
	private static final String OUTPUT_FILE_NAME = "C:\\Users\\ALDERI\\Desktop\\SKM\\";
	
	public abstract boolean getClosed();
	public abstract void setClosed(boolean closed);

	public abstract void setServiceId(String serviceId);

	public abstract String getServiceId();

	public abstract void setSubServiceId(String subServiceId);

	public abstract String getSubServiceId();

	public abstract String getClientName();

	public abstract void setClientName(String clientName);

	public abstract String getLicenceNumber(); // mandatory

	public abstract void setLicenceNumber(String licenceNumber);

	public abstract String getApRefNumber();

	public abstract void setApRefNumber(String apRefNumber);

	public abstract void setServices(Services services);

	public abstract Services getServices();

	public abstract void setSubServices(SubServices subServices);

	public abstract SubServices getSubServices();

	public abstract void setLicenses(Licenses licenses);

	public abstract Licenses getLicenses();

	public abstract LicenseSpectra getSelectedLicense();

	public abstract void setSelectedLicense(LicenseSpectra selected);

	public abstract Licenses getSelectedLicenseForInvoice();

	public abstract void setSelectedLicenseForInvoice(Licenses selected);

	@InjectPage("licenseCreate")
	public abstract LicenseCreate getLicenseCreate();

	@InjectObject("spring:licenseSpectraManager")
	public abstract LicenseSpectraManager getLicenseSpectraManager();

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectObject("spring:servicesManager")
	public abstract ServicesManager getServicesManager();

	@InjectObject("spring:subServicesManager")
	public abstract SubServicesManager getSubServicesManager();

	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	@InjectSpring("invoiceManager")
	public abstract InvoiceManagerObsolete getInvoiceManager();

	@InjectObject("spring:sessionRegistry")
	public abstract SessionRegistry getSessionRegistry();

	public abstract void setInvoiceType(String invoiceType);

	public abstract String getInvoiceType();

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract LicenseSpectra getRow();

	public abstract Licenses getLicensesRow();

	public IPropertySelectionModel getServiceModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		List<Services> serviceList = getServicesManager().findAllServices();
		for (int i = 0; i < serviceList.size(); i++) {
			map.put(serviceList.get(i).getServiceId().toString(), serviceList
					.get(i).getServiceName());
		}
		return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public IPropertySelectionModel getSubServiceModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		List<SubServices> subServiceList = getSubServicesManager()
				.findAllSubServices();
		for (int i = 0; i < subServiceList.size(); i++) {
			map.put(subServiceList.get(i).getSubserviceId().toString(),
					subServiceList.get(i).getSubserviceName());
		}
		return new PropertySelectionModel(getLocale(), map, true, false);
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		

		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				}
			}
		}
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	public List getLicenseList() {
		List<LicenseSpectra> licenseList = (List<LicenseSpectra>) getFields()
				.get("LICENSE_LIST");
		return licenseList;
	}
	
	public List getClosedStatus() {
		List<Licenses> closedStatus = (List<Licenses>) getFields()
				.get("CLOSED_STATUS");
		return closedStatus;
	}

	public List getInvoiceList() {
		List<Licenses> licenseList = (List<Licenses>) getFields().get(
				"INVOICE_LIST");
		return licenseList;
	}

	@SuppressWarnings("unchecked")
	public void doSearch() {

		String metodeBhp = "";
		if (getInvoiceType() != null) {
			if (getInvoiceType().equals("MANUAL")) {
				metodeBhp = "FR";
			}
		}

		String validationMsg = generalValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		if (getClientName() == null) {
			setClientName("");
		}
		if (getApRefNumber() == null) {
			setApRefNumber("");
		}

		setLicenceNumber("");

		Services services = getServicesManager().findServicesById(
				Long.parseLong(getServiceId()));
		SubServices subServices = getSubServicesManager().findSubServicesById(
				Long.parseLong(getSubServiceId()));

//		if (getFields().get("PAGE") != null
//				&& getFields().get("PAGE").toString().equals("LICENSE")) {
			List<LicenseSpectra> licenseList = null;
			licenseList = getLicenseSpectraManager().searchLicenseForCreate(
					services.getServiceName(), subServices.getSubserviceName(),
					getClientName(), getLicenceNumber(), getApRefNumber());
			getFields().put("LICENSE_LIST", licenseList);
			
			//setClosed(true);
		//}

//		if (getFields().get("PAGE") != null
//				&& getFields().get("PAGE").toString().equals("INVOICE")) {
//			Integer yearTo = 1;
//			List<Licenses> licenseTemp = null;
//			List<Licenses> licenseList = new ArrayList<Licenses>();
//			List licenseIdList = getLicenseManager().findLicenseIdList(
//					services.getServiceId().toString(),
//					subServices.getSubserviceId().toString(), metodeBhp,
//					getClientName(), getApRefNumber(), getLicenceNumber());
//			for (int i = 0; i < licenseIdList.size(); i++) {
//				String year = getLicenseManager().findLicenseYear(
//						licenseIdList.get(i).toString());
//
//				if (year != null && !year.equals("")) {
//					yearTo = Integer.parseInt(year) + 1;
//				} else {
//					yearTo = 1;
//				}
//
//				licenseTemp = getLicenseManager().findLicenseForCreateInvoice(
//						licenseIdList.get(i).toString(), yearTo.toString());
//
//				if (licenseTemp != null && licenseTemp.size() > 0) {
//					Licenses licenses = licenseTemp.get(0);
//					licenseList.add(licenses);
//				}
//			}
//			getFields().put("INVOICE_LIST", licenseList);
//		}
	}

	private String generalValidation() {
		String errorMessage = null;

		if (getServiceId() == null) {
			errorMessage = getText("license.error.service.empty");
		} else if (getSubServiceId() == null) {
			errorMessage = getText("license.error.subServcie.empty");
		} else {
			errorMessage = null;
		}
		return errorMessage;
	}

	public void back(IRequestCycle cycle) {
		if (getFields().get("PAGE") != null
				&& getFields().get("PAGE").toString().equals("LICENSE")) {
			LicenseCreate licenseCreate = (LicenseCreate) cycle
					.getPage("licenseCreate");
			if (getLicenses() != null) {
				Licenses license = getLicenses();
				licenseCreate.setBhpTotal(license.getBhpTotal());
				licenseCreate.setLicenceNo(license.getLicenceNo());
				licenseCreate.setLicenceId(license.getLicenceId());
				licenseCreate.setServiceId(license.getServiceId());
				licenseCreate.setSubServiceId(license.getSubServiceId());
				licenseCreate.setClientName(license.getClientName());
				licenseCreate.setClientIDs(license.getClientId().toString());
				licenseCreate.setLicenceBegin(DateUtil.convertDateToString(
						license.getLicenceBegin(), "dd MMM yyyy"));
				licenseCreate.setLicenceEnd(DateUtil.convertDateToString(
						license.getLicenceEnd(), "dd MMM yyyy"));
				licenseCreate.setTransmitMin(license.getTransmitMin());
				licenseCreate.setTransmitMax(license.getTransmitMax());
				licenseCreate.setReceiveMin(license.getReceiveMin());
				licenseCreate.setReceiveMax(license.getReceiveMax());
			}
			cycle.activate(licenseCreate);
		}

		if (getFields().get("PAGE") != null
				&& getFields().get("PAGE").toString().equals("INVOICE")) {
			InvoiceCreateDetail invoiceCreateDetail = (InvoiceCreateDetail) cycle
					.getPage("invoiceCreateDetail");
			invoiceCreateDetail.setInvoiceType(getInvoiceType());
			invoiceCreateDetail
					.setLicenses((getLicenses() == null) ? new Licenses()
							: getLicenses());
			invoiceCreateDetail
					.setServices((getServices() == null) ? new Services()
							: getServices());
			invoiceCreateDetail
					.setSubServices((getSubServices() == null) ? new SubServices()
							: getSubServices());
			invoiceCreateDetail.setInvoices(new Invoices());
			cycle.activate(invoiceCreateDetail);
		}
	}

	public void doSelect(IRequestCycle cycle) {
		if (getSelectedLicense() != null) {
//			if (getFields().get("PAGE") != null
//					&& getFields().get("PAGE").toString().equals("LICENSE")) {
				LicenseCreate licenseCreate = (LicenseCreate) cycle
						.getPage("licenseCreate");
				LicenseSpectra licenseSpectra = getSelectedLicense();

				List<Licenses> createdLicenseList = getLicenseManager()
						.findListByLicenseNo(licenseSpectra.getApRefNumber());
				if (createdLicenseList != null && createdLicenseList.size() > 0) {
					addError(getDelegate(), "errorShadow",
							getText("license.error.alreadyCreated"),
							ValidationConstraint.CONSISTENCY);
					return;
				}

				Calendar dateExpire = Calendar.getInstance();
				Calendar dateEnd = Calendar.getInstance();

				List<VariableAnnualRate> obj = getVariableAnnualRateManager()
						.findByStatus(1);

//				System.out.println("create calculate BHP - BHP Rate No SKM : "
//						+ obj.get(0).getBaseOnNote());

				licenseCreate.setLicenceNo(licenseSpectra.getApRefNumber());
				licenseCreate.setLicenceId(licenseSpectra.getLicenceNumber());
//				licenseCreate.setLicenceNoBirate(obj.get(0).getBaseOnNote());
				licenseCreate.setService(licenseSpectra.getSv());
				licenseCreate.setServiceId(licenseSpectra.getSvId());
				licenseCreate.setSubService(licenseSpectra.getSs());
				licenseCreate.setSubServiceId(licenseSpectra.getSsId());
				licenseCreate.setClientName(licenseSpectra.getAddressCompany());
				licenseCreate.setClientIDs(licenseSpectra.getAddressNumber());
				if (licenseSpectra.getStartDate() != null) {
					licenseCreate.setLicenceBegin(DateUtil.convertDateToString(
							licenseSpectra.getStartDate(), "dd MMM yyyy"));
				}
				if (licenseSpectra.getEndDate() != null) {
					licenseCreate.setLicenceEnd(DateUtil.convertDateToString(
							licenseSpectra.getEndDate(), "dd MMM yyyy"));
				}
				dateEnd.setTime(licenseSpectra.getStartDate());
				dateEnd.add(Calendar.YEAR, 10);
				dateEnd.add(Calendar.DATE, -1);

				File file = new File(INPUT_FILE_NAME);

				byte[] docByte = new byte[(int) file.length()];

				FileInputStream fileInputStream = null;
				FileOutputStream fileOuputStream = null;

				File skmbaru = null;

				try {
					// convert file into array of bytes
					fileInputStream = new FileInputStream(file);
					fileInputStream.read(docByte);

					String fileSKM = OUTPUT_FILE_NAME
							+ "SKM_"
							+ licenseSpectra.getAddressCompany().substring(0,
									12) + "_" + licenseSpectra.getApRefNumber()
							+ ".pdf";
					fileOuputStream = new FileOutputStream(fileSKM);
					fileOuputStream.write(docByte);

					fileOuputStream.close();

					skmbaru = new File(fileSKM);

					System.out.println("Done");
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("nama SKM : " + skmbaru.getName());
				System.out.println("lokasi SKM : " + skmbaru.getAbsolutePath());
				System.out.println("path SKM : " + skmbaru.getPath());
				System.out.println("biner dokumen SKM : " + docByte.toString());
				System.out.println("biner  SKM : " + docByte);

				System.out
						.println("tanggal jatuh tempo = " + dateEnd.getTime());
				licenseCreate.setLicenceExpireDate(DateUtil
						.convertDateToString(dateEnd.getTime(), "dd MMM yyyy"));
				licenseCreate.setBhpPaymentType("Full Payment");
				licenseCreate.setYearTo(new BigDecimal(1));
				licenseCreate.setLicenceDocumentName(skmbaru.getName());
				licenseCreate.setLicenceDocumentPath(skmbaru.getAbsolutePath());
				//licenseCreate.setLicenceDocument(docByte);

				licenseCreate.setTransmitMin(licenseSpectra
						.getEqFreqRangeMinEtx());
				licenseCreate.setTransmitMax(licenseSpectra
						.getEqFreqRangeMaxEtx());
				licenseCreate.setReceiveMin(licenseSpectra
						.getEqFreqRangeMinErx());
				licenseCreate.setReceiveMax(licenseSpectra
						.getEqFreqRangeMaxErx());

				Licenses mainLicense = new Licenses();
				mainLicense.setLicenceNo(licenseSpectra.getApRefNumber());
				mainLicense.setLicenceId(licenseSpectra.getLicenceNumber());
				mainLicense.setServiceId(licenseSpectra.getSvId());
				mainLicense.setSubServiceId(licenseSpectra.getSsId());
				mainLicense.setClientName(licenseSpectra.getAddressCompany());
				mainLicense.setClientId(new BigDecimal(licenseSpectra
						.getAddressNumber()));
				mainLicense.setLicenceBegin(licenseSpectra.getStartDate());
				mainLicense.setLicenceEnd(licenseSpectra.getEndDate());
				mainLicense.setTransmitMin(licenseSpectra
						.getEqFreqRangeMinEtx());
				mainLicense.setTransmitMax(licenseSpectra
						.getEqFreqRangeMaxEtx());
				mainLicense
						.setReceiveMin(licenseSpectra.getEqFreqRangeMinErx());
				mainLicense
						.setReceiveMax(licenseSpectra.getEqFreqRangeMaxErx());

				getFields().put("LICENSE", mainLicense);
				if (getFields().get("BHP_LIST") != null) {
					getFields().put("BHP_LIST", null);
				}
				
				//setClosed(true);
				System.out.println("CLOSED = TRUE");
				licenseCreate.setLicenses(mainLicense);
				
				List<Licenses> closed = new ArrayList<Licenses>();
				getFields().put("CLOSED_STATUS", closed);

				cycle.activate(licenseCreate);
//			}

//			if (getFields().get("PAGE") != null
//					&& getFields().get("PAGE").toString().equals("INVOICE")) {
//
//			}

		} else {
			addError(getDelegate(), "errorShadow",
					getText("license.error.choose"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
	}

	public void doSelectForInvoice(IRequestCycle cycle) throws ParseException {
		if (getSelectedLicenseForInvoice() != null) {
			InvoiceCreateDetail invoiceCreateDetail = (InvoiceCreateDetail) cycle
					.getPage("invoiceCreateDetail");
			Licenses licenses = getSelectedLicenseForInvoice();
			Services services = getServicesManager().findServicesById(
					licenses.getServiceId().longValue());
			SubServices subServices = getSubServicesManager()
					.findSubServicesById(licenses.getSubServiceId().longValue());

			List invoiceList = getInvoiceManager()
					.findGeneratedInvoiceByLicense(licenses.getLicenceNo(),
							licenses.getLicenceId(),
							licenses.getYearTo().toString());

			if (invoiceList != null && invoiceList.size() > 0) {
				addError(getDelegate(), "errorShadow",
						getText("invoice.error.generated"),
						ValidationConstraint.CONSISTENCY);
				return;
			}

			if (getInvoiceType() != null && getInvoiceType().equals("INIT")
					&& !licenses.getYearTo().toString().equals("1")) {
				addError(getDelegate(), "errorShadow",
						getText("invoice.error.fail.initial"),
						ValidationConstraint.CONSISTENCY);
				return;
			}

			if (getInvoiceType() != null && getInvoiceType().equals("MANUAL")
					&& licenses.getYearTo().toString().equals("1")) {
				addError(getDelegate(), "errorShadow",
						getText("invoice.error.fail.manual"),
						ValidationConstraint.CONSISTENCY);
				return;
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(licenses.getLicenceBegin());
			calendar.add(Calendar.YEAR, 1);
			calendar.add(Calendar.DATE, -1);

			Invoices invoices = new Invoices();
			invoices.setPaymentDueDate(calendar.getTime());

			invoiceCreateDetail.setInvoices(invoices);
			invoiceCreateDetail.setLicenses(licenses);
			invoiceCreateDetail.setServices(services);
			invoiceCreateDetail.setSubServices(subServices);
			invoiceCreateDetail.setInvoiceType(getInvoiceType());

			invoiceCreateDetail.setLicenseBegin(DateUtil.convertDateToString(
					licenses.getLicenceBegin(), "dd-MMM-yyyy"));
			invoiceCreateDetail.setLicenseEnd(DateUtil.convertDateToString(
					licenses.getLicenceEnd(), "dd-MMM-yyyy"));
			invoiceCreateDetail.setInvoiceDueDate(DateUtil.convertDateToString(
					invoices.getPaymentDueDate(), "dd-MMM-yyyy"));

			if (licenses.getBhpMethod().equals(
					ModelConstant.MetodeBhp.FLAT_RATE)) {
				invoiceCreateDetail.setMethodBhp(getText("method.bhp.flat"));
			} else if (licenses.getBhpMethod().equals(
					ModelConstant.MetodeBhp.VARIATED_RATE)) {
				invoiceCreateDetail
						.setMethodBhp(getText("method.bhp.variated"));
			} else if (licenses.getBhpMethod().equals(
					ModelConstant.MetodeBhp.CONVERSION)) {
				invoiceCreateDetail
						.setMethodBhp(getText("method.bhp.conversion"));
			}

			cycle.activate(invoiceCreateDetail);
		} else {
			addError(getDelegate(), "errorShadow",
					getText("license.error.choose"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
	}

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		Date licenseBegin = df.parse(df.format(new Date()));
		Date test = new Date();
		System.out.println(licenseBegin);
	}
}
