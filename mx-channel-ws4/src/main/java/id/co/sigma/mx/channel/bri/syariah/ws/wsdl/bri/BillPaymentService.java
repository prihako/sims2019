package id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.1
 * 2017-05-13T09:57:11.862+07:00
 * Generated source version: 2.7.1
 * 
 */
@WebServiceClient(name = "BillPaymentService", 
                  wsdlLocation = "classpath:/wsdl/BillPaymentService.wsdl",
                  targetNamespace = "bri.h2h.billpayment.ws") 
public class BillPaymentService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("bri.h2h.billpayment.ws", "BillPaymentService");
    public final static QName BillPaymentServiceSoap = new QName("bri.h2h.billpayment.ws", "BillPaymentServiceSoap");
    static {
        URL url = null;
        try {
            url = new URL("classpath:/wsdl/BillPaymentService.wsdl");
        } catch (MalformedURLException e) {
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
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public BillPaymentService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public BillPaymentService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public BillPaymentService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
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

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BillPaymentServiceSoap
     */
    @WebEndpoint(name = "BillPaymentServiceSoap")
    public BillPaymentServiceSoap getBillPaymentServiceSoap(WebServiceFeature... features) {
        return super.getPort(BillPaymentServiceSoap, BillPaymentServiceSoap.class, features);
    }

}
