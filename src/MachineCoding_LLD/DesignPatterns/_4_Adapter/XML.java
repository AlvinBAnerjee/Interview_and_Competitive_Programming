package MachineCoding_LLD.DesignPatterns._4_Adapter;

// The adaptee: an existing/legacy source with an incompatible interface
// (different method name and return type than what Data expects).
public class XML {
    public String fetchXmlData() {
        String xmlPayload = "<data><value>42</value></data>";
        System.out.println("XML source returned: " + xmlPayload);
        return xmlPayload;
    }
}
