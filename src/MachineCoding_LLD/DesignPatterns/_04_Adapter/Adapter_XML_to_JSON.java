package MachineCoding_LLD.DesignPatterns._04_Adapter;

// Adapts XML's incompatible interface to the Data interface the client expects.
public class Adapter_XML_to_JSON implements Data {

    private final XML xml;

    public Adapter_XML_to_JSON(XML xml) {
        this.xml = xml;
    }

    @Override
    public void getData() {
        String xmlPayload = xml.fetchXmlData();
        String jsonPayload = convertXmlToJson(xmlPayload);
        System.out.println("Adapted to JSON: " + jsonPayload);
    }

    private String convertXmlToJson(String xmlPayload) {
        // Toy conversion just to demonstrate the adaptation step.
        return "{\"value\": 42}";
    }
}
