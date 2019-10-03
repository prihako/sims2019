package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.LabelValue;
import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.mastermaintenance.service.Services;
import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.license.LicenseManager;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class LicenseCreate extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {

	private static final String OUTPUT_FILE_NAME = "C:\\Users\\ALDERI\\Desktop\\SKM\\";

	public LicenseCreate() {

	}

	public abstract Licenses getLicenses();

	public abstract void setLicenses(Licenses license);

	public abstract Map getLicenseMap();

	public abstract void setLicenseMap(Map licenseMap);

	public abstract String getService();

	public abstract void setService(String service);

	public abstract String getSubService();

	public abstract void setSubService(String subService);

	public abstract BigDecimal getServiceId();

	public abstract void setServiceId(BigDecimal serviceId);

	public abstract BigDecimal getSubServiceId();

	public abstract void setSubServiceId(BigDecimal subServiceId);

	public abstract Services getServices();

	public abstract void setServices(Services services);

	public abstract String getBhpMethod();

	public abstract void setBhpMethod(String bhpMethod);

	public abstract String getClientName();

	public abstract void setClientName(String clientName);

	public abstract String getClientIDs();

	public abstract void setClientIDs(String clientID);

	public abstract String getLicenceId();

	public abstract void setLicenceId(String licenceId);

	public abstract String getLicenceNo();

	public abstract void setLicenceNo(String licenceNo);

	public abstract Date getLicenceDate();

	public abstract void setLicenceDate(Date licenceDate);

	public abstract String getLicenceBegin();

	public abstract void setLicenceBegin(String licenceBegin);

	public abstract String getLicenceEnd();

	public abstract void setLicenceEnd(String licenceEnd);

	public abstract BigDecimal getYearTo();

	public abstract void setYearTo(BigDecimal yearTo);

	public abstract String getLicenceNoBirate();

	public abstract void setLicenceNoBirate(String licenceNoBirate);

	public abstract String getLicenceExpireDate();

	public abstract void setLicenceExpireDate(String licenceExpireDate);

	public abstract BigDecimal getTransmit();

	public abstract BigDecimal getTransmitMin();

	public abstract void setTransmitMin(BigDecimal transmitMin);

	public abstract BigDecimal getTransmitMax();

	public abstract void setTransmitMax(BigDecimal transmitMax);

	public abstract BigDecimal getReceive();

	public abstract BigDecimal getReceiveMin();

	public abstract void setReceiveMin(BigDecimal receivetMin);

	public abstract BigDecimal getReceiveMax();

	public abstract void setReceiveMax(BigDecimal receiveMax);

	public abstract String getBhpPaymentType();

	public abstract void setBhpPaymentType(String bhpPaymentType);

	public abstract Date getPaymentDueDate();

	public abstract void setPaymentDueDate(Date paymentDueDate);

	public abstract BigDecimal getBhpTotal();

	public abstract void setBhpTotal(BigDecimal bhpTotal);

	public abstract String getBhpUpfrontFee();

	public abstract void setBhpUpfrontFee(String bhpUpfrontFee);

	public abstract String getBhpRate();

	public abstract void setBhpRate(String bhpRate);

	public abstract String getBhpCalcIndex();

	public abstract void setBhpCalcIndex(String bhpCalcIndex);

	public abstract String getBhpPhl();

	public abstract void setBhpPhl(String bhpPhl);

	public abstract String getBhpHl();

	public abstract String getBhpAnnualPercent();

	public abstract void setBhpAnnualPercent(String bhpAnnualPercent);

	public abstract String getBhpAnnualValue();

	public abstract void setBhpAnnualValue(String bhpAnnualValue);

	public abstract void setBhpHl(String bhpHl);

	public abstract String getStatusActive();

	public abstract void setStatusActive(String activeStatus);

	public abstract int getActiveStatus();

	public abstract void setActiveStatus(int activeStatus);

	public abstract String getBaseOnNote();

	public abstract void setBaseOnNote(String baseOnNote);

	public abstract BigDecimal getPercentageYearOne();

	public abstract void setPercentageYearOne(BigDecimal percentageYearOne);

	public abstract BigDecimal getPercentageYearTwo();

	public abstract void setPercentageYearTwo(BigDecimal percentageYearTwo);

	public abstract BigDecimal getPercentageYearThree();

	public abstract void setPercentageYearThree(BigDecimal percentageYearThree);

	public abstract BigDecimal getPercentageYearFour();

	public abstract void setPercentageYearFour(BigDecimal percentageYearFour);

	public abstract BigDecimal getPercentageYearFive();

	public abstract void setPercentageYearFive(BigDecimal percentageYearFive);

	public abstract BigDecimal getPercentageYearSix();

	public abstract void setPercentageYearSix(BigDecimal percentageYearSix);

	public abstract BigDecimal getPercentageYearSeven();

	public abstract void setPercentageYearSeven(BigDecimal percentageYearSeven);

	public abstract BigDecimal getPercentageYearEight();

	public abstract void setPercentageYearEight(BigDecimal percentageYearEight);

	public abstract BigDecimal getPercentageYearNine();

	public abstract void setPercentageYearNine(BigDecimal percentageYearNine);

	public abstract BigDecimal getPercentageYearTen();

	public abstract void setPercentageYearTen(BigDecimal percentageYearTen);

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract VariableAnnualRate getRows();

	public abstract Licenses getRow1();

	public abstract IUploadFile getFile();

	public abstract void setFile(IUploadFile file);

	public abstract String getLicenceDocumentPath();

	public abstract void setLicenceDocumentPath(String licenceDocumentPath);

	public abstract String getLicenceDocumentName();

	public abstract void setLicenceDocumentName(String licenceDocumentName);

	public abstract byte[] getLicenceDocument();

	public abstract void setLicenceDocument(byte[] licenceDocument);

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();

	@InjectPage("licenseSpectra")
	public abstract LicenseSearch getLicenseSpectra();

	@InjectPage("licenseEntry")
	public abstract LicenseEntry getLicenseEntry();

	protected final Log log = LogFactory.getLog(LicenseCreate.class);


	public IPropertySelectionModel getMethodBhpModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("FR", "Flat Rate");
		map.put("VR", "Variety Rate");
		map.put("C", "Conversion");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public IPropertySelectionModel getAnnualPercentModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		List<VariableAnnualPercentage> annualPercentList = getVariableManager()
				.findByStatus(1);
//		for (int i = 0; i < annualPercentList.size(); i++) {
//			map.put(annualPercentList.get(i).getAnnualPercentId().toString(),
//					annualPercentList.get(i).getBaseOnNote());
//		}
		return new PropertySelectionModel(getLocale(), map, true, false);
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		if(getLicenses() == null){
			setLicenses(new Licenses());
		}

		if (!pageEvent.getRequestCycle().isRewinding()) {
				

			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() != null) {
					if (getFields().get("LICENSE") != null) {
						System.out.println("LICNESE_CREATE.....");
						setLicenses((Licenses) getFields().get("LICENSE"));
					} else if (getFields().get("BHP_CALC") == null) {
						System.out.println("BHP_CALC.....");

						setFields(new HashMap());
					}
				} else {
					System.out.println("NEW HASMAP.....");

					setFields(new HashMap());
				}
			}
		}

		List<VariableAnnualRate> obj = getVariableAnnualRateManager()
				.findByStatus(1);

		List<VariableAnnualPercentage> variable = getVariableManager()
				.findByStatus(1);

		Long annualPercentID = variable.get(0).getAnnualPercentId();

		VariableAnnualPercentageDetail percentageYearOne = getVariableDetailManager()
				.findByYear(annualPercentID, 1);
		VariableAnnualPercentageDetail percentageYearTwo = getVariableDetailManager()
				.findByYear(annualPercentID, 2);
		VariableAnnualPercentageDetail percentageYearThree = getVariableDetailManager()
				.findByYear(annualPercentID, 3);
		VariableAnnualPercentageDetail percentageYearFour = getVariableDetailManager()
				.findByYear(annualPercentID, 4);
		VariableAnnualPercentageDetail percentageYearFive = getVariableDetailManager()
				.findByYear(annualPercentID, 5);
		VariableAnnualPercentageDetail percentageYearSix = getVariableDetailManager()
				.findByYear(annualPercentID, 6);
		VariableAnnualPercentageDetail percentageYearSeven = getVariableDetailManager()
				.findByYear(annualPercentID, 7);
		VariableAnnualPercentageDetail percentageYearEight = getVariableDetailManager()
				.findByYear(annualPercentID, 8);
		VariableAnnualPercentageDetail percentageYearNine = getVariableDetailManager()
				.findByYear(annualPercentID, 9);
		VariableAnnualPercentageDetail percentageYearTen = getVariableDetailManager()
				.findByYear(annualPercentID, 10);

		setPercentageYearOne(percentageYearOne.getPercentage());
		setPercentageYearTwo(percentageYearTwo.getPercentage());
		setPercentageYearThree(percentageYearThree.getPercentage());
		setPercentageYearFour(percentageYearFour.getPercentage());
		setPercentageYearFive(percentageYearFive.getPercentage());
		setPercentageYearSix(percentageYearSix.getPercentage());
		setPercentageYearSeven(percentageYearSeven.getPercentage());
		setPercentageYearEight(percentageYearEight.getPercentage());
		setPercentageYearNine(percentageYearNine.getPercentage());
		setPercentageYearTen(percentageYearTen.getPercentage());

		setBhpRate(obj.get(0).getRateValue().toString());
		//setBaseOnNote(obj.get(0).getBaseOnNote());
		setStatusActive("active");

		BigDecimal multiplyIndex = (new BigDecimal(obj.get(0).getRateValue()
				.toString()).divide(new BigDecimal(100)))
				.add(new BigDecimal(1));

		setBhpCalcIndex(multiplyIndex.toString());

	}

	@Override
	public void pageEndRender(PageEvent event) {
		// super.pageEndRender(event);

	}

	public List getBhpList() {
		List<Licenses> bhpList = (List<Licenses>) getFields().get("BHP_LIST");
		return bhpList;
	}

	public List getBhpFRList() {
		List<Licenses> bhpList = (List<Licenses>) getFields()
				.get("BHP_FR_LIST");
		return bhpList;
	}
	
	public void addList(){
		System.out.println("ADD-LIST LICENSE CREATE");
	}

	public void doSearch(IRequestCycle cycle) {
		LicenseSearch licenseSearch = (LicenseSearch) cycle
				.getPage("licenseSearch");
		System.out.println("DO SEARCH");
		getFields().put("PAGE", "LICENSE");
		getFields().put("LICENSE_LIST", null);
		licenseSearch.setFields(getFields());
		// saveObjectToSession(null, "LICENSE_LIST");
		cycle.activate(licenseSearch);
	}

	public void info() {
		System.out.println("getBhpAnnualPercent = " + getBhpAnnualPercent());

		Long annualPercentId = Long.parseLong(getBhpAnnualPercent());

		System.out.println("annualPercentId = " + annualPercentId);
		VariableAnnualPercentage variable = getVariableManager()
				.findByAnnualPercentId(annualPercentId);

		VariableAnnualPercentageDetail percentageYearOne = getVariableDetailManager()
				.findByYear(annualPercentId, 1);
		VariableAnnualPercentageDetail percentageYearTwo = getVariableDetailManager()
				.findByYear(annualPercentId, 2);
		VariableAnnualPercentageDetail percentageYearThree = getVariableDetailManager()
				.findByYear(annualPercentId, 3);
		VariableAnnualPercentageDetail percentageYearFour = getVariableDetailManager()
				.findByYear(annualPercentId, 4);
		VariableAnnualPercentageDetail percentageYearFive = getVariableDetailManager()
				.findByYear(annualPercentId, 5);
		VariableAnnualPercentageDetail percentageYearSix = getVariableDetailManager()
				.findByYear(annualPercentId, 6);
		VariableAnnualPercentageDetail percentageYearSeven = getVariableDetailManager()
				.findByYear(annualPercentId, 7);
		VariableAnnualPercentageDetail percentageYearEight = getVariableDetailManager()
				.findByYear(annualPercentId, 8);
		VariableAnnualPercentageDetail percentageYearNine = getVariableDetailManager()
				.findByYear(annualPercentId, 9);
		VariableAnnualPercentageDetail percentageYearTen = getVariableDetailManager()
				.findByYear(annualPercentId, 10);

		setPercentageYearOne(percentageYearOne.getPercentage());
		setPercentageYearTwo(percentageYearTwo.getPercentage());
		setPercentageYearThree(percentageYearThree.getPercentage());
		setPercentageYearFour(percentageYearFour.getPercentage());
		setPercentageYearFive(percentageYearFive.getPercentage());
		setPercentageYearSix(percentageYearSix.getPercentage());
		setPercentageYearSeven(percentageYearSeven.getPercentage());
		setPercentageYearEight(percentageYearEight.getPercentage());
		setPercentageYearNine(percentageYearNine.getPercentage());
		setPercentageYearTen(percentageYearTen.getPercentage());
	}

	public void doDraft(IRequestCycle cycle) {

		String host = getLicenseEntry().getRequest().getProtocol();
		int port = getLicenseEntry().getRequest().getServerPort();

		System.out.println("Host/Port : " + host + ":" + port);//test to get ApplicationServer IP and Port
		System.out.println("upload_ile_path = " + getFile().getFilePath());
		System.out.println("file_name = " + getFile().getFileName());
		System.out.println("file_length = " + getFile().getSize());

		FileOutputStream fos = null;

		byte[] documentBinary = new byte[(int) getFile().getSize()];

		String documentPath = OUTPUT_FILE_NAME + getFile().getFileName();

		try {
			getFile().getStream().read(documentBinary);

			fos = new FileOutputStream(documentPath);

			fos.write(documentBinary);
			fos.close();
			getFile().getStream().close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String validationMsg = saveValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		List<VariableAnnualRate> obj = getVariableAnnualRateManager()
				.findByStatus(1);

	//	System.out.println("BHP Rate SKM : " + obj.get(0).getBaseOnNote());

		Licenses licenses = null;
		List<Licenses> licenseList = null;

		Calendar datePayment = Calendar.getInstance();
		datePayment.setTime(getPaymentDueDate());

		if (getFields().get("LICENCES_LIST") != null) {
			licenseList = (List<Licenses>) getFields().get("LICENCES_LIST");
			for (int i = 0; i < licenseList.size(); i++) {
				licenses = licenseList.get(i);
				licenses.setBhpMethod(getBhpMethod());

				if (licenses.getYearTo().equals(new BigDecimal(1))) {
					licenses.setPaymentDueDate(getPaymentDueDate());

				} else {
					datePayment.add(Calendar.YEAR, 1);
					licenses.setPaymentDueDate(datePayment.getTime());

				}
				licenses.setBhpPaymentType("FP");
				if (getBhpMethod().equals("VR")) {

					if (licenses.getYearTo().equals(new BigDecimal(1))) {
						licenses.setLicenceStatus("D");
					} else {
						licenses.setLicenceStatus(null);
					}

				} else {
					licenses.setLicenceStatus("D");

				}

				licenses.setLicenceDate(getLicenceDate());
				licenses.setLicenceNoBirate(getLicenceNoBirate());

				System.out.println("licence_document_skm = " + documentBinary);

//				licenses.setLicenceDocument(documentBinary);
				try {
					licenses.setLicenceExpireDate(DateUtil.convertStringToDate(
							"dd MMM yyyy", getLicenceExpireDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				licenses.setCreatedOn(new Date());
				licenses.setCreatedBy(getUserLoginFromSession().getUsername());
				getLicenseManager().save(licenses);
			}
		} else {
			licenses = new Licenses();
			licenses.setLicenceNo(getLicenceNo());
			licenses.setLicenceId(getLicenceId());
			licenses.setServiceId(getServiceId());
			licenses.setSubServiceId(getSubServiceId());
			licenses.setClientName(getClientName());

			licenses.setClientId(new BigDecimal(getClientIDs()));
			try {
				licenses.setLicenceBegin(DateUtil
						.convertStringToDate(getLicenceBegin()));
				licenses.setLicenceEnd(DateUtil
						.convertStringToDate(getLicenceEnd()));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			licenses.setPaymentDueDate(getPaymentDueDate());
			licenses.setTransmitMin(getTransmitMin());
			licenses.setTransmitMax(getTransmitMax());
			licenses.setReceiveMin(getReceiveMin());
			licenses.setReceiveMax(getReceiveMax());
			licenses.setBhpMethod(getBhpMethod());
			licenses.setBhpPaymentType("FP");
			licenses.setLicenceStatus("D");
			licenses.setLicenceDate(getLicenceDate());
			licenses.setLicenceNoBirate(getLicenceNoBirate());
			licenses.setCreatedOn(new Date());
			licenses.setCreatedBy(getUserLoginFromSession().getUsername());
			getLicenseManager().save(licenses);
		}

		getFields().remove("BHP_LIST");
		getFields().remove("BHP_FR_LIST");
		getFields().clear();

		InfoPageCommand infoPageCommand = new InfoPageCommand();

		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information",
				new Object[] { getLicenceId(), getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "licenseEntry.html"));

		// addError(getDelegate(), "errorShadow",
		// getText("license.draft.success"), ValidationConstraint.CONSISTENCY);
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		cycle.activate(infoPage);

	}

	public void doSubmit(IRequestCycle cycle) {
		
		FileOutputStream fos = null;

		byte[] documentBinary = new byte[(int) getFile().getSize()];

		String documentPath = OUTPUT_FILE_NAME + getFile().getFileName();

		try {
			getFile().getStream().read(documentBinary);

			fos = new FileOutputStream(documentPath);

			fos.write(documentBinary);
			fos.close();
			getFile().getStream().close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String validationMsg = saveValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		List<VariableAnnualRate> obj = getVariableAnnualRateManager()
				.findByStatus(1);

//		System.out.println("Submit BHP Rate SKM : "
//				+ obj.get(0).getBaseOnNote());

		Licenses licenses = null;
		List<Licenses> licenseList = null;

		Calendar datePayment = Calendar.getInstance();
		datePayment.setTime(getPaymentDueDate());

		if (getFields().get("LICENCES_LIST") != null) {
			licenseList = (List<Licenses>) getFields().get("LICENCES_LIST");
			for (int i = 0; i < licenseList.size(); i++) {
				licenses = licenseList.get(i);

				licenses.setBhpMethod(getBhpMethod());

				if (licenses.getYearTo().equals(new BigDecimal(1))) {
					licenses.setPaymentDueDate(getPaymentDueDate());

				} else {
					datePayment.add(Calendar.YEAR, 1);
					licenses.setPaymentDueDate(datePayment.getTime());

				}

				// licenses.setPaymentDueDate(getPaymentDueDate());
				// licenses.setBhpPaymentType(getBhpPaymentType());
//				licenses.setLicenceDocument(documentBinary);
				licenses.setBhpPaymentType("FP");

				if (getBhpMethod().equals("VR")) {

					if (licenses.getYearTo().equals(new BigDecimal(1))) {
						licenses.setLicenceStatus("S");
					} else {
						licenses.setLicenceStatus(null);
					}

				} else {
					licenses.setLicenceStatus("S");

				}

				try {
					licenses.setLicenceExpireDate(DateUtil.convertStringToDate(
							"dd MMM yyyy", getLicenceExpireDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				licenses.setLicenceDate(getLicenceDate());
				licenses.setLicenceNoBirate(getLicenceNoBirate());
				licenses.setCreatedOn(new Date());
				licenses.setCreatedBy(getUserLoginFromSession().getUsername());
				getLicenseManager().save(licenses);
			}
		}

		getFields().remove("BHP_LIST");
		getFields().remove("BHP_FR_LIST");
		getFields().clear();

		// addError(getDelegate(), "errorShadow",
		// getText("license.submit.success"), ValidationConstraint.CONSISTENCY);
		InfoPageCommand infoPageCommand = new InfoPageCommand();

		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information",
				new Object[] { getLicenceId(), getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "licenseEntry.html"));

		// addError(getDelegate(), "errorShadow",
		// getText("license.draft.success"), ValidationConstraint.CONSISTENCY);
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		cycle.activate(infoPage);
	}

	public IPage doCancel() {
		LicenseEntry licenseEntry = getLicenseEntry();
		return licenseEntry;
	}

	private String generalValidation() {

		String errorMessage = null;

		if (getLicenceNo() == null) {
			errorMessage = getText("calculateBhp.error.licenseNumber");
		} else {
			errorMessage = null;
		}
		return errorMessage;
	}

	private String saveValidation() {

		String errorMessage = null;

		Calendar datePayment = Calendar.getInstance();
		try {
			datePayment.setTime(DateUtil.convertStringToDate("dd MMM yyyy",
					getLicenceBegin()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// datePayment.add(Calendar.YEAR, 1);
		datePayment.add(Calendar.DATE, -1);

		System.out.println("date PAYMENT = " + datePayment);

		if (getLicenceNo() == null) {
			errorMessage = getText("calculateBhp.error.licenseNumber");
		} else if (getBhpTotal() == null) {
			errorMessage = getText("calculateBhp.error.calculateBhp");
		} else if (!getPaymentDueDate().equals(datePayment.getTime())) {
			errorMessage = getText("calculateBhp.error.paymentDueDate");

		} else {
			errorMessage = null;
		}
		return errorMessage;
	}

	public void hitungBHP(IRequestCycle cycle) {
		String validationMsg = generalValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		if (getBhpMethod().equals("VR")) {

			System.out.println("METODE BHP VR = OK ");

			Licenses mainLicense = new Licenses();
			mainLicense.setLicenceNo(getLicenceNo());
			mainLicense.setLicenceId(getLicenceId());
			mainLicense.setServiceId(getServiceId());
			mainLicense.setSubServiceId(getSubServiceId());
			mainLicense.setClientName(getClientName());
			mainLicense.setClientId(new BigDecimal(getClientIDs()));
			try {
				mainLicense.setLicenceBegin(DateUtil.convertStringToDate(
						"dd MMM yyyy", getLicenceBegin()));
				mainLicense.setLicenceEnd(DateUtil.convertStringToDate(
						"dd MMM yyyy", getLicenceEnd()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mainLicense.setTransmitMin(getTransmitMin());
			mainLicense.setTransmitMax(getTransmitMax());
			mainLicense.setReceiveMin(getReceiveMin());
			mainLicense.setReceiveMax(getReceiveMax());

			for (int i = 0; i < 11; i++) {
				getFields().put("LICENSE" + i, new Licenses(mainLicense));
			}

			Calendar dateBegin = Calendar.getInstance();
			Calendar dateEnd = Calendar.getInstance();

			List<Licenses> bhpList = new ArrayList<Licenses>();
			List<Licenses> bhpListVR = new ArrayList<Licenses>();

			System.out.println("UpFrontFee = " + getBhpUpfrontFee());
			System.out.println("PHL = " + getBhpPhl());
			System.out.println("Persentase BHP Tahunan = "
					+ getBhpAnnualPercent());

			Long annualPercentID = Long.parseLong(getBhpAnnualPercent());

			List<VariableAnnualPercentageDetail> variableAnnualPercentageDetailList = getVariableDetailManager()
					.findByAnnualPercentId(annualPercentID);

			BigDecimal percentage = variableAnnualPercentageDetailList.get(0)
					.getPercentage();

			BigDecimal upfrontfee = new BigDecimal(getBhpUpfrontFee());

			List<VariableAnnualPercentage> annualpercentage = getVariableManager()
					.findByStatus(1);

			int bhpAnnualRate1 = 0;
			int bhpAnnualRate2 = 0;
			int bhpAnnualRate3 = 0;
			int bhpAnnualRate4 = 0;
			int bhpAnnualRate5 = 0;
			int bhpAnnualRate6 = 0;
			int bhpAnnualRate7 = 0;
			int bhpAnnualRate8 = 0;
			int bhpAnnualRate9 = 0;
			int bhpAnnualRate10 = 0;

			List<VariableAnnualPercentageDetail> annualPercentageList = getVariableDetailManager()
					.findByAnnualPercentId(annualPercentID);
			for (VariableAnnualPercentageDetail annualPercentage : annualPercentageList) {

				System.out.println("tahun  = " + annualPercentage.getYearTo()
						+ ", percentage = "
						+ annualPercentage.getPercentage().intValue());
				if (annualPercentage.getYearTo() == 1) {
					bhpAnnualRate1 = annualPercentage.getPercentage()
							.intValue();
				} else if (annualPercentage.getYearTo() == 2) {
					bhpAnnualRate2 = annualPercentage.getPercentage()
							.intValue();
				} else if (annualPercentage.getYearTo() == 3) {
					bhpAnnualRate3 = annualPercentage.getPercentage()
							.intValue();
				} else if (annualPercentage.getYearTo() == 4) {
					bhpAnnualRate4 = annualPercentage.getPercentage()
							.intValue();
				} else if (annualPercentage.getYearTo() == 5) {
					bhpAnnualRate5 = annualPercentage.getPercentage()
							.intValue();
				} else if (annualPercentage.getYearTo() == 6) {
					bhpAnnualRate6 = annualPercentage.getPercentage()
							.intValue();
				} else if (annualPercentage.getYearTo() == 7) {
					bhpAnnualRate7 = annualPercentage.getPercentage()
							.intValue();
				} else if (annualPercentage.getYearTo() == 8) {
					bhpAnnualRate8 = annualPercentage.getPercentage()
							.intValue();
				} else if (annualPercentage.getYearTo() == 9) {
					bhpAnnualRate9 = annualPercentage.getPercentage()
							.intValue();
				} else if (annualPercentage.getYearTo() == 10) {
					bhpAnnualRate10 = annualPercentage.getPercentage()
							.intValue();
				}

			}

			System.out.println("percentage tahun pertama " + percentage);
			System.out.println("upfrontfee =  " + upfrontfee);

			// Tahun ke-1
			Licenses license = (Licenses) getFields().get("LICENSE1");
			BigDecimal percent1 = new BigDecimal(bhpAnnualRate1);
			BigDecimal bhpAnnual1 = percentage
					.multiply(new BigDecimal(getBhpPhl()))
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			setBhpAnnualValue(bhpAnnual1.toString());

			BigDecimal bhpTotal1 = new BigDecimal(getBhpUpfrontFee()).add(
					bhpAnnual1).setScale(0, BigDecimal.ROUND_HALF_UP);
			setBhpTotal(bhpTotal1);

			System.out.println("percent tahun ke1 = " + percent1
					+ ", BHP Tahunan = " + bhpAnnual1);

			setBhpAnnualValue(bhpAnnual1.toString());
			license.setBhpUpfrontFee(new BigDecimal(getBhpUpfrontFee()));
			license.setBhpRate(new BigDecimal(0));
			license.setBhpCalcIndex(new BigDecimal(0).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			license.setBhpAnnualPercent(new BigDecimal(bhpAnnualRate1));
			license.setBhpAnnualValue(bhpAnnual1);
			license.setBhpTotal(bhpTotal1);
			license.setBhpPhl(new BigDecimal(getBhpPhl()));
			license.setYearTo(new BigDecimal(1));

			try {
				dateBegin.setTime(DateUtil.convertStringToDate("dd MMM yyyy",
						getLicenceBegin()));
				dateEnd.setTime(DateUtil.convertStringToDate("dd MMM yyyy",
						getLicenceEnd()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// dateBegin.add(Calendar.YEAR, 0);
			license.setLicenceBegin(dateBegin.getTime());

			// dateEnd.add(Calendar.YEAR, 0);
			license.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license);
			bhpListVR.add(license);
			// Tahun ke-2
			Licenses license2 = (Licenses) getFields().get("LICENSE2");
			BigDecimal index1 = (new BigDecimal(getBhpRate())
					.divide(new BigDecimal(100))).add(new BigDecimal(1));

			// BigDecimal percent2 = new
			// BigDecimal(getLicenseManager().findBhpAnnualRate(new
			// Integer(2)));
			BigDecimal percent2 = new BigDecimal(bhpAnnualRate2);
			BigDecimal bhpAnnual2 = new BigDecimal(getBhpPhl())
					.multiply(index1).multiply(percent2)
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			System.out.println("percent tahun ke2 = " + percent2
					+ ", Index Pengali = " + index1 + ", BHP Tahunan = "
					+ bhpAnnual2);

			license2.setBhpUpfrontFee(new BigDecimal(0));
			license2.setBhpRate(new BigDecimal(getBhpRate()));
			license2.setBhpCalcIndex(index1);
			license2.setBhpAnnualPercent(percent2);
			license2.setBhpAnnualValue(bhpAnnual2);
			license2.setBhpTotal(bhpAnnual2);
			license2.setBhpPhl(new BigDecimal(getBhpPhl()));
			license2.setYearTo(new BigDecimal(2));
			license2.setBhpPhl(new BigDecimal(getBhpPhl()));
			dateBegin.add(Calendar.YEAR, 1);
			license2.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license2.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license2);

			// Tahun ke-3
			Licenses license3 = (Licenses) getFields().get("LICENSE3");

			BigDecimal index2 = index1.multiply(index1);
			BigDecimal percent3 = new BigDecimal(bhpAnnualRate3);
			BigDecimal bhpAnnual3 = new BigDecimal(getBhpPhl())
					.multiply(index2).multiply(percent3)
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);

			System.out.println("percent tahun ke3 = " + percent3
					+ ", Index Pengali = " + index2 + ", BHP Tahunan = "
					+ bhpAnnual3);
			license3.setBhpUpfrontFee(new BigDecimal(0));
			license3.setBhpRate(new BigDecimal(getBhpRate()));
			license3.setBhpCalcIndex(index2);
			license3.setBhpAnnualPercent(percent3);
			license3.setBhpAnnualValue(bhpAnnual3);
			license3.setBhpTotal(bhpAnnual3);
			license3.setYearTo(new BigDecimal(3));
			license3.setBhpPhl(new BigDecimal(getBhpPhl()));
			dateBegin.add(Calendar.YEAR, 1);
			license3.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license3.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license3);

			// Tahun ke-4
			Licenses license4 = (Licenses) getFields().get("LICENSE4");
			BigDecimal index3 = index2.multiply(index1);
			BigDecimal percent4 = new BigDecimal(bhpAnnualRate4);
			BigDecimal bhpAnnual4 = new BigDecimal(getBhpPhl())
					.multiply(index3).multiply(percent4)
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);

			System.out.println("percent tahun ke4 = " + percent4
					+ ", Index Pengali = " + index3 + ", BHP Tahunan = "
					+ bhpAnnual4);

			license4.setBhpUpfrontFee(new BigDecimal(0));
			license4.setBhpRate(new BigDecimal(getBhpRate()));
			license4.setBhpCalcIndex(index3);
			license4.setBhpAnnualPercent(percent4);
			license4.setBhpAnnualValue(bhpAnnual4);
			license4.setBhpTotal(bhpAnnual4);
			license4.setYearTo(new BigDecimal(4));
			license4.setBhpPhl(new BigDecimal(getBhpPhl()));
			dateBegin.add(Calendar.YEAR, 1);
			license4.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license4.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license4);

			// Tahun ke-5
			Licenses license5 = (Licenses) getFields().get("LICENSE5");
			BigDecimal index4 = index3.multiply(index1);
			BigDecimal percent5 = new BigDecimal(bhpAnnualRate5);
			BigDecimal bhpAnnual5 = new BigDecimal(getBhpPhl())
					.multiply(index4).multiply(percent5)
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			license5.setBhpUpfrontFee(new BigDecimal(0));
			license5.setBhpRate(new BigDecimal(getBhpRate()));
			license5.setBhpCalcIndex(index4);
			license5.setBhpAnnualPercent(percent5);
			license5.setBhpAnnualValue(bhpAnnual5);
			license5.setBhpTotal(bhpAnnual5);
			license5.setYearTo(new BigDecimal(5));
			license5.setBhpPhl(new BigDecimal(getBhpPhl()));
			dateBegin.add(Calendar.YEAR, 1);
			license5.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license5.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license5);

			// Tahun ke-6
			Licenses license6 = (Licenses) getFields().get("LICENSE6");
			BigDecimal index5 = index4.multiply(index1);
			BigDecimal percent6 = new BigDecimal(bhpAnnualRate6);
			BigDecimal bhpAnnual6 = new BigDecimal(getBhpPhl())
					.multiply(index5).multiply(percent6)
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			license6.setBhpUpfrontFee(new BigDecimal(0));
			license6.setBhpRate(new BigDecimal(getBhpRate()));
			license6.setBhpCalcIndex(index5);
			license6.setBhpAnnualPercent(percent6);
			license6.setBhpAnnualValue(bhpAnnual6);
			license6.setBhpTotal(bhpAnnual6);
			license6.setYearTo(new BigDecimal(6));
			license6.setBhpPhl(new BigDecimal(getBhpPhl()));
			dateBegin.add(Calendar.YEAR, 1);
			license6.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license6.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license6);

			// Tahun ke-7
			Licenses license7 = (Licenses) getFields().get("LICENSE7");
			BigDecimal index6 = index5.multiply(index1);
			BigDecimal percent7 = new BigDecimal(bhpAnnualRate7);
			BigDecimal bhpAnnual7 = new BigDecimal(getBhpPhl())
					.multiply(index6).multiply(percent7)
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			license7.setBhpUpfrontFee(new BigDecimal(0));
			license7.setBhpRate(new BigDecimal(getBhpRate()));
			license7.setBhpCalcIndex(index6);
			license7.setBhpAnnualPercent(percent7);
			license7.setBhpAnnualValue(bhpAnnual7);
			license7.setBhpTotal(bhpAnnual7);
			license7.setYearTo(new BigDecimal(7));
			license7.setBhpPhl(new BigDecimal(getBhpPhl()));
			dateBegin.add(Calendar.YEAR, 1);
			license7.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license7.setLicenceEnd(dateEnd.getTime());
			bhpList.add(license7);

			// Tahun ke-8
			Licenses license8 = (Licenses) getFields().get("LICENSE8");
			BigDecimal index7 = index6.multiply(index1);
			BigDecimal percent8 = new BigDecimal(bhpAnnualRate8);
			BigDecimal bhpAnnual8 = new BigDecimal(getBhpPhl())
					.multiply(index7).multiply(percent8)
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			license8.setBhpUpfrontFee(new BigDecimal(0));
			license8.setBhpRate(new BigDecimal(getBhpRate()));
			license8.setBhpCalcIndex(index7);
			license8.setBhpAnnualPercent(percent8);
			license8.setBhpAnnualValue(bhpAnnual8);
			license8.setBhpTotal(bhpAnnual8);
			license8.setYearTo(new BigDecimal(8));
			license8.setBhpPhl(new BigDecimal(getBhpPhl()));
			dateBegin.add(Calendar.YEAR, 1);
			license8.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license8.setLicenceEnd(dateEnd.getTime());
			bhpList.add(license8);

			// Tahun ke-9
			Licenses license9 = (Licenses) getFields().get("LICENSE9");
			BigDecimal index8 = index7.multiply(index1);
			BigDecimal percent9 = new BigDecimal(bhpAnnualRate9);
			BigDecimal bhpAnnual9 = new BigDecimal(getBhpPhl())
					.multiply(index8).multiply(percent9)
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			license9.setBhpUpfrontFee(new BigDecimal(0));
			license9.setBhpRate(new BigDecimal(getBhpRate()));
			license9.setBhpCalcIndex(index8);
			license9.setBhpAnnualPercent(percent9);
			license9.setBhpAnnualValue(bhpAnnual9);
			license9.setBhpTotal(bhpAnnual9);
			license9.setYearTo(new BigDecimal(9));
			license9.setBhpPhl(new BigDecimal(getBhpPhl()));
			dateBegin.add(Calendar.YEAR, 1);
			license9.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license9.setLicenceEnd(dateEnd.getTime());
			bhpList.add(license9);

			// Tahun ke-10
			Licenses license10 = (Licenses) getFields().get("LICENSE10");
			BigDecimal index9 = index8.multiply(index1);
			BigDecimal percent10 = new BigDecimal(bhpAnnualRate10);
			BigDecimal bhpAnnual10 = new BigDecimal(getBhpPhl())
					.multiply(index9).multiply(percent10)
					.divide(new BigDecimal(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			license10.setBhpUpfrontFee(new BigDecimal(0));
			license10.setBhpRate(new BigDecimal(getBhpRate()));
			license10.setBhpCalcIndex(index9);
			license10.setBhpAnnualPercent(percent10);
			license10.setBhpAnnualValue(bhpAnnual10);
			license10.setBhpTotal(bhpAnnual10);
			license10.setYearTo(new BigDecimal(10));
			license10.setBhpPhl(new BigDecimal(getBhpPhl()));
			dateBegin.add(Calendar.YEAR, 1);
			license10.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license10.setLicenceEnd(dateEnd.getTime());
			bhpList.add(license10);

			getFields().put("BHP_LIST", bhpListVR);
			getFields().put("LICENCES_LIST", bhpList);

		} else if (getBhpMethod().equals("FR")) {

			System.out.println("METODE BHP FR = OK ");

			Licenses mainLicense = new Licenses();
			mainLicense.setLicenceNo(getLicenceNo());
			mainLicense.setLicenceId(getLicenceId());
			mainLicense.setServiceId(getServiceId());
			mainLicense.setSubServiceId(getSubServiceId());
			mainLicense.setClientName(getClientName());
			mainLicense.setClientId(new BigDecimal(getClientIDs()));
			try {
				mainLicense.setLicenceBegin(DateUtil.convertStringToDate(
						"dd MMM yyyy", getLicenceBegin()));
				mainLicense.setLicenceEnd(DateUtil.convertStringToDate(
						"dd MMM yyyy", getLicenceEnd()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mainLicense.setTransmitMin(getTransmitMin());
			mainLicense.setTransmitMax(getTransmitMax());
			mainLicense.setReceiveMin(getReceiveMin());
			mainLicense.setReceiveMax(getReceiveMax());

			for (int i = 0; i < 11; i++) {
				getFields().put("LICENSE" + i, new Licenses(mainLicense));
			}

			Calendar dateBegin = Calendar.getInstance();
			Calendar dateEnd = Calendar.getInstance();

			List<Licenses> bhpList = new ArrayList<Licenses>();
			List<Licenses> bhpListVR = new ArrayList<Licenses>();

			System.out.println("UpFrontFee = " + getBhpUpfrontFee());
			System.out.println("PHL = " + getBhpPhl());
			System.out.println("Persentase BHP Tahunan = "
					+ getBhpAnnualPercent());

			// Tahun ke-1
			Licenses license = (Licenses) getFields().get("LICENSE1");
			BigDecimal bhpAnnual1 = null;

			BigDecimal bhpTotal1 = null;

			if (getBhpPhl() == null) {
				bhpAnnual1 = new BigDecimal(getBhpHl());
				setBhpAnnualValue(bhpAnnual1.toString());
				bhpTotal1 = new BigDecimal(getBhpUpfrontFee()).add(
						new BigDecimal(getBhpHl())).setScale(0,
						BigDecimal.ROUND_HALF_UP);
				license.setBhpHl(bhpAnnual1);
			} else {
				bhpAnnual1 = new BigDecimal(getBhpPhl());
				setBhpAnnualValue(bhpAnnual1.toString());
				bhpTotal1 = new BigDecimal(getBhpUpfrontFee()).add(
						new BigDecimal(getBhpPhl())).setScale(0,
						BigDecimal.ROUND_HALF_UP);
				license.setBhpPhl(bhpAnnual1);

			}

			setBhpTotal(bhpTotal1);

			license.setBhpUpfrontFee(new BigDecimal(getBhpUpfrontFee()));
			license.setBhpAnnualValue(bhpAnnual1);
			license.setBhpTotal(bhpTotal1);
			license.setYearTo(new BigDecimal(1));

			try {
				dateBegin.setTime(DateUtil.convertStringToDate("dd MMM yyyy",
						getLicenceBegin()));
				dateEnd.setTime(DateUtil.convertStringToDate("dd MMM yyyy",
						getLicenceEnd()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// dateBegin.add(Calendar.YEAR, 0);
			license.setLicenceBegin(dateBegin.getTime());

			// dateEnd.add(Calendar.YEAR, 0);
			license.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license);
			// Tahun ke-2
			Licenses license2 = (Licenses) getFields().get("LICENSE2");

			BigDecimal bhpAnnual2 = null;

			if (getBhpPhl() == null) {
				bhpAnnual2 = new BigDecimal(getBhpHl());
				// license2.setBhpPhl(new BigDecimal(0));
				license2.setBhpHl(bhpAnnual2);

			} else {
				bhpAnnual2 = new BigDecimal(getBhpPhl());
				license2.setBhpPhl(bhpAnnual2);
				// license2.setBhpHl(new BigDecimal(0));

			}

			license2.setBhpUpfrontFee(new BigDecimal(0));
			license2.setBhpAnnualValue(bhpAnnual2);
			license2.setBhpTotal(bhpAnnual2);
			license2.setYearTo(new BigDecimal(2));

			dateBegin.add(Calendar.YEAR, 1);
			license2.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license2.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license2);

			// Tahun ke-3
			Licenses license3 = (Licenses) getFields().get("LICENSE3");

			BigDecimal bhpAnnual3 = null;

			if (getBhpPhl() == null) {
				bhpAnnual3 = new BigDecimal(getBhpHl());
				// license3.setBhpPhl(new BigDecimal(0));
				license3.setBhpHl(bhpAnnual3);

			} else {
				bhpAnnual3 = new BigDecimal(getBhpPhl());
				license3.setBhpPhl(bhpAnnual3);
				// license3.setBhpHl(new BigDecimal(0));

			}

			license3.setBhpUpfrontFee(new BigDecimal(0));
			license3.setBhpAnnualValue(bhpAnnual3);
			license3.setBhpTotal(bhpAnnual3);
			license3.setYearTo(new BigDecimal(3));

			dateBegin.add(Calendar.YEAR, 1);
			license3.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license3.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license3);

			// Tahun ke-4
			Licenses license4 = (Licenses) getFields().get("LICENSE4");

			BigDecimal bhpAnnual4 = null;

			if (getBhpPhl() == null) {
				bhpAnnual4 = new BigDecimal(getBhpHl());
				// license4.setBhpPhl(new BigDecimal(0));
				license4.setBhpHl(bhpAnnual4);

			} else {
				bhpAnnual4 = new BigDecimal(getBhpPhl());
				license4.setBhpPhl(bhpAnnual4);
				// license4.setBhpHl(new BigDecimal(0));

			}

			license4.setBhpUpfrontFee(new BigDecimal(0));
			license4.setBhpAnnualValue(bhpAnnual4);
			license4.setBhpTotal(bhpAnnual4);
			license4.setYearTo(new BigDecimal(4));

			dateBegin.add(Calendar.YEAR, 1);
			license4.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license4.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license4);

			// Tahun ke-5
			Licenses license5 = (Licenses) getFields().get("LICENSE5");

			BigDecimal bhpAnnual5 = null;

			if (getBhpPhl() == null) {
				bhpAnnual5 = new BigDecimal(getBhpHl());
				// license5.setBhpPhl(new BigDecimal(0));
				license5.setBhpHl(bhpAnnual5);

			} else {
				bhpAnnual5 = new BigDecimal(getBhpPhl());
				license5.setBhpPhl(bhpAnnual5);
				// license5.setBhpHl(new BigDecimal(0));

			}

			license5.setBhpUpfrontFee(new BigDecimal(0));

			license5.setBhpAnnualValue(bhpAnnual5);
			license5.setBhpTotal(bhpAnnual5);
			license5.setYearTo(new BigDecimal(5));
			dateBegin.add(Calendar.YEAR, 1);
			license5.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license5.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license5);

			// Tahun ke-6
			Licenses license6 = (Licenses) getFields().get("LICENSE6");

			BigDecimal bhpAnnual6 = null;

			if (getBhpPhl() == null) {
				bhpAnnual6 = new BigDecimal(getBhpHl());
				// license6.setBhpPhl(new BigDecimal(0));
				license6.setBhpHl(bhpAnnual6);

			} else {
				bhpAnnual6 = new BigDecimal(getBhpPhl());
				license6.setBhpPhl(bhpAnnual6);
				// license6.setBhpHl(new BigDecimal(0));

			}

			license6.setBhpUpfrontFee(new BigDecimal(0));
			license6.setBhpAnnualValue(bhpAnnual6);
			license6.setBhpTotal(bhpAnnual6);
			license6.setYearTo(new BigDecimal(6));
			dateBegin.add(Calendar.YEAR, 1);
			license6.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license6.setLicenceEnd(dateEnd.getTime());

			bhpList.add(license6);

			// Tahun ke-7
			Licenses license7 = (Licenses) getFields().get("LICENSE7");
			BigDecimal bhpAnnual7 = null;

			if (getBhpPhl() == null) {
				bhpAnnual7 = new BigDecimal(getBhpHl());
				// license7.setBhpPhl(new BigDecimal(0));
				license7.setBhpHl(bhpAnnual7);

			} else {
				bhpAnnual7 = new BigDecimal(getBhpPhl());
				license7.setBhpPhl(bhpAnnual7);
				// license7.setBhpHl(new BigDecimal(0));

			}

			license7.setBhpUpfrontFee(new BigDecimal(0));
			license7.setBhpAnnualValue(bhpAnnual7);
			license7.setBhpTotal(bhpAnnual7);
			license7.setYearTo(new BigDecimal(7));
			dateBegin.add(Calendar.YEAR, 1);
			license7.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license7.setLicenceEnd(dateEnd.getTime());
			bhpList.add(license7);

			// Tahun ke-8
			Licenses license8 = (Licenses) getFields().get("LICENSE8");

			BigDecimal bhpAnnual8 = null;

			if (getBhpPhl() == null) {
				bhpAnnual8 = new BigDecimal(getBhpHl());
				// license8.setBhpPhl(new BigDecimal(0));
				license8.setBhpHl(bhpAnnual8);

			} else {
				bhpAnnual8 = new BigDecimal(getBhpPhl());
				license8.setBhpPhl(bhpAnnual8);
				// license8.setBhpHl(new BigDecimal(0));

			}

			license8.setBhpUpfrontFee(new BigDecimal(0));
			license8.setBhpAnnualValue(bhpAnnual8);
			license8.setBhpTotal(bhpAnnual8);
			license8.setYearTo(new BigDecimal(8));
			dateBegin.add(Calendar.YEAR, 1);
			license8.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license8.setLicenceEnd(dateEnd.getTime());
			bhpList.add(license8);

			// Tahun ke-9
			Licenses license9 = (Licenses) getFields().get("LICENSE9");

			BigDecimal bhpAnnual9 = null;

			if (getBhpPhl() == null) {
				bhpAnnual9 = new BigDecimal(getBhpHl());
				// license9.setBhpPhl(new BigDecimal(0));
				license9.setBhpHl(bhpAnnual9);

			} else {
				bhpAnnual9 = new BigDecimal(getBhpPhl());
				license9.setBhpPhl(bhpAnnual9);
				// license9.setBhpHl(new BigDecimal(0));

			}

			license9.setBhpUpfrontFee(new BigDecimal(0));
			license9.setBhpAnnualValue(bhpAnnual9);
			license9.setBhpTotal(bhpAnnual9);
			license9.setYearTo(new BigDecimal(9));
			dateBegin.add(Calendar.YEAR, 1);
			license9.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license9.setLicenceEnd(dateEnd.getTime());
			bhpList.add(license9);

			// Tahun ke-10
			Licenses license10 = (Licenses) getFields().get("LICENSE10");
			BigDecimal bhpAnnual10 = null;

			if (getBhpPhl() == null) {
				bhpAnnual10 = new BigDecimal(getBhpHl());
				// license10.setBhpPhl(new BigDecimal(0));
				license10.setBhpHl(bhpAnnual8);

			} else {
				bhpAnnual10 = new BigDecimal(getBhpPhl());
				license10.setBhpPhl(bhpAnnual10);
				// license10.setBhpHl(new BigDecimal(0));

			}
			license10.setBhpUpfrontFee(new BigDecimal(0));
			license10.setBhpAnnualValue(bhpAnnual10);

			license10.setBhpTotal(bhpAnnual10);
			license10.setYearTo(new BigDecimal(10));
			dateBegin.add(Calendar.YEAR, 1);
			license10.setLicenceBegin(dateBegin.getTime());

			dateEnd.add(Calendar.YEAR, 1);
			license10.setLicenceEnd(dateEnd.getTime());
			bhpList.add(license10);

			getFields().put("BHP_FR_LIST", bhpList);

			getFields().put("LICENCES_LIST", bhpList);

		}
		
		

		getFields().put("BHP_CALC", true);
		setFile(getFile());
		getDelegate().clearErrors();
	}

	public void back(IRequestCycle cycle) {
		getFields().remove("BHP_LIST");
		getFields().remove("BHP_FR_LIST");

		setBhpUpfrontFee(null);
		setBhpHl(null);
		setBhpPhl(null);
		setBhpTotal(null);
	}

	public void hitung(IRequestCycle cycle) {

		String validationMsg = generalValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		if (getFields().get("BHP_UPFRONT") != null) {
			setBhpUpfrontFee(getFields().get("BHP_UPFRONT").toString());
		}
		if (getFields().get("BHP_RATE") != null) {
			setBhpRate(getFields().get("BHP_RATE").toString());
		}
		if (getFields().get("BHP_INDEX") != null) {
			setBhpCalcIndex(getFields().get("BHP_INDEX").toString());
		}
		if (getFields().get("BHP_PHL") != null) {
			setBhpPhl(getFields().get("BHP_PHL").toString());
		}
		if (getLicenses() == null) {
			setLicenses((Licenses) getFields().get("LICENSE"));
		}

		getFields().put("BHP_METHOD", getBhpMethod());
		getFields().put("LICENSE_DATE", getLicenceDate());
		getFields().put("BHP_YEAR", getYearTo());
		getFields().put("BHP_PAYMENT", getBhpPaymentType());
		getFields().put("BHP_DUEDATE", getPaymentDueDate());

		for (int i = 0; i < 11; i++) {
			getFields().put("LICENSE" + i, new Licenses(getLicenses()));
		}

		List<VariableAnnualRate> obj = getVariableAnnualRateManager()
				.findByStatus(1);

		System.out.println("BHP Rate : " + obj.get(0).getRateValue());

		CalculateBHP calculate = (CalculateBHP) cycle.getPage("calculateBhp");
		calculate.setFields(getFields());
		calculate.setLicenseNumber(getLicenceNo());
		calculate.setBhpUpfrontFee(getBhpUpfrontFee());
		calculate.setBhpRate(obj.get(0).getRateValue().toString());
		// calculate.setBhpRate(getBhpRate());
		calculate.setBhpCalcIndex(getBhpCalcIndex());
		calculate.setBhpPhl(getBhpPhl());
		cycle.activate(calculate);
	}

}
