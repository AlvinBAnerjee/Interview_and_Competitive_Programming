package MachineCoding_LLD.DesignPatterns._4_Adapter;

public class Adapter_XML_to_JSON implements Data{
    XML xml=new XML();
    @Override
    public void getData() {

        xml.getData();
        //followed by do conversion
    }
}
