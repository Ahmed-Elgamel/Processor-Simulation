package src.processor;

public class RegisterFile {
    String [] GPRS;
    String SREG;
    String PC;

    public RegisterFile()
    {
        GPRS=new String[64];
        SREG="00000000";
        PC="0000000000000000";
        fill();
    }
    public void fill()
    {
        String s="00000000";
        for (int i = 0; i <64 ; i++) {
            GPRS[i]=s;
        }
    }
}
