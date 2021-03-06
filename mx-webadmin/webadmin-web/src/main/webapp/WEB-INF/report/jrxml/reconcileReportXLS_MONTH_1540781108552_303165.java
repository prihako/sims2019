/*
 * Generated by JasperReports - 10/29/18 9:45 AM
 */
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.*;

import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
import java.net.*;

import net.sf.jasperreports.engine.*;
import java.util.*;
import net.sf.jasperreports.engine.data.*;


/**
 *
 */
public class reconcileReportXLS_MONTH_1540781108552_303165 extends JREvaluator
{


    /**
     *
     */
    private JRFillParameter parameter_unconfirmed = null;
    private JRFillParameter parameter_REPORT_CONNECTION = null;
    private JRFillParameter parameter_REPORT_TIME_ZONE = null;
    private JRFillParameter parameter_REPORT_TEMPLATES = null;
    private JRFillParameter parameter_bankName = null;
    private JRFillParameter parameter_amountSettled = null;
    private JRFillParameter parameter_REPORT_MAX_COUNT = null;
    private JRFillParameter parameter_reconcileStatus = null;
    private JRFillParameter parameter_title = null;
    private JRFillParameter parameter_REPORT_SCRIPTLET = null;
    private JRFillParameter parameter_trxStartDate = null;
    private JRFillParameter parameter_REPORT_PARAMETERS_MAP = null;
    private JRFillParameter parameter_REPORT_RESOURCE_BUNDLE = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_trxDate = null;
    private JRFillParameter parameter_channelCode = null;
    private JRFillParameter parameter_IS_IGNORE_PAGINATION = null;
    private JRFillParameter parameter_settled = null;
    private JRFillParameter parameter_REPORT_LOCALE = null;
    private JRFillParameter parameter_SEA = null;
    private JRFillParameter parameter_REPORT_FORMAT_FACTORY = null;
    private JRFillParameter parameter_REPORT_CLASS_LOADER = null;
    private JRFillParameter parameter_REPORT_URL_HANDLER_FACTORY = null;
    private JRFillParameter parameter_REPORT_VIRTUALIZER = null;
    private JRFillParameter parameter_notSettled = null;
    private JRFillParameter parameter_trxEndDate = null;
    private JRFillParameter parameter_headerImg = null;
    private JRFillField field_simsStatus = null;
    private JRFillField field_trxAmount = null;
    private JRFillField field_clientId = null;
    private JRFillField field_clientName = null;
    private JRFillField field_invoiceDendaNo = null;
    private JRFillField field_bankBranch = null;
    private JRFillField field_statusDenda = null;
    private JRFillField field_paymentDateSims = null;
    private JRFillField field_bankName = null;
    private JRFillField field_billerRc = null;
    private JRFillField field_rawData = null;
    private JRFillField field_transactionName = null;
    private JRFillField field_reconcileStatus = null;
    private JRFillField field_transactionTime = null;
    private JRFillField field_trxId = null;
    private JRFillField field_invoiceDueDate = null;
    private JRFillField field_paymentChannel = null;
    private JRFillField field_delivChannel = null;
    private JRFillField field_channelRc = null;
    private JRFillField field_invoiceNo = null;
    private JRFillField field_mt940Status = null;
    private JRFillVariable variable_PAGE_NUMBER = null;
    private JRFillVariable variable_COLUMN_NUMBER = null;
    private JRFillVariable variable_REPORT_COUNT = null;
    private JRFillVariable variable_PAGE_COUNT = null;
    private JRFillVariable variable_COLUMN_COUNT = null;


    /**
     *
     */
    public void customizedInit(
        Map pm,
        Map fm,
        Map vm
        )
    {
        initParams(pm);
        initFields(fm);
        initVars(vm);
    }


    /**
     *
     */
    private void initParams(Map pm)
    {
        parameter_unconfirmed = (JRFillParameter)pm.get("unconfirmed");
        parameter_REPORT_CONNECTION = (JRFillParameter)pm.get("REPORT_CONNECTION");
        parameter_REPORT_TIME_ZONE = (JRFillParameter)pm.get("REPORT_TIME_ZONE");
        parameter_REPORT_TEMPLATES = (JRFillParameter)pm.get("REPORT_TEMPLATES");
        parameter_bankName = (JRFillParameter)pm.get("bankName");
        parameter_amountSettled = (JRFillParameter)pm.get("amountSettled");
        parameter_REPORT_MAX_COUNT = (JRFillParameter)pm.get("REPORT_MAX_COUNT");
        parameter_reconcileStatus = (JRFillParameter)pm.get("reconcileStatus");
        parameter_title = (JRFillParameter)pm.get("title");
        parameter_REPORT_SCRIPTLET = (JRFillParameter)pm.get("REPORT_SCRIPTLET");
        parameter_trxStartDate = (JRFillParameter)pm.get("trxStartDate");
        parameter_REPORT_PARAMETERS_MAP = (JRFillParameter)pm.get("REPORT_PARAMETERS_MAP");
        parameter_REPORT_RESOURCE_BUNDLE = (JRFillParameter)pm.get("REPORT_RESOURCE_BUNDLE");
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)pm.get("REPORT_DATA_SOURCE");
        parameter_trxDate = (JRFillParameter)pm.get("trxDate");
        parameter_channelCode = (JRFillParameter)pm.get("channelCode");
        parameter_IS_IGNORE_PAGINATION = (JRFillParameter)pm.get("IS_IGNORE_PAGINATION");
        parameter_settled = (JRFillParameter)pm.get("settled");
        parameter_REPORT_LOCALE = (JRFillParameter)pm.get("REPORT_LOCALE");
        parameter_SEA = (JRFillParameter)pm.get("SEA");
        parameter_REPORT_FORMAT_FACTORY = (JRFillParameter)pm.get("REPORT_FORMAT_FACTORY");
        parameter_REPORT_CLASS_LOADER = (JRFillParameter)pm.get("REPORT_CLASS_LOADER");
        parameter_REPORT_URL_HANDLER_FACTORY = (JRFillParameter)pm.get("REPORT_URL_HANDLER_FACTORY");
        parameter_REPORT_VIRTUALIZER = (JRFillParameter)pm.get("REPORT_VIRTUALIZER");
        parameter_notSettled = (JRFillParameter)pm.get("notSettled");
        parameter_trxEndDate = (JRFillParameter)pm.get("trxEndDate");
        parameter_headerImg = (JRFillParameter)pm.get("headerImg");
    }


    /**
     *
     */
    private void initFields(Map fm)
    {
        field_simsStatus = (JRFillField)fm.get("simsStatus");
        field_trxAmount = (JRFillField)fm.get("trxAmount");
        field_clientId = (JRFillField)fm.get("clientId");
        field_clientName = (JRFillField)fm.get("clientName");
        field_invoiceDendaNo = (JRFillField)fm.get("invoiceDendaNo");
        field_bankBranch = (JRFillField)fm.get("bankBranch");
        field_statusDenda = (JRFillField)fm.get("statusDenda");
        field_paymentDateSims = (JRFillField)fm.get("paymentDateSims");
        field_bankName = (JRFillField)fm.get("bankName");
        field_billerRc = (JRFillField)fm.get("billerRc");
        field_rawData = (JRFillField)fm.get("rawData");
        field_transactionName = (JRFillField)fm.get("transactionName");
        field_reconcileStatus = (JRFillField)fm.get("reconcileStatus");
        field_transactionTime = (JRFillField)fm.get("transactionTime");
        field_trxId = (JRFillField)fm.get("trxId");
        field_invoiceDueDate = (JRFillField)fm.get("invoiceDueDate");
        field_paymentChannel = (JRFillField)fm.get("paymentChannel");
        field_delivChannel = (JRFillField)fm.get("delivChannel");
        field_channelRc = (JRFillField)fm.get("channelRc");
        field_invoiceNo = (JRFillField)fm.get("invoiceNo");
        field_mt940Status = (JRFillField)fm.get("mt940Status");
    }


    /**
     *
     */
    private void initVars(Map vm)
    {
        variable_PAGE_NUMBER = (JRFillVariable)vm.get("PAGE_NUMBER");
        variable_COLUMN_NUMBER = (JRFillVariable)vm.get("COLUMN_NUMBER");
        variable_REPORT_COUNT = (JRFillVariable)vm.get("REPORT_COUNT");
        variable_PAGE_COUNT = (JRFillVariable)vm.get("PAGE_COUNT");
        variable_COLUMN_COUNT = (JRFillVariable)vm.get("COLUMN_COUNT");
    }


    /**
     *
     */
    public Object evaluate(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 0 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=0$
                break;
            }
            case 1 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=1$
                break;
            }
            case 2 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=2$
                break;
            }
            case 3 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=3$
                break;
            }
            case 4 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=4$
                break;
            }
            case 5 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=5$
                break;
            }
            case 6 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=6$
                break;
            }
            case 7 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=7$
                break;
            }
            case 8 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_headerImg.getValue())+"/kominfo_2.png");//$JR_EXPR_ID=8$
                break;
            }
            case 9 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.String)("Total Settled");//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_settled.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.String)("Total Unsettled");//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_notSettled.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.String)("Total Need Confirmation");//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_unconfirmed.getValue()));//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=23$
                break;
            }
            case 24 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=24$
                break;
            }
            case 25 : 
            {
                value = (java.lang.String)("Total Invoice");//$JR_EXPR_ID=25$
                break;
            }
            case 26 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=26$
                break;
            }
            case 27 : 
            {
                value = (java.lang.String)(""+(Integer.parseInt(((java.lang.String)parameter_settled.getValue()))+Integer.parseInt(((java.lang.String)parameter_notSettled.getValue()))+Integer.parseInt(((java.lang.String)parameter_unconfirmed.getValue()))));//$JR_EXPR_ID=27$
                break;
            }
            case 28 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue()==1));//$JR_EXPR_ID=28$
                break;
            }
            case 29 : 
            {
                value = (java.lang.String)("Total Nilai Invoice Settled ");//$JR_EXPR_ID=29$
                break;
            }
            case 30 : 
            {
                value = (java.lang.String)(": "+ new java.text.DecimalFormat("Rp #,###,###").format(Double.valueOf(((java.lang.String)parameter_amountSettled.getValue()))));//$JR_EXPR_ID=30$
                break;
            }
            case 31 : 
            {
                value = (java.lang.String)("Tanggal "+((java.lang.String)parameter_trxStartDate.getValue())+" sampai dengan "+((java.lang.String)parameter_trxEndDate.getValue()));//$JR_EXPR_ID=31$
                break;
            }
            case 32 : 
            {
                value = (java.lang.String)("Laporan Rekonsiliasi Mingguan");//$JR_EXPR_ID=32$
                break;
            }
            case 33 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_REPORT_COUNT.getValue()).intValue()==0));//$JR_EXPR_ID=33$
                break;
            }
            case 34 : 
            {
                value = (java.lang.String)(((java.lang.String)field_invoiceNo.getValue()));//$JR_EXPR_ID=34$
                break;
            }
            case 35 : 
            {
                value = (java.lang.Integer)(((java.lang.Integer)variable_REPORT_COUNT.getValue()));//$JR_EXPR_ID=35$
                break;
            }
            case 36 : 
            {
                value = (java.lang.String)(((java.lang.String)field_clientId.getValue()));//$JR_EXPR_ID=36$
                break;
            }
            case 37 : 
            {
                value = (java.lang.String)(((java.lang.String)field_clientName.getValue()).length()>50?((java.lang.String)field_clientName.getValue()).substring(0, 50):((java.lang.String)field_clientName.getValue()));//$JR_EXPR_ID=37$
                break;
            }
            case 38 : 
            {
                value = (java.lang.String)(((java.lang.String)field_simsStatus.getValue()));//$JR_EXPR_ID=38$
                break;
            }
            case 39 : 
            {
                value = (java.lang.String)(((java.lang.String)field_transactionTime.getValue()));//$JR_EXPR_ID=39$
                break;
            }
            case 40 : 
            {
                value = (java.lang.String)(((java.lang.String)field_reconcileStatus.getValue()));//$JR_EXPR_ID=40$
                break;
            }
            case 41 : 
            {
                value = (java.lang.String)(((java.lang.String)field_billerRc.getValue()));//$JR_EXPR_ID=41$
                break;
            }
            case 42 : 
            {
                value = (java.lang.String)(((java.lang.String)field_paymentDateSims.getValue()));//$JR_EXPR_ID=42$
                break;
            }
            case 43 : 
            {
                value = (java.lang.String)(((java.lang.String)field_mt940Status.getValue()));//$JR_EXPR_ID=43$
                break;
            }
            case 44 : 
            {
                value = (java.lang.String)(((java.lang.String)field_paymentChannel.getValue()));//$JR_EXPR_ID=44$
                break;
            }
            case 45 : 
            {
                value = (java.lang.String)(((java.lang.String)field_invoiceDueDate.getValue()));//$JR_EXPR_ID=45$
                break;
            }
            case 46 : 
            {
                value = (java.lang.String)(((java.lang.String)field_trxAmount.getValue()));//$JR_EXPR_ID=46$
                break;
            }
            case 47 : 
            {
                value = (java.lang.String)(((java.lang.String)field_bankName.getValue()));//$JR_EXPR_ID=47$
                break;
            }
            case 48 : 
            {
                value = (java.lang.String)(((java.lang.String)field_bankBranch.getValue()));//$JR_EXPR_ID=48$
                break;
            }
            case 49 : 
            {
                value = (java.lang.String)(((java.lang.String)field_rawData.getValue()));//$JR_EXPR_ID=49$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


    /**
     *
     */
    public Object evaluateOld(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 0 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=0$
                break;
            }
            case 1 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=1$
                break;
            }
            case 2 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=2$
                break;
            }
            case 3 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=3$
                break;
            }
            case 4 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=4$
                break;
            }
            case 5 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=5$
                break;
            }
            case 6 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=6$
                break;
            }
            case 7 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=7$
                break;
            }
            case 8 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_headerImg.getValue())+"/kominfo_2.png");//$JR_EXPR_ID=8$
                break;
            }
            case 9 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.String)("Total Settled");//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_settled.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.String)("Total Unsettled");//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_notSettled.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.String)("Total Need Confirmation");//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_unconfirmed.getValue()));//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=23$
                break;
            }
            case 24 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=24$
                break;
            }
            case 25 : 
            {
                value = (java.lang.String)("Total Invoice");//$JR_EXPR_ID=25$
                break;
            }
            case 26 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=26$
                break;
            }
            case 27 : 
            {
                value = (java.lang.String)(""+(Integer.parseInt(((java.lang.String)parameter_settled.getValue()))+Integer.parseInt(((java.lang.String)parameter_notSettled.getValue()))+Integer.parseInt(((java.lang.String)parameter_unconfirmed.getValue()))));//$JR_EXPR_ID=27$
                break;
            }
            case 28 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue()==1));//$JR_EXPR_ID=28$
                break;
            }
            case 29 : 
            {
                value = (java.lang.String)("Total Nilai Invoice Settled ");//$JR_EXPR_ID=29$
                break;
            }
            case 30 : 
            {
                value = (java.lang.String)(": "+ new java.text.DecimalFormat("Rp #,###,###").format(Double.valueOf(((java.lang.String)parameter_amountSettled.getValue()))));//$JR_EXPR_ID=30$
                break;
            }
            case 31 : 
            {
                value = (java.lang.String)("Tanggal "+((java.lang.String)parameter_trxStartDate.getValue())+" sampai dengan "+((java.lang.String)parameter_trxEndDate.getValue()));//$JR_EXPR_ID=31$
                break;
            }
            case 32 : 
            {
                value = (java.lang.String)("Laporan Rekonsiliasi Mingguan");//$JR_EXPR_ID=32$
                break;
            }
            case 33 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_REPORT_COUNT.getOldValue()).intValue()==0));//$JR_EXPR_ID=33$
                break;
            }
            case 34 : 
            {
                value = (java.lang.String)(((java.lang.String)field_invoiceNo.getOldValue()));//$JR_EXPR_ID=34$
                break;
            }
            case 35 : 
            {
                value = (java.lang.Integer)(((java.lang.Integer)variable_REPORT_COUNT.getOldValue()));//$JR_EXPR_ID=35$
                break;
            }
            case 36 : 
            {
                value = (java.lang.String)(((java.lang.String)field_clientId.getOldValue()));//$JR_EXPR_ID=36$
                break;
            }
            case 37 : 
            {
                value = (java.lang.String)(((java.lang.String)field_clientName.getOldValue()).length()>50?((java.lang.String)field_clientName.getOldValue()).substring(0, 50):((java.lang.String)field_clientName.getOldValue()));//$JR_EXPR_ID=37$
                break;
            }
            case 38 : 
            {
                value = (java.lang.String)(((java.lang.String)field_simsStatus.getOldValue()));//$JR_EXPR_ID=38$
                break;
            }
            case 39 : 
            {
                value = (java.lang.String)(((java.lang.String)field_transactionTime.getOldValue()));//$JR_EXPR_ID=39$
                break;
            }
            case 40 : 
            {
                value = (java.lang.String)(((java.lang.String)field_reconcileStatus.getOldValue()));//$JR_EXPR_ID=40$
                break;
            }
            case 41 : 
            {
                value = (java.lang.String)(((java.lang.String)field_billerRc.getOldValue()));//$JR_EXPR_ID=41$
                break;
            }
            case 42 : 
            {
                value = (java.lang.String)(((java.lang.String)field_paymentDateSims.getOldValue()));//$JR_EXPR_ID=42$
                break;
            }
            case 43 : 
            {
                value = (java.lang.String)(((java.lang.String)field_mt940Status.getOldValue()));//$JR_EXPR_ID=43$
                break;
            }
            case 44 : 
            {
                value = (java.lang.String)(((java.lang.String)field_paymentChannel.getOldValue()));//$JR_EXPR_ID=44$
                break;
            }
            case 45 : 
            {
                value = (java.lang.String)(((java.lang.String)field_invoiceDueDate.getOldValue()));//$JR_EXPR_ID=45$
                break;
            }
            case 46 : 
            {
                value = (java.lang.String)(((java.lang.String)field_trxAmount.getOldValue()));//$JR_EXPR_ID=46$
                break;
            }
            case 47 : 
            {
                value = (java.lang.String)(((java.lang.String)field_bankName.getOldValue()));//$JR_EXPR_ID=47$
                break;
            }
            case 48 : 
            {
                value = (java.lang.String)(((java.lang.String)field_bankBranch.getOldValue()));//$JR_EXPR_ID=48$
                break;
            }
            case 49 : 
            {
                value = (java.lang.String)(((java.lang.String)field_rawData.getOldValue()));//$JR_EXPR_ID=49$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


    /**
     *
     */
    public Object evaluateEstimated(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 0 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=0$
                break;
            }
            case 1 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=1$
                break;
            }
            case 2 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=2$
                break;
            }
            case 3 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=3$
                break;
            }
            case 4 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=4$
                break;
            }
            case 5 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=5$
                break;
            }
            case 6 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=6$
                break;
            }
            case 7 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=7$
                break;
            }
            case 8 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_headerImg.getValue())+"/kominfo_2.png");//$JR_EXPR_ID=8$
                break;
            }
            case 9 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.String)("Total Settled");//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_settled.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.String)("Total Unsettled");//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_notSettled.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.String)("Total Need Confirmation");//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_unconfirmed.getValue()));//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=23$
                break;
            }
            case 24 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=24$
                break;
            }
            case 25 : 
            {
                value = (java.lang.String)("Total Invoice");//$JR_EXPR_ID=25$
                break;
            }
            case 26 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=26$
                break;
            }
            case 27 : 
            {
                value = (java.lang.String)(""+(Integer.parseInt(((java.lang.String)parameter_settled.getValue()))+Integer.parseInt(((java.lang.String)parameter_notSettled.getValue()))+Integer.parseInt(((java.lang.String)parameter_unconfirmed.getValue()))));//$JR_EXPR_ID=27$
                break;
            }
            case 28 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue()==1));//$JR_EXPR_ID=28$
                break;
            }
            case 29 : 
            {
                value = (java.lang.String)("Total Nilai Invoice Settled ");//$JR_EXPR_ID=29$
                break;
            }
            case 30 : 
            {
                value = (java.lang.String)(": "+ new java.text.DecimalFormat("Rp #,###,###").format(Double.valueOf(((java.lang.String)parameter_amountSettled.getValue()))));//$JR_EXPR_ID=30$
                break;
            }
            case 31 : 
            {
                value = (java.lang.String)("Tanggal "+((java.lang.String)parameter_trxStartDate.getValue())+" sampai dengan "+((java.lang.String)parameter_trxEndDate.getValue()));//$JR_EXPR_ID=31$
                break;
            }
            case 32 : 
            {
                value = (java.lang.String)("Laporan Rekonsiliasi Mingguan");//$JR_EXPR_ID=32$
                break;
            }
            case 33 : 
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_REPORT_COUNT.getEstimatedValue()).intValue()==0));//$JR_EXPR_ID=33$
                break;
            }
            case 34 : 
            {
                value = (java.lang.String)(((java.lang.String)field_invoiceNo.getValue()));//$JR_EXPR_ID=34$
                break;
            }
            case 35 : 
            {
                value = (java.lang.Integer)(((java.lang.Integer)variable_REPORT_COUNT.getEstimatedValue()));//$JR_EXPR_ID=35$
                break;
            }
            case 36 : 
            {
                value = (java.lang.String)(((java.lang.String)field_clientId.getValue()));//$JR_EXPR_ID=36$
                break;
            }
            case 37 : 
            {
                value = (java.lang.String)(((java.lang.String)field_clientName.getValue()).length()>50?((java.lang.String)field_clientName.getValue()).substring(0, 50):((java.lang.String)field_clientName.getValue()));//$JR_EXPR_ID=37$
                break;
            }
            case 38 : 
            {
                value = (java.lang.String)(((java.lang.String)field_simsStatus.getValue()));//$JR_EXPR_ID=38$
                break;
            }
            case 39 : 
            {
                value = (java.lang.String)(((java.lang.String)field_transactionTime.getValue()));//$JR_EXPR_ID=39$
                break;
            }
            case 40 : 
            {
                value = (java.lang.String)(((java.lang.String)field_reconcileStatus.getValue()));//$JR_EXPR_ID=40$
                break;
            }
            case 41 : 
            {
                value = (java.lang.String)(((java.lang.String)field_billerRc.getValue()));//$JR_EXPR_ID=41$
                break;
            }
            case 42 : 
            {
                value = (java.lang.String)(((java.lang.String)field_paymentDateSims.getValue()));//$JR_EXPR_ID=42$
                break;
            }
            case 43 : 
            {
                value = (java.lang.String)(((java.lang.String)field_mt940Status.getValue()));//$JR_EXPR_ID=43$
                break;
            }
            case 44 : 
            {
                value = (java.lang.String)(((java.lang.String)field_paymentChannel.getValue()));//$JR_EXPR_ID=44$
                break;
            }
            case 45 : 
            {
                value = (java.lang.String)(((java.lang.String)field_invoiceDueDate.getValue()));//$JR_EXPR_ID=45$
                break;
            }
            case 46 : 
            {
                value = (java.lang.String)(((java.lang.String)field_trxAmount.getValue()));//$JR_EXPR_ID=46$
                break;
            }
            case 47 : 
            {
                value = (java.lang.String)(((java.lang.String)field_bankName.getValue()));//$JR_EXPR_ID=47$
                break;
            }
            case 48 : 
            {
                value = (java.lang.String)(((java.lang.String)field_bankBranch.getValue()));//$JR_EXPR_ID=48$
                break;
            }
            case 49 : 
            {
                value = (java.lang.String)(((java.lang.String)field_rawData.getValue()));//$JR_EXPR_ID=49$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


}
