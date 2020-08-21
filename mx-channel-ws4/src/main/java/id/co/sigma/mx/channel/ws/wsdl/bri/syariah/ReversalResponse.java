
package id.co.sigma.mx.channel.ws.wsdl.bri.syariah;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReversalResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReversalResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="status" type="{bri.h2h.billpayment.ws}Status" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReversalResponse", propOrder = {
    "status"
})
public class ReversalResponse {

    protected Status status;

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }
    
	@SuppressWarnings("rawtypes")
	public ReversalResponse convertFromMap(Map map) {
		// note based on BMRI-POSTEL WS Specification Documents
		ReversalResponse reversalResponse = new ReversalResponse();

		Status status = new Status();
		status.setErrorCode(map.get("errorCode").toString());
		if (status.getErrorCode() != null && status.getErrorCode().equals("00")) {
			status.setIsError(false);
		} else {
			status.setIsError(true);
		}
		status.setStatusDescription(map.get("statusDescription").toString());

		reversalResponse.setStatus(status);

		return reversalResponse;
	}

}
