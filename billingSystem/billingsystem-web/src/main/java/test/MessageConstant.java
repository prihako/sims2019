package test;

import java.util.HashMap;
import java.util.Map;

public class MessageConstant {

    static public class TransactionType {
        public static final String TRANSFER_OVERBOOKING = "TRANSFER_OVERBOOKING";

        public static final String TRANSFER_NON_ATMBERSAMA = "VIA_TREASURY";

        public static final String TRANSFER_ATMBERSAMA = "ATM_BERSAMA";

        public static final String TRANSFER_ALTO = "ALTO";
    
        public static final String SETOR_TUNAI_ALTO = "SETOR_TUNAI_ALTO";

        public static final String TRANSFER_SKN = "TRANSFER_SKN";

        public static final String TRANSFER_RTGS = "TRANSFER_RTGS";

        public static final String TRANSFER_BATCH = "TRANSFER_BATCH";

        static public String SELULAR_PREPAID = "SELULAR_PREPAID";

        static public String SELULAR_POSTPAID = "SELULAR_POSTPAID";

        static public String DEPOSITO = "TIME_DEPOSIT";

        public static final String ACCOUNT_REGISTRATION = "ACCOUNT_REGISTRATION";

        public static final String INQUIRY_BALANCE = "INQUIRY_BALANCE";

        public static final String INQUIRY_ACCOUNT_MUTATION = "INQUIRY_ACCOUNT_MUTATION";

        public static final String NETWORK_ECHO = "NETWORK_ECHO";

        public static final String COMMON_TRANSACTION = "COMMON_TRANSACTION";

        public static final String INQUIRY_POIN = "INQUIRY_POIN";

        public static final String FEE_WESELKU_CASH_IN = "FEE_WESELKU_CASH_IN";
        
        public static final String FEE_WESELKU_NEWS_CASH_IN = "FEE_WESELKU_NEWS_CASH_IN";

        public static final String FEE_WESELKU_CASH_OUT = "FEE_WESELKU_CASH_OUT";
        
        public static final String FEE_WESELKU_NEWS_CASH_OUT = "FEE_WESELKU_NEWS_CASH_OUT";
            
        public static final String FEE_WESELKU_BANK = "FEE_WESELKU_BANK";
        
        public static final String FEE_WESELKU_NEWS_BANK = "FEE_WESELKU_NEWS_BANK";
        
        public static final String WESELKU_I_CASH_IN = "WESELKU_I_CASH_IN";
        
        public static final String WESELKU_I_NEWS_CASH_IN = "WESELKU_I_NEWS_CASH_IN";
        
        public static final String WESELKU_CASH_IN = "WESELKU_CASH_IN";
        
        public static final String WESELKU_NEWS_CASH_IN = "WESELKU_NEWS_CASH_IN";

        public static final String WESELKU_CASH_OUT = "WESELKU_CASH_OUT";
        
        public static final String WESELKU_NEWS_CASH_OUT = "WESELKU_NEWS_CASH_OUT";
        
        public static final String WESELKU_REFUND = "WESELKU_REFUND";

        public static final String WESELKU_CANCEL = "WESELKU_CANCEL";

        public static final String CARD_NUMBER = "CARD";
        
        public static final String FTRANSFER_CASH_IN = "FTRANSFER_CASH_IN";

        public static final String FTRANSFER_CASH_IN_STATUS = "FTRANSFER_CASH_IN_STATUS";

        public static final String FTRANSFER_CASH_OUT = "FTRANSFER_CASH_OUT";

        public static final String FTRANSFER_CASH_OUT_STATUS = "FTRANSFER_CASH_OUT_STATUS";

        public static final String FTRANSFER_REFUND = "FTRANSFER_REFUND";

        public static final String FTRANSFER_REFUND_STATUS = "FTRANSFER_REFUND_STATUS";
        
        public static final String TCASH_AIS_CASH_IN = "TCASH_AIS_CASH_IN";
        
        public static final String TCASH_AIS_CASH_OUT = "TCASH_AIS_CASH_OUT";
        
        public static final String TCASH_AIS_CASH_IN_STATUS = "TCASH_AIS_CASH_IN_STATUS";
        
        public static final String TCASH_AIS_CASH_OUT_STATUS = "TCASH_AIS_CASH_OUT_STATUS";
        
        public static final String MB_REGISTRATION = "MB_REGISTRATION";
        
        public static final String GSP_NONTAGLIST = "GSP_NONTAGLIST";
        
        public static final String SERA_MX3B_SELULAR_PREPAID = "SERA_MX3B_SELULAR_PREPAID";
        
        public static final String SELULAR_POSTPAID_SERA = "SELULAR_POSTPAID_SERA";
        
        public static final Map<String, String[]> TRANSACTION_LIST;

        public static final Map<String, String> TRANSACTION_LIST_REV;
        
        public static final String AKSES_GLOBALINDO_AIR_PALYJA = "AKSES_GLOBALINDO_AIR_PALYJA";

        static {
            TRANSACTION_LIST_REV = new HashMap<String, String>();
            TRANSACTION_LIST_REV.put(TRANSFER_OVERBOOKING, TRANSFER_OVERBOOKING);
            TRANSACTION_LIST_REV.put(TRANSFER_NON_ATMBERSAMA, TRANSFER_NON_ATMBERSAMA);
            TRANSACTION_LIST_REV.put(TRANSFER_ATMBERSAMA, TRANSFER_ATMBERSAMA);
            TRANSACTION_LIST_REV.put(TRANSFER_ALTO, TRANSFER_ALTO);
            TRANSACTION_LIST_REV.put(SETOR_TUNAI_ALTO, SETOR_TUNAI_ALTO);
            TRANSACTION_LIST_REV.put("INDOSMART_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("SERA_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("SERA_MX3B_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("MKN_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("ACS_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("AKSES_NUSANTARA_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("SIPUTRI_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("NUSAPRO_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("MITRACOMM_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("EURONET_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("ARTA_JASA_SELULAR_PREPAID", SELULAR_PREPAID);
            TRANSACTION_LIST_REV.put("SELULAR_POSTPAID_OTHER", SELULAR_POSTPAID);
            TRANSACTION_LIST_REV.put("SELULAR_POSTPAID_SERA", SELULAR_POSTPAID);
            TRANSACTION_LIST_REV.put("SELULAR_POSTPAID_FINNET", SELULAR_POSTPAID);
            TRANSACTION_LIST_REV.put("SELULAR_POSTPAID_MITRACOMM", SELULAR_POSTPAID);
            TRANSACTION_LIST_REV.put("SELULAR_POSTPAID_ACS", SELULAR_POSTPAID);
            TRANSACTION_LIST_REV.put("SELULAR_POSTPAID_AKSES", SELULAR_POSTPAID);
            TRANSACTION_LIST_REV.put("SYB_PLN_PREPAID", "PLN_PREPAID");
            TRANSACTION_LIST_REV.put("GSP_PLN_PREPAID", "PLN_PREPAID");
            TRANSACTION_LIST_REV.put("FINNET_EWALLET", "EWALLET_PREPAID");
            TRANSACTION_LIST_REV.put("SYB_PLN_POSTPAID", "PLN_POSTPAID");
            TRANSACTION_LIST_REV.put("GSP_PLN_POSTPAID", "PLN_POSTPAID");
            TRANSACTION_LIST_REV.put("FINNET_TELKOM", "FINNET_TELKOM");
            TRANSACTION_LIST_REV.put("FINNET_INTERNET", "INTERNET");
            TRANSACTION_LIST_REV.put("BRI_SYB_LOAN", "CREDIT_LOAN");
            TRANSACTION_LIST_REV.put("FINNET_LOAN", "CREDIT_LOAN");
            TRANSACTION_LIST_REV.put("AJ_LOAN1", "CREDIT_LOAN");
            TRANSACTION_LIST_REV.put("AJ_LOAN2", "CREDIT_LOAN");
            TRANSACTION_LIST_REV.put("AJ_LOAN3", "CREDIT_LOAN");
            TRANSACTION_LIST_REV.put("BRI_SYB_CREDIT_CARD1", "CREDIT_CARD");
            TRANSACTION_LIST_REV.put("BRI_SYB_CREDIT_CARD2", "CREDIT_CARD");
            TRANSACTION_LIST_REV.put("BRISYB_TRAIN_TICKET", "TRAIN_TICKET");
            TRANSACTION_LIST_REV.put("FINNET_TRAIN_TICKET", "TRAIN_TICKET");
            TRANSACTION_LIST_REV.put("BRISYB_TICKET_PLANE", "PLANE_TICKET");
            TRANSACTION_LIST_REV.put("MITRACOMM_WATER", "WATER");
            TRANSACTION_LIST_REV.put("FINNET_TV", "TV");
            TRANSACTION_LIST_REV.put("FINNET_TV_TELKOMVISION", "TV");
            TRANSACTION_LIST_REV.put("AG_TV_CENTRIN", "TV");
            TRANSACTION_LIST_REV.put("TCASH_AIS_CASH_IN", "TCASH_AIS_CASH_IN");
            TRANSACTION_LIST_REV.put("TCASH_AIS_CASH_OUT", "TCASH_AIS_CASH_OUT");
            TRANSACTION_LIST_REV.put("TCASH_AIS_CASH_IN_STATUS", "TCASH_AIS_CASH_IN_STATUS");
            TRANSACTION_LIST_REV.put("TCASH_AIS_CASH_OUT_STATUS", "TCASH_AIS_CASH_OUT_STATUS");
            TRANSACTION_LIST_REV.put("MB_REGISTRATION", "MB_REGISTRATION");
            TRANSACTION_LIST_REV.put("GSP_NONTAGLIST", "GSP_NONTAGLIST");
            TRANSACTION_LIST_REV.put("AKSES_GLOBALINDO_AIR_PALYJA", "WATER");
                        
            
            TRANSACTION_LIST_REV.put("FTRANSFER_CASH_IN", "FTRANSFER_CASH_IN");
            TRANSACTION_LIST_REV.put("FTRANSFER_CASH_OUT", "FTRANSFER_CASH_OUT");
            TRANSACTION_LIST_REV.put("FTRANSFER_REFUND", "FTRANSFER_REFUND");
            TRANSACTION_LIST_REV.put("WESELKU_CASH_IN", "WESELKU_CASH_IN");
            TRANSACTION_LIST_REV.put("WESELKU_NEWS_CASH_IN", "WESELKU_NEWS_CASH_IN");
            TRANSACTION_LIST_REV.put("WESELKU_I_CASH_IN", "WESELKU_I_CASH_IN");
            TRANSACTION_LIST_REV.put("WESELKU_I_NEWS_CASH_IN", "WESELKU_I_NEWS_CASH_IN");
            TRANSACTION_LIST_REV.put("WESELKU_CASH_OUT", "WESELKU_CASH_OUT");
            TRANSACTION_LIST_REV.put("WESELKU_NEWS_CASH_OUT", "WESELKU_NEWS_CASH_OUT");
            TRANSACTION_LIST_REV.put("WESELKU_REFUND", "WESELKU_REFUND");

            TRANSACTION_LIST = new HashMap<String, String[]>();
            TRANSACTION_LIST.put(TRANSFER_OVERBOOKING, new String[] { TRANSFER_OVERBOOKING });
            TRANSACTION_LIST.put(TRANSFER_NON_ATMBERSAMA, new String[] { TRANSFER_NON_ATMBERSAMA });
            TRANSACTION_LIST.put(TRANSFER_ATMBERSAMA, new String[] { TRANSFER_ATMBERSAMA });
            TRANSACTION_LIST.put(TRANSFER_ALTO, new String[] { TRANSFER_ALTO });
            TRANSACTION_LIST.put(SETOR_TUNAI_ALTO, new String[] { SETOR_TUNAI_ALTO });
            TRANSACTION_LIST.put(SELULAR_PREPAID, new String[] { "INDOSMART_SELULAR_PREPAID", "SERA_SELULAR_PREPAID",
            		"MKN_SELULAR_PREPAID","AKSES_NUSANTARA_PREPAID","ACS_SELULAR_PREPAID", "SIPUTRI_SELULAR_PREPAID", "NUSAPRO_SELULAR_PREPAID", 
            		"MITRACOMM_SELULAR_PREPAID", "EURONET_SELULAR_PREPAID", "ARTA_JASA_SELULAR_PREPAID","SERA_MX3B_SELULAR_PREPAID"});
            TRANSACTION_LIST.put(SELULAR_POSTPAID, new String[] { "SELULAR_POSTPAID_OTHER","SELULAR_POSTPAID_AKSES","SELULAR_POSTPAID_ACS", "SELULAR_POSTPAID_FINNET",
                    "SELULAR_POSTPAID_MITRACOMM","SELULAR_POSTPAID_SERA" });
            TRANSACTION_LIST.put("PLN_PREPAID", new String[] { "SYB_PLN_PREPAID","GSP_PLN_PREPAID" });
//            TRANSACTION_LIST.put("EWALLET", new String[] { "FINNET_EWALLET" });
            TRANSACTION_LIST.put("PLN_POSTPAID", new String[] { "SYB_PLN_POSTPAID","GSP_PLN_POSTPAID" });
            TRANSACTION_LIST.put("TELKOM", new String[] { "FINNET_TELKOM" });
            TRANSACTION_LIST.put("INTERNET", new String[] { "FINNET_INTERNET" });
//            TRANSACTION_LIST.put("CREDIT_LOAN", new String[] { "BRI_SYB_LOAN", "FINNET_LOAN", "AJ_LOAN1", "AJ_LOAN2",
//                    "AJ_LOAN3" });
//            TRANSACTION_LIST.put("CREDIT_CARD", new String[] { "BRI_SYB_CREDIT_CARD1", "BRI_SYB_CREDIT_CARD2" });
            TRANSACTION_LIST.put("TRAIN_TICKET", new String[] { "BRISYB_TRAIN_TICKET", "FINNET_TRAIN_TICKET" });
//            TRANSACTION_LIST.put("PLANE_TICKET", new String[] { "BRISYB_TICKET_PLANE" });
//            TRANSACTION_LIST.put("WATER", new String[] { "MITRACOMM_WATER" });
            TRANSACTION_LIST.put("TV", new String[] { "FINNET_TV", "FINNET_TV_TELKOMVISION", "AG_TV_CENTRIN" });

//            TRANSACTION_LIST.put("TCASH_IN", new String[] { "TCASH_IN" });

            TRANSACTION_LIST.put(FTRANSFER_CASH_IN, new String[] { FTRANSFER_CASH_IN });
            TRANSACTION_LIST.put(FTRANSFER_CASH_OUT, new String[] { FTRANSFER_CASH_OUT });
            TRANSACTION_LIST.put(FTRANSFER_REFUND, new String[] { FTRANSFER_REFUND });
            TRANSACTION_LIST.put(WESELKU_CASH_IN, new String[] { WESELKU_CASH_IN });
            TRANSACTION_LIST.put(WESELKU_NEWS_CASH_IN, new String[] { WESELKU_NEWS_CASH_IN });
            TRANSACTION_LIST.put(WESELKU_I_CASH_IN, new String[] { WESELKU_I_CASH_IN });
            TRANSACTION_LIST.put(WESELKU_I_NEWS_CASH_IN, new String[] { WESELKU_I_NEWS_CASH_IN });
            TRANSACTION_LIST.put(WESELKU_CASH_OUT, new String[] { WESELKU_CASH_OUT });
            TRANSACTION_LIST.put(WESELKU_NEWS_CASH_OUT, new String[] { WESELKU_NEWS_CASH_OUT });
            TRANSACTION_LIST.put(WESELKU_REFUND, new String[] { WESELKU_REFUND });
            
            TRANSACTION_LIST.put(TCASH_AIS_CASH_IN, new String[] { TCASH_AIS_CASH_IN });
            TRANSACTION_LIST.put(TCASH_AIS_CASH_OUT, new String[] { TCASH_AIS_CASH_OUT });
            TRANSACTION_LIST.put(TCASH_AIS_CASH_IN_STATUS, new String[] { TCASH_AIS_CASH_IN_STATUS });
            TRANSACTION_LIST.put(TCASH_AIS_CASH_OUT_STATUS, new String[] { TCASH_AIS_CASH_OUT_STATUS });
            TRANSACTION_LIST.put("MB_REGISTRATION",new String[] {"MB_REGISTRATION"});
            TRANSACTION_LIST.put("GSP_NONTAGLIST",new String[] {"GSP_NONTAGLIST"});
            TRANSACTION_LIST.put("AKSES_GLOBALINDO_AIR_PALYJA",new String[] {"AKSES_GLOBALINDO_AIR_PALYJA"});
            
            
        }
    }

    static public class ResponseCode {
        public static final String SUCCESS = "00";
    }
    
    static public class Industry {
    	public static final String CASH_REMITTANCE = "Cash Remittance";
    }
    
	static public class Security {
		public static final String GROUP = "security";
		public static final String PASSWORD_LENGTH="security.passwordLength";
		public static final String USERNAME_NAME_LENGTH="security.userName.nameLength";
		public static final String USERNAME_TOTAL_LENGTH="security.userName.totalLength";
		public static final String ITM_PIN_ENCODER_KEY="security.itm.pinEncoderKey";
		
		public static final String VPIN_LIVE_TIME = "security.vPin.liveTime";
		public static final String VPIN_LENGTH = "security.vPin.length";
		public static final String VPIN_PART_LENGTH = "security.vPin.partLength";
		public static final String VPIN_3DES_SEEDKEY = "security.vPin.seedKey";

		public static final String SOFTTOKEN_LIVETIME = "security.softToken.liveTime";
		public static final String SOFTTOKEN_LENGTH = "security.softToken.length";
		public static final String SOFTTOKEN_ACTIVATION_LIVETIME = "security.softToken.activation.liveTime";

		public static final String HARDTOKEN_LENGTH = "security.hardToken.length";
		public static final String HARDTOKEN_LIVE_TIME = "security.hardToken.liveTime";

		public static final String LOGINFAIL_BLOCK_TIMES = "security.loginFail.block.times";
		public static final String LOGINFAIL_BLOCK_INTERVAL = "security.loginFail.block.inteval";
		public static final String SOFTBLOCK_RESET_TIMES =
			"security.softBlock.reset.times";
		
		public static final String INITIAL_USER_ROLE = "security.initialUserRole";

		public static final String ALLOW_WEAK_VPIN= "security.allowWeakVPin";

		public static final String SESION_TIMEOUT_PERIODE = "security.sessionTimeoutPeriode";

	}		
}
