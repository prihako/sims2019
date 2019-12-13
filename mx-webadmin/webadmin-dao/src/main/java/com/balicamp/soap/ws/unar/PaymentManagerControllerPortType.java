package com.balicamp.soap.ws.unar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.1
 * 2019-12-12T14:38:27.831+07:00
 * Generated source version: 2.7.1
 * 
 */
@WebService(targetNamespace = "urn:PaymentManagerControllerwsdl", name = "PaymentManagerControllerPortType")
@XmlSeeAlso({ObjectFactory.class})
public interface PaymentManagerControllerPortType {

    @WebMethod(action = "urn:PaymentManagerControllerwsdl#inquiry")
    @RequestWrapper(localName = "inquiry", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.Inquiry")
    @ResponseWrapper(localName = "inquiryResponse", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.InquiryResponse")
    @WebResult(name = "inquiryResult", targetNamespace = "")
    public com.balicamp.soap.ws.unar.InquiryResult inquiry(
        @WebParam(name = "req", targetNamespace = "")
        com.balicamp.soap.ws.unar.InquiryRequest req
    );

    @WebMethod(action = "urn:PaymentManagerControllerwsdl#payment")
    @RequestWrapper(localName = "payment", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.Payment")
    @ResponseWrapper(localName = "paymentResponse", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.PaymentResponse")
    @WebResult(name = "paymentResult", targetNamespace = "")
    public com.balicamp.soap.ws.unar.PaymentResult payment(
        @WebParam(name = "req", targetNamespace = "")
        com.balicamp.soap.ws.unar.PaymentRequest req
    );

    @WebMethod(action = "urn:PaymentManagerControllerwsdl#reversal")
    @RequestWrapper(localName = "reversal", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.Reversal")
    @ResponseWrapper(localName = "reversalResponse", targetNamespace = "urn:PaymentManagerControllerwsdl", className = "paymentmanagercontrollerwsdl.ReversalResponse")
    @WebResult(name = "reversalResult", targetNamespace = "")
    public com.balicamp.soap.ws.unar.ReversalResult reversal(
        @WebParam(name = "req", targetNamespace = "")
        com.balicamp.soap.ws.unar.ReversalRequest req
    );
}