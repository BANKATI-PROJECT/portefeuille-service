package ma.ensa.portefeuille_service.util;
import jakarta.xml.soap.*;
import ma.ensa.portefeuille_service.model.AddRealCardResponse;
import ma.ensa.portefeuille_service.model.MessageResponse;
import ma.ensa.portefeuille_service.model.RealCardCMI;

import java.io.ByteArrayOutputStream;
// import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
// import org.xml.sax.InputSource;

public class SoapHandler {
    
    public static SOAPMessage buildAddRealCardRequest(String safeToken, String cardNum, String cvv, String expire, String label) throws Exception {
        // Create a SOAP message factory
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        
        // Get the SOAP envelope
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        
        // Add namespaces
        envelope.addNamespaceDeclaration("req", "http://ensa.ma/cmi-service/requests_responses");
        envelope.addNamespaceDeclaration("RealCardCMI", "http://ensa.ma/cmi-service/entities/RealCardCMI");

        // Build SOAP body
        SOAPBody body = envelope.getBody();
        SOAPElement addRealCardRequest = body.addChildElement("AddRealCardRequest", "req");

        // Add safeToken element
        SOAPElement safeTokenElement = addRealCardRequest.addChildElement("safeToken", "req");
        safeTokenElement.addTextNode(safeToken);

        // Add card element
        SOAPElement cardElement = addRealCardRequest.addChildElement("card", "req");

        // Add card details
        SOAPElement numElement = cardElement.addChildElement("num", "RealCardCMI");
        numElement.addTextNode(cardNum);

        SOAPElement cvvElement = cardElement.addChildElement("cvv", "RealCardCMI");
        cvvElement.addTextNode(cvv);

        SOAPElement expireElement = cardElement.addChildElement("expire", "RealCardCMI");
        expireElement.addTextNode(expire);

        SOAPElement labelElement = cardElement.addChildElement("label", "RealCardCMI");
        labelElement.addTextNode(label);

        SOAPElement soldeElement = cardElement.addChildElement("solde", "RealCardCMI");
        soldeElement.addTextNode("1000");

        // Save the changes to the SOAP message
        soapMessage.saveChanges();

        // Log the request (optional)
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);
        System.out.println("SOAP Request: \n" + out.toString());

        return soapMessage;
    }

    public static SOAPMessage sendSoapRequest(String endpointUrl, SOAPMessage soapRequest) throws Exception {
        // Create a SOAP connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send the request and receive the response
        SOAPMessage soapResponse = soapConnection.call(soapRequest, new URL(endpointUrl));

        // Close the connection
        soapConnection.close();

        return soapResponse;
    }

    public static AddRealCardResponse parsebuildAddRealCardResponse(SOAPMessage soapResponse) throws Exception {
        // Get the SOAP body from the response
        SOAPBody soapBody = soapResponse.getSOAPBody();
        AddRealCardResponse response = new AddRealCardResponse();
        
        NodeList safeTokenNodeList = soapBody.getElementsByTagName("ns2:safeToken");
        if (safeTokenNodeList.getLength() > 0) {
            response.setSafeToken(safeTokenNodeList.item(0).getTextContent());
        } else {
            return null;
        }

        NodeList cardIdNodeList = soapBody.getElementsByTagName("ns2:cardId");
        if (cardIdNodeList.getLength() > 0) {
            response.setCardId(Long.parseLong(cardIdNodeList.item(0).getTextContent()));
        } else {
            return null;
        }

        return response;
    }

    public static MessageResponse parseCreateTransactionResponse(SOAPMessage soapResponse) throws Exception {
        // Get the SOAP body from the response
        SOAPBody soapBody = soapResponse.getSOAPBody();
        MessageResponse response = new MessageResponse();
        
        NodeList messageNodeList = soapBody.getElementsByTagName("ns2:message");
        if (messageNodeList.getLength() > 0) {
            response.setMessage(messageNodeList.item(0).getTextContent());
        } else {
            return null;
        }
        if(response.getMessage().equals("Transaction successful"))
            return response;

        return null;
    }
    
     public static List<RealCardCMI> parseGetAllCardsResponse(SOAPMessage soapResponse) throws Exception {
        List<RealCardCMI> cards = new ArrayList<>();
        SOAPBody soapBody = soapResponse.getSOAPBody();
        
        // Parse the SOAP response
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        // Navigate to the cards elements
        NodeList cardNodes = soapBody.getElementsByTagName("ns3:cards");

        for (int i = 0; i < cardNodes.getLength(); i++) {
            Element cardElement = (Element) cardNodes.item(i);
            RealCardCMI card = new RealCardCMI();

            // Extract card details
            card.setId(Integer.parseInt(getElementTextContent(cardElement, "ns2:id")));
            card.setNum(obscureCreditCardNumber(getElementTextContent(cardElement, "ns2:num")));
            card.setLabel(getElementTextContent(cardElement, "ns2:label"));

            cards.add(card);
        }

        return cards;
    }
    private static String getElementTextContent(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
    public static SOAPMessage createTransactionRequest(String saveToken, double cardId, double amount) throws Exception {
        // Create a SOAP message
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        // Get the SOAP envelope
        SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        envelope.addNamespaceDeclaration("req", "http://ensa.ma/cmi-service/requests_responses");

        // Add SOAP Body
        SOAPBody body = envelope.getBody();

        // Create the TransactionRequest element
        SOAPElement transactionRequest = body.addChildElement("TransactionRequest", "req");

        // Add the saveToken element
        SOAPElement saveTokenElement = transactionRequest.addChildElement("saveToken", "req");
        saveTokenElement.addTextNode(saveToken);

        // Add the id element
        SOAPElement idElement = transactionRequest.addChildElement("id", "req");
        idElement.addTextNode(String.valueOf(cardId));

        // Add the amount element
        SOAPElement amountElement = transactionRequest.addChildElement("amount", "req");
        amountElement.addTextNode(String.valueOf(amount));

        // Save changes to the SOAP message
        soapMessage.saveChanges();

        // Return the constructed SOAP message
        return soapMessage;
    }

    public static SOAPMessage createGetAllCardsRequest(String saveToken) throws Exception {
        // Create a SOAP message
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        // Get the SOAP envelope
        SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        envelope.addNamespaceDeclaration("req", "http://ensa.ma/cmi-service/requests_responses");

        // Add SOAP Body
        SOAPBody body = envelope.getBody();

        // Create the GetAllCardsRequest element
        SOAPElement getAllCardsRequest = body.addChildElement("GetAllCardsRequest", "req");

        // Add the saveToken element
        SOAPElement saveTokenElement = getAllCardsRequest.addChildElement("saveToken", "req");
        saveTokenElement.addTextNode(saveToken);

        // Save changes to the SOAP message
        soapMessage.saveChanges();

        // Return the constructed SOAP message
        return soapMessage;
    }

    private static String obscureCreditCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return cardNumber; // Return as is if too short to obscure
        }
        // Keep the first 4 and last 4 digits visible, replace the middle with *
        int visibleDigits = 4;
        int length = cardNumber.length();
        String start = cardNumber.substring(0, visibleDigits);
        String end = cardNumber.substring(length - visibleDigits);
        String obscuredMiddle = "*".repeat(length - (2 * visibleDigits));
        return start + obscuredMiddle + end;
    }
}
