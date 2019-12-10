package com.balicamp.soap.ws.pengujian;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.1
 * 2019-12-10T22:34:08.470+07:00
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
    public com.balicamp.soap.ws.pengujian.ReversalResult reversal(
        @WebParam(name = "req", targetNamespace = "")
        com.balicamp.soap.ws.pengujian.ReversalRequest req
    );

    @WebResult(name = "paymentResult", targetNamespace = "")
    @RequestWrapper(localName = "payment", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.Payment")
    @WebMethod(action = "urn:PaymentManagerControllerwsdl#payment")
    @ResponseWrapper(localName = "paymentResponse", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.PaymentResponse")
    public com.balicamp.soap.ws.pengujian.PaymentResult payment(
        @WebParam(name = "req", targetNamespace = "")
        com.balicamp.soap.ws.pengujian.PaymentRequest req
    );

    @WebResult(name = "inquiryResult", targetNamespace = "")
    @RequestWrapper(localName = "inquiry", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.Inquiry")
    @WebMethod(action = "urn:PaymentManagerControllerwsdl#inquiry")
    @ResponseWrapper(localName = "inquiryResponse", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.InquiryResponse")
    public com.balicamp.soap.ws.pengujian.InquiryResult inquiry(
        @WebParam(name = "req", targetNamespace = "")
        com.balicamp.soap.ws.pengujian.InquiryRequest req
    );
}
