package com.balicamp.soap.ws.iar;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.1
 * 2019-12-02T17:50:18.860+07:00
 * Generated source version: 2.7.1
 * 
 */
@WebServiceClient(name = "PaymentManagerControllerService", 
                  wsdlLocation = "classpath:wsdl/KLBSI.wsdl",
                  targetNamespace = "urn:PaymentManagerControllerwsdl") 
public class PaymentManagerControllerService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("urn:PaymentManagerControllerwsdl", "PaymentManagerControllerService");
    public final static QName PaymentManagerControllerPort = new QName("urn:PaymentManagerControllerwsdl", "PaymentManagerControllerPort");
    static {
        URL url = null;
        try {
            url = new URL("classpath:wsdl/KLBSI.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(PaymentManagerControllerService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "classpath:wsdl/KLBSI.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public PaymentManagerControllerService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public PaymentManagerControllerService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PaymentManagerControllerService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public PaymentManagerControllerService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public PaymentManagerControllerService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public PaymentManagerControllerService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns PaymentManagerControllerPortType
     */
    @WebEndpoint(name = "PaymentManagerControllerPort")
    public PaymentManagerControllerPortType getPaymentManagerControllerPort() {
        return super.getPort(PaymentManagerControllerPort, PaymentManagerControllerPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PaymentManagerControllerPortType
     */
    @WebEndpoint(name = "PaymentManagerControllerPort")
    public PaymentManagerControllerPortType getPaymentManagerControllerPort(WebServiceFeature... features) {
        return super.getPort(PaymentManagerControllerPort, PaymentManagerControllerPortType.class, features);
    }

}
