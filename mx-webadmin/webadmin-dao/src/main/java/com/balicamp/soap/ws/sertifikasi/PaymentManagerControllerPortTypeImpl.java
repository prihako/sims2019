
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.balicamp.soap.ws.sertifikasi;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.1
 * 2020-10-07T14:43:34.748+07:00
 * Generated source version: 2.7.1
 * 
 */

@javax.jws.WebService(
                      serviceName = "PaymentManagerControllerService",
                      portName = "PaymentManagerControllerPort",
                      targetNamespace = "urn:PaymentManagerControllerwsdl",
                      wsdlLocation = "file:/F:/sims2019/sims_git/sims2019/mx-webadmin/webadmin-dao/src/main/resources/wsdl/perangkat.wsdl",
                      endpointInterface = "paymentmanagercontrollerwsdl.PaymentManagerControllerPortType")
                      
public class PaymentManagerControllerPortTypeImpl implements PaymentManagerControllerPortType {

    private static final Logger LOG = Logger.getLogger(PaymentManagerControllerPortTypeImpl.class.getName());

    /* (non-Javadoc)
     * @see paymentmanagercontrollerwsdl.PaymentManagerControllerPortType#inquiry(paymentmanagercontrollerwsdl.InquiryRequest  req )*
     */
    public com.balicamp.soap.ws.sertifikasi.InquiryResult inquiry(com.balicamp.soap.ws.sertifikasi.InquiryRequest req) { 
        LOG.info("Executing operation inquiry");
        System.out.println(req);
        try {
        	com.balicamp.soap.ws.sertifikasi.InquiryResult _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see paymentmanagercontrollerwsdl.PaymentManagerControllerPortType#payment(paymentmanagercontrollerwsdl.PaymentRequest  req )*
     */
    public com.balicamp.soap.ws.sertifikasi.PaymentResult payment(com.balicamp.soap.ws.sertifikasi.PaymentRequest req) { 
        LOG.info("Executing operation payment");
        System.out.println(req);
        try {
        	com.balicamp.soap.ws.sertifikasi.PaymentResult _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see paymentmanagercontrollerwsdl.PaymentManagerControllerPortType#reversal(paymentmanagercontrollerwsdl.ReversalRequest  req )*
     */
    public com.balicamp.soap.ws.sertifikasi.ReversalResult reversal(com.balicamp.soap.ws.sertifikasi.ReversalRequest req) { 
        LOG.info("Executing operation reversal");
        System.out.println(req);
        try {
        	com.balicamp.soap.ws.sertifikasi.ReversalResult _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}