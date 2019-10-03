package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.mastermaintenance.service.Services;
import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.service.mastermaintenance.license.LicenseManager;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;

/**
 * @author <a href="mailto:hendy.yusprasetya@sigma.co.id">Yusprasetya</a>
 */
public abstract class CalculateBHP extends AdminBasePage implements
		PageBeginRenderListener {

	public abstract Licenses getLicenses();

	public abstract void setLicenses(Licenses licenses);

	public abstract String getBhpUpfrontFee();

	public abstract void setBhpUpfrontFee(String bhpUpfrontFee);

	public abstract String getBhpRate();

	public abstract void setBhpRate(String bhpRate);

	public abstract String getBhpCalcIndex();

	public abstract void setBhpCalcIndex(String bhpCalcIndex);

	public abstract String getBhpPhl();

	public abstract void setBhpPhl(String bhpPhl);

	public abstract String getBhpAnnualPercent();

	public abstract void setBhpAnnualPercent(String bhpAnnualPercent);

	public abstract String getBhpAnnualValue();

	public abstract void setBhpAnnualValue(String bhpAnnualValue);

	public abstract String getBhpTotal();

	public abstract void setBhpTotal(String bhpTotal);

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract String getLicenseNumber();

	public abstract void setLicenseNumber(String licenseNumber);

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();

	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	@InjectObject("spring:servicesManager")
	public abstract ServicesManager getServiceManager();

	@InjectObject("spring:subServicesManager")
	public abstract SubServicesManager getSubServicesManager();

	protected final Log log = LogFactory.getLog(CalculateBHP.class);

	private static final String YEAR = "1";

	public abstract Licenses getRow1();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		Integer bhpRate = new Integer(0);
		Double bhpRates = new Double(0);

		Integer multiplyIndex = new Integer(0);
		Integer bhpAnnualRate = new Integer(0);

//		if (!pageEvent.getRequestCycle().isRewinding()) {
//			if (!isNotFirstLoad()) {
//				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				}

				// bhpAnnualRate =
				// getLicenseManager().findBhpAnnualRate(Integer.parseInt(YEAR));

				List<VariableAnnualPercentage> annualpercentage = getVariableManager()
						.findByStatus(1);
				Long annualPercentId = annualpercentage.get(0)
						.getAnnualPercentId();

				List<VariableAnnualPercentageDetail> annualPercentageList = getVariableDetailManager()
						.findByAnnualPercentId(annualPercentId);

				for (VariableAnnualPercentageDetail annualPercentage : annualPercentageList) {

					System.out.println("tahun pertama = "
							+ annualPercentage.getYearTo() + ", percentage = "
							+ annualPercentage.getPercentage().intValue());

					if (annualPercentage.getYearTo() == 1) {
						bhpAnnualRate = annualPercentage.getPercentage()
								.intValue();
					}

				}

				getFields().put("BHP_ANNUAL", bhpAnnualRate);

				if (getBhpCalcIndex() == null) {
					setBhpCalcIndex(multiplyIndex.toString());
				}
				setBhpAnnualPercent(bhpAnnualRate.toString());

//			} else {
				if (getFields() != null) {
					if (getFields().get("BHP_RATE") != null) {
						bhpRates = Double.parseDouble(getFields().get(
								"BHP_RATE").toString());
						setBhpRate(bhpRates.toString());
						// System.out.println();
					}

					if (getFields().get("BHP_ANNUAL") != null) {
						bhpAnnualRate = Integer.parseInt(getFields().get(
								"BHP_ANNUAL").toString());
					}
				}
//			}
//		}
	}

	public void hitungBhp(IRequestCycle cycle) {
		String validationMsg = generalValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		Calendar dateBegin = Calendar.getInstance();
		Calendar dateEnd = Calendar.getInstance();

		List<Licenses> bhpList = new ArrayList<Licenses>();

		System.out.println("Nilai PHL = "+getBhpPhl());
		System.out.println("NIlai upfrontfee =  "+getBhpUpfrontFee());

		// Tahun ke-1
		Licenses license = (Licenses) getFields().get("LICENSE1");
		BigDecimal bhpAnnual1 = new BigDecimal(
				Integer.parseInt(getBhpAnnualPercent()))
				.multiply(new BigDecimal(getBhpPhl()))
				.divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
		setBhpAnnualValue(bhpAnnual1.toString());

		BigDecimal bhpTotal1 = new BigDecimal(getBhpUpfrontFee()).add(
				bhpAnnual1).setScale(0, BigDecimal.ROUND_HALF_UP);
		setBhpTotal(bhpTotal1.toString());

		setBhpAnnualValue(bhpAnnual1.toString());
		license.setBhpUpfrontFee(new BigDecimal(getBhpUpfrontFee()));
		license.setBhpRate(new BigDecimal(0));
		license.setBhpCalcIndex(new BigDecimal(0).setScale(2,
				BigDecimal.ROUND_HALF_UP));
		license.setBhpAnnualPercent(new BigDecimal(getBhpAnnualPercent()));
		license.setBhpAnnualValue(bhpAnnual1);
		license.setBhpTotal(bhpTotal1);
		license.setBhpPhl(new BigDecimal(getBhpPhl()));
		license.setYearTo(new BigDecimal(1));

		dateBegin.setTime(license.getLicenceBegin());
		dateBegin.add(Calendar.YEAR, 0);
		license.setLicenceBegin(dateBegin.getTime());

		dateEnd.setTime(license.getLicenceEnd());
		dateEnd.add(Calendar.YEAR, 0);
		license.setLicenceEnd(dateEnd.getTime());

		bhpList.add(license);

		// Tahun ke-2
		Licenses license2 = (Licenses) getFields().get("LICENSE2");
		BigDecimal index1 = (new BigDecimal(getBhpRate())
				.divide(new BigDecimal(100))).add(new BigDecimal(1));

		List<VariableAnnualPercentage> annualpercentage = getVariableManager()
				.findByStatus(1);
		Long annualPercentId = annualpercentage.get(0).getAnnualPercentId();

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
				.findByAnnualPercentId(annualPercentId);
		for (VariableAnnualPercentageDetail annualPercentage : annualPercentageList) {

			System.out.println("tahun  = " + annualPercentage.getYearTo()
					+ ", percentage = "
					+ annualPercentage.getPercentage().intValue());

			if (annualPercentage.getYearTo() == 2) {
				bhpAnnualRate2 = annualPercentage.getPercentage().intValue();
			} else if (annualPercentage.getYearTo() == 3) {
				bhpAnnualRate3 = annualPercentage.getPercentage().intValue();
			} else if (annualPercentage.getYearTo() == 4) {
				bhpAnnualRate4 = annualPercentage.getPercentage().intValue();
			} else if (annualPercentage.getYearTo() == 5) {
				bhpAnnualRate5 = annualPercentage.getPercentage().intValue();
			} else if (annualPercentage.getYearTo() == 6) {
				bhpAnnualRate6 = annualPercentage.getPercentage().intValue();
			} else if (annualPercentage.getYearTo() == 7) {
				bhpAnnualRate7 = annualPercentage.getPercentage().intValue();
			} else if (annualPercentage.getYearTo() == 8) {
				bhpAnnualRate8 = annualPercentage.getPercentage().intValue();
			} else if (annualPercentage.getYearTo() == 9) {
				bhpAnnualRate9 = annualPercentage.getPercentage().intValue();
			} else if (annualPercentage.getYearTo() == 10) {
				bhpAnnualRate10 = annualPercentage.getPercentage().intValue();
			}

		}

		// BigDecimal percent2 = new
		// BigDecimal(getLicenseManager().findBhpAnnualRate(new Integer(2)));
		BigDecimal percent2 = new BigDecimal(bhpAnnualRate2);
		BigDecimal bhpAnnual2 = new BigDecimal(getBhpPhl()).multiply(index1)
				.multiply(percent2).divide(new BigDecimal(100))
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
		BigDecimal bhpAnnual3 = new BigDecimal(getBhpPhl()).multiply(index2)
				.multiply(percent3).divide(new BigDecimal(100))
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
		BigDecimal bhpAnnual4 = new BigDecimal(getBhpPhl()).multiply(index3)
				.multiply(percent4).divide(new BigDecimal(100))
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
		BigDecimal bhpAnnual5 = new BigDecimal(getBhpPhl()).multiply(index4)
				.multiply(percent5).divide(new BigDecimal(100))
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
		BigDecimal bhpAnnual6 = new BigDecimal(getBhpPhl()).multiply(index5)
				.multiply(percent6).divide(new BigDecimal(100))
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
		BigDecimal bhpAnnual7 = new BigDecimal(getBhpPhl()).multiply(index6)
				.multiply(percent7).divide(new BigDecimal(100))
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
		BigDecimal bhpAnnual8 = new BigDecimal(getBhpPhl()).multiply(index7)
				.multiply(percent8).divide(new BigDecimal(100))
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
		BigDecimal bhpAnnual9 = new BigDecimal(getBhpPhl()).multiply(index8)
				.multiply(percent9).divide(new BigDecimal(100))
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
		BigDecimal bhpAnnual10 = new BigDecimal(getBhpPhl()).multiply(index9)
				.multiply(percent10).divide(new BigDecimal(100))
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

		getFields().put("BHP_LIST", bhpList);
		getFields().put("BHP_CALC", true);
		getDelegate().clearErrors();
	}

	public List getBhpList() {
		List<Licenses> bhpList = (List<Licenses>) getFields().get("BHP_LIST");
		return bhpList;
	}

	private String generalValidation() {

		String errorMessage = null;
		Pattern alfa = Pattern.compile("[a-zA-Z]");

		if (getBhpUpfrontFee() == null) {
			errorMessage = getText("calculateBhp.error.empty.upfrontFee");
		} else if (alfa.matcher(getBhpUpfrontFee()).find()) {
			errorMessage = getText("calculateBhp.error.numerik.upfrontFee");
		} else if (getBhpRate() == null) {
			errorMessage = getText("calculateBhp.error.empty.rate");
		} else if (alfa.matcher(getBhpRate()).find()) {
			errorMessage = getText("calculateBhp.error.numerik.rate");
		} else if (getBhpCalcIndex().equals("0")) {
			errorMessage = getText("calculateBhp.error.zero.index");
		} else if (alfa.matcher(getBhpCalcIndex()).find()) {
			errorMessage = getText("calculateBhp.error.numerik.index");
		} else if (getBhpPhl() == null) {
			errorMessage = getText("calculateBhp.error.empty.phl");
		} else if (alfa.matcher(getBhpPhl()).find()) {
			errorMessage = getText("calculateBhp.error.numerik.phl");
		} else if (getBhpAnnualPercent() == null) {
			errorMessage = getText("calculateBhp.error.empty.persentaseBhp");
		} else if (alfa.matcher(getBhpAnnualPercent()).find()) {
			errorMessage = getText("calculateBhp.error.numerik.persentaseBhp");
		} else {
			errorMessage = null;
		}
		return errorMessage;
	}

	public void refresh(IRequestCycle cycle) {
		getDelegate().clearErrors();
		if (cycle.isRewinding()) {
			if (getBhpRate() != null) {
				getFields().put("BHP_RATE", getBhpRate());
			}
		}
	}

	public void back(IRequestCycle cycle) {
		if (getFields().get("LICENSE_EDIT") != null) {
			Licenses license = (Licenses) getFields().get("LICENSE1");
			LicenseEdit licenseEdit = (LicenseEdit) cycle.getPage("licenseEdit");
//			getFields().remove("BHP_LIST");
			setValue(licenseEdit, license);
			cycle.activate(licenseEdit);
		} else {
			LicenseCreate licenseCreate = (LicenseCreate) cycle.getPage("licenseCreate");
			Licenses license = (Licenses) getFields().get("LICENSE1");
//			getFields().remove("BHP_LIST");
			setValue(licenseCreate, license);
			cycle.activate(licenseCreate);
		}
	}

	public void setValue(LicenseCreate licenseCreate, Licenses license) {
		licenseCreate.setBhpTotal(license.getBhpTotal());
		licenseCreate.setLicenceNo(license.getLicenceNo());
		licenseCreate.setLicenceId(license.getLicenceId());
		licenseCreate.setServiceId(license.getServiceId());
		licenseCreate.setSubServiceId(license.getSubServiceId());
		licenseCreate.setClientName(license.getClientName());
		licenseCreate.setClientIDs(license.getClientId().toString());
		if (license.getLicenceBegin() != null) {
			licenseCreate.setLicenceBegin(DateUtil.convertDateToString(
					license.getLicenceBegin(), "dd MMM yyyy"));
		}
		if (license.getLicenceEnd() != null) {
			licenseCreate.setLicenceEnd(DateUtil.convertDateToString(
					license.getLicenceEnd(), "dd MMM yyyy"));
		}
		licenseCreate.setTransmitMin(license.getTransmitMin());
		licenseCreate.setTransmitMax(license.getTransmitMax());
		licenseCreate.setReceiveMin(license.getReceiveMin());
		licenseCreate.setReceiveMax(license.getReceiveMax());
		licenseCreate.setBhpUpfrontFee(getBhpUpfrontFee());
		licenseCreate.setBhpRate(getBhpRate());
		licenseCreate.setBhpCalcIndex(getBhpCalcIndex());
		licenseCreate.setBhpPhl(getBhpPhl());

		if (getFields().get("BHP_METHOD") != null) {
			licenseCreate
					.setBhpMethod(getFields().get("BHP_METHOD").toString());
		}
		if (getFields().get("LICENSE_DATE") != null) {
			licenseCreate
					.setLicenceDate((Date) getFields().get("LICENSE_DATE"));
		}
		if (getFields().get("BHP_YEAR") != null) {
			licenseCreate.setYearTo(new BigDecimal(getFields().get("BHP_YEAR")
					.toString()));
		}
		if (getFields().get("BHP_PAYMENT") != null) {
			licenseCreate.setBhpPaymentType(getFields().get("BHP_PAYMENT")
					.toString());
		}
		if (getFields().get("BHP_DUEDATE") != null) {
			licenseCreate.setPaymentDueDate((Date) getFields().get(
					"BHP_DUEDATE"));
		}

		licenseCreate.setFields(getFields());
	}

	public void setValue(LicenseEdit licenseEdit, Licenses license) {
		Services service = getServiceManager().findServicesById(
				license.getServiceId().longValue());
		SubServices subServices = getSubServicesManager().findSubServicesById(
				license.getSubServiceId().longValue());

		licenseEdit.setLicenses(license);
		licenseEdit.setBsLicenceId(license.getBsLicenceId());
		licenseEdit.setServiceId(license.getServiceId());
		licenseEdit.setServiceName(service.getServiceName());
		licenseEdit.setSubServiceId(license.getSubServiceId());
		licenseEdit.setSubServicesName(subServices.getSubserviceName());
		licenseEdit.setLicenceNo(license.getLicenceNo());
		licenseEdit.setLicenceId(license.getLicenceId());
		licenseEdit.setBhpMethod(license.getBhpMethod());
		licenseEdit.setClientID(license.getClientId());
		licenseEdit.setClientName(license.getClientName());
		licenseEdit.setLicenceDate(license.getLicenceDate());
		licenseEdit.setLicenceBegin(license.getLicenceBegin());
		licenseEdit.setLicenceEnd(license.getLicenceEnd());
		licenseEdit.setYearTo(license.getYearTo());
		licenseEdit.setTransmitMin(license.getTransmitMin());
		licenseEdit.setTransmitMax(license.getTransmitMax());
		licenseEdit.setReceiveMin(license.getReceiveMin());
		licenseEdit.setReceiveMax(license.getReceiveMax());
		licenseEdit.setBhpPaymentType(license.getBhpPaymentType());
		//licenseEdit.setPaymentDueDate(license.getPaymentDueDate());
		licenseEdit.setBhpTotal(license.getBhpTotal());

		licenseEdit.setFields(getFields());
	}

	public void submit(IRequestCycle cycle) {
		if (getFields().get("LICENSE_EDIT") != null) {
			Licenses license = (Licenses) getFields().get("LICENSE1");
			LicenseEdit licenseEdit = (LicenseEdit) cycle.getPage("licenseEdit");
//			getFields().remove("BHP_LIST");
			setValue(licenseEdit, license);
			cycle.activate(licenseEdit);
		} else {
			Licenses license = (Licenses) getFields().get("LICENSE1");
			LicenseCreate licenseCreate = (LicenseCreate) cycle.getPage("licenseCreate");
//			getFields().remove("BHP_LIST");
			setValue(licenseCreate, license);
			cycle.activate(licenseCreate);
		}
	}

	public void calculateIndex(IRequestCycle cycle) {
		// if (getBhpRate() != null) {

		List<VariableAnnualRate> obj = getVariableAnnualRateManager()
				.findByStatus(1);

		System.out.println("BHP rate : " + getBhpRate());
		BigDecimal multiplyIndex = (new BigDecimal(getBhpRate())
				.divide(new BigDecimal(100))).add(new BigDecimal(1));

		// Long x = multiplyIndex.longValue();
		System.out.println("big decimal multiplyIndex = " + multiplyIndex);

		setBhpCalcIndex(multiplyIndex.toString());

		getFields().put("BHP_RATE", getBhpRate());
		getFields().put("BHP_INDEX", getBhpCalcIndex());
		// }

		// else {
		//
		// setBhpRate("0");
		// getFields().put("BHP_RATE", getBhpRate());
		// setBhpCalcIndex("0");
		// getFields().put("BHP_INDEX", getBhpCalcIndex());
		// }
		getFields().put("BHP_UPFRONT", getBhpUpfrontFee());
		getFields().put("BHP_PHL", getBhpPhl());
	}

	// public static void main(String[] args) {
	// List test = new ArrayList();
	// Integer dengkul = new Integer(5);
	// test.add(dengkul);
	// dengkul = 10;
	// test.add(dengkul);
	// System.out.println(test.get(0).toString());
	// System.out.println(test.get(1).toString());
	// }
}
