package MachineCoding_LLD.DesignPatterns._4_Adapter;

// Client: depends only on the target Data interface, unaware it's talking
// to an adapted XML source.
public class JsonReader {

    private final Data data;

    public JsonReader(Data data) {
        this.data = data;
    }

    public void getData() {
        data.getData();
    }

    public static void main(String[] args) {
        Data adapter = new Adapter_XML_to_JSON(new XML());
        JsonReader reader = new JsonReader(adapter);
        reader.getData();
    }
}
