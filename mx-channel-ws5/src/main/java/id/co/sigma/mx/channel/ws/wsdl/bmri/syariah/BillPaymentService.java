package id.co.sigma.mx.channel.ws.wsdl.bmri.syariah;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * This class was generated by Apache CXF 2.7.4
 * 2013-05-07T17:11:13.204+07:00
 * Generated source version: 2.7.4
 * 
 */
@WebServiceClient(name = "BillPaymentService", 
                  wsdlLocation = "BillPaymentService.wsdl",
                  targetNamespace = "bankmandiri.h2h.billpayment.ws") 
public class BillPaymentService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("bankmandiri.h2h.billpayment.ws", "BillPaymentService");
    public final static QName BillPaymentServiceSoap = new QName("bankmandiri.h2h.billpayment.ws", "BillPaymentServiceSoap");
    static {
        URL url = BillPaymentService.class.getResource("BillPaymentService.wsdl");
        if (url == null) {
            url = BillPaymentService.class.getClassLoader().getResource("BillPaymentService.wsdl");
        } 
        if (url == null) {
            java.util.logging.Logger.getLogger(BillPaymentService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "BillPaymentService.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public BillPaymentService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BillPaymentService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BillPaymentService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     *
     * @return
     *     returns BillPaymentServiceSoap
     */
    @WebEndpoint(name = "BillPaymentServiceSoap")
    public BillPaymentServiceSoap getBillPaymentServiceSoap() {
        return super.getPort(BillPaymentServiceSoap, BillPaymentServiceSoap.class);
    }

}
