package src.processor;

public class DataMemory {
    String[] dm;
    public DataMemory()
    {
        dm=new String[2048];
        fill();
    }
    public void fill()
    {
        String s="00000000";
        for (int i=0;i<2048;i++)
            dm[i]=s;
    }

}
