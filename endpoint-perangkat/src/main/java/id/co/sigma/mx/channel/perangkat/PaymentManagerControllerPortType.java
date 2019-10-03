package id.co.sigma.mx.channel.perangkat;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.1
 * 2014-11-17T08:43:01.924+07:00
 * Generated source version: 2.7.1
 * 
 */
@WebService(targetNamespace = "urn:PaymentManagerControllerwsdl", name = "PaymentManagerControllerPortType")
@XmlSeeAlso({ObjectFactory.class})
public interface PaymentManagerControllerPortType {

    @WebResult(name = "reversalResult", targetNamespace = "")
    @RequestWrapper(localName = "reversal", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.Reversal")
    @WebMethod(action = "urn:PaymentManagerControllerwsdl#reversal")
    @ResponseWrapper(localName = "reversalResponse", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.ReversalResponse")
    public id.co.sigma.mx.channel.perangkat.ReversalResult reversal(
        @WebParam(name = "req", targetNamespace = "")
        id.co.sigma.mx.channel.perangkat.ReversalRequest req
    );

    @WebResult(name = "paymentResult", targetNamespace = "")
    @RequestWrapper(localName = "payment", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.Payment")
    @WebMethod(action = "urn:PaymentManagerControllerwsdl#payment")
    @ResponseWrapper(localName = "paymentResponse", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.PaymentResponse")
    public id.co.sigma.mx.channel.perangkat.PaymentResult payment(
        @WebParam(name = "req", targetNamespace = "")
        id.co.sigma.mx.channel.perangkat.PaymentRequest req
    );

    @WebResult(name = "inquiryResult", targetNamespace = "")
    @RequestWrapper(localName = "inquiry", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.Inquiry")
    @WebMethod(action = "urn:PaymentManagerControllerwsdl#inquiry")
    @ResponseWrapper(localName = "inquiryResponse", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.InquiryResponse")
    public id.co.sigma.mx.channel.perangkat.InquiryResult inquiry(
        @WebParam(name = "req", targetNamespace = "")
        id.co.sigma.mx.channel.perangkat.InquiryRequest req
    );
}
