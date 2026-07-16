package MachineCoding_LLD.DesignPatterns._4_Adapter;

public class JsonReader {
    private Data d;
    public void getData()
    {
        d=new Adapter_XML_to_JSON();
        d.getData();
    }

}
