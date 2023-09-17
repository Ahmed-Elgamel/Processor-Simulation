package src.processor;

public class main {
 //  public static short currentinst;
    static  InstructionMemory instructionmem;
    static DataMemory datamem;
    static RegisterFile regfile;
//    static int OpCode;
//    static int R1;
//    static int R2;
//    static byte R2value;
//    static int immediate;
//    static int temp;
//    static int sign1;
//    static int sign2;
    static int clk;
    static String pipelineregfe_de;
    static String [] pipelineregde_ex;
    static int PCvalue;
static int numoflines;
    static int C, V, N, S, Z;
    public static void main(String[] args) {

         instructionmem=new InstructionMemory();
         datamem=new DataMemory();
         regfile=new RegisterFile();
         pipelineregde_ex=new String[6];
         fill();
         pipelineregfe_de="0000000000000000";
         instructionmem.read_textfile();
         numoflines=instructionmem.numoflines;
         PCvalue=0;
        C=0;V=0;N=0;S=0;Z=0;
         m();
         print_registers();
         print_mem();

    }
    public static void print_registers()
    {
        System.out.println("PC="+from16bitstonum(regfile.PC));
        System.out.println("SREG="+regfile.SREG);
        for (int i = 0; i <64 ; i++) {
            System.out.println("R"+i+"="+from8bitstonum(regfile.GPRS[i]));
        }
    }
    public static void print_mem()
    {
        for (int i = 0; i <2048 ; i++) {
            System.out.println("memory at "+i+" = "+from8bitstonum(datamem.dm[i]));
        }
        for (int i = 0; i <1024 ; i++) {
            System.out.println("instruction at"+i+" = "+from16bitstonum(instructionmem.im[i]));

        }
    }
    public static void fill()
    {
        pipelineregde_ex[0]="1111";
        pipelineregde_ex[1]="111111";
        pipelineregde_ex[2]="111111";
        pipelineregde_ex[3]="000000";
        pipelineregde_ex[4]="00000000";
        pipelineregde_ex[5]="00000000";
    }
    public static void m()
    {
        int c=0;
        for(clk=0;clk<(3+numoflines-1);clk++)
        {

            System.out.println("Clock cycle number"+clk);
            if (PCvalue==numoflines)
            {
                if (c==0){
                execute();decode();
                c++;
                }else if(c==1)execute();
            }else{
            if (clk==(numoflines+3-2))
            {
              execute();
            }else
            {
                if(clk==numoflines+3-3)
                {
                   execute();decode();
                }else{
                    if (clk==1){

                   decode(); fetch();
                    }
                    else if(clk==0)
                    {
                        fetch();
                    }else
                    {
                        execute();decode();fetch();
                    }
                }
            }}



        }
    }

    public static void fetch()
    {
        PCvalue=Integer.parseInt(regfile.PC,2);
        System.out.println("fetching inst at pc= "+PCvalue);
        pipelineregfe_de=instructionmem.im[PCvalue];
        PCvalue++;
        if (PCvalue>65535)
        {
            System.out.println("");
            //momken 2a3melha ye throw exception
            return;
        }
        regfile.PC=fromnumto16bits(PCvalue);
    }
    public static void decode()
    {
        System.out.println("decoding inst= "+pipelineregfe_de);
        pipelineregde_ex[0] =  (pipelineregfe_de.substring(0,4));//opcode
        pipelineregde_ex[1]=       (pipelineregfe_de.substring(4,10));//r1adress
        pipelineregde_ex[2]=       (pipelineregfe_de.substring(10,16));//r2 address
        pipelineregde_ex[3] =(pipelineregfe_de.substring(10,16));//immediate
        int r1address=Integer.parseInt(pipelineregde_ex[1],2);
        int r2address=Integer.parseInt(pipelineregde_ex[2],2);
        pipelineregde_ex[4]=regfile.GPRS[r1address];//r1value
        pipelineregde_ex[5]=regfile.GPRS[r2address];//r2value
    }

    private static void execute () {
        System.out.println("exucuting inst= "+pipelineregde_ex[0]+pipelineregde_ex[1]+pipelineregde_ex[2]);
        int r1=from8bitstonum(pipelineregde_ex[4]);
        int r2=from8bitstonum(pipelineregde_ex[5]);
        int r1address=Integer.parseInt(pipelineregde_ex[1],2);
        int r2address=Integer.parseInt(pipelineregde_ex[2],2);
        int immediate=getvalueof6bits(pipelineregde_ex[3]);
        int pc_value=from16bitstonum(regfile.PC);
        int result;
        String finalresult;

        switch (pipelineregde_ex[0])
        {
            case "0000":result=r1+r2;
            int  r11=r1&0x000000FF;
            int r22 =r2&0x000000FF;
            int temp=r11+r22;
            if ((temp&0x00000100)==0x00000100)
                C=1;
            else C=0;
            finalresult=fromnumto8bits(result);
            System.out.println("R"+r1address+"was="+from8bitstonum(regfile.GPRS[r1address])+"and got updated to"+from8bitstonum(finalresult));
            regfile.GPRS[r1address]=finalresult;
            if (from8bitstonum(finalresult)==0)
                Z=1;
            else Z=0;

            if (finalresult.charAt(0)==1)
                N=1;
            else N=0;
            if ((r1>=0&&r2>=0)||(r1<0&&r2<0))
            {
                if (r1>=0&&finalresult.charAt(0)=='1')
                    V=1;
                else V=0;
                if (r1<0&&finalresult.charAt(0)=='0')
                    V=1;
                else V=0;
            }else
                V=0;
             S=N^V;
            break;
            case "0001":result=r1-r2;
                finalresult=fromnumto8bits(result);
                System.out.println("R"+r1address+"was="+from8bitstonum(regfile.GPRS[r1address])+"and got updated to"+from8bitstonum(finalresult));
                regfile.GPRS[r1address]=finalresult;
                if (from8bitstonum(finalresult)==0)
                    Z=1;
                else Z=0;
                if (finalresult.charAt(0)==1)
                    N=1;
                else N=0;
                if (pipelineregde_ex[4].charAt(0)!=pipelineregde_ex[4].charAt(0))
                {
                    if (finalresult.charAt(0)==pipelineregde_ex[4].charAt(0))
                        V=1;
                    else V=0;
                }else V=0;
                S=N^V;
                break;
                case "0010":
                result=r1*r2;
                finalresult=fromnumto8bits(result);
                System.out.println("R"+r1address+"was="+from8bitstonum(regfile.GPRS[r1address])+"and got updated to"+from8bitstonum(finalresult));
                regfile.GPRS[r1address]=finalresult;
                if (from8bitstonum(finalresult)==0)
                    Z=1;
                else Z=0;

                if (finalresult.charAt(0)==1)
                    N=1;
                break;

            case "0011":
                System.out.println("R"+r1address+"was="+from8bitstonum(regfile.GPRS[r1address])+"and got updated to"+immediate);
                regfile.GPRS[r1address]=fromnumto8bits(immediate);
                break;
            case "0100":
                if (r1==0)
                {
                    System.out.println("PC register was="+pc_value+"and became="+(pc_value+immediate-1));
                    regfile.PC=fromnumto16bits(pc_value+immediate-1);//very imp!!!!!!
                }
                pipelineregfe_de="1111000000000000";
                pipelineregde_ex[0]="1111";
                break;
            case "0101":
                result=r1&immediate;//be carefull here revise it
                finalresult=fromnumto8bits(result);
                System.out.println("R"+r1address+"was="+from8bitstonum(regfile.GPRS[r1address])+"and got updated to"+from8bitstonum(finalresult));
                regfile.GPRS[r1address]=finalresult;
                if (from8bitstonum(finalresult)==0)
                    Z=1;
                else Z=0;

                if (finalresult.charAt(0)==1)
                    N=1;
                else N=0;
                break;
            case "0110":
                result=r1^r2;//be carefull here revise it
                finalresult=fromnumto8bits(result);
                System.out.println("R"+r1address+"was="+from8bitstonum(regfile.GPRS[r1address])+"and got updated to"+from8bitstonum(finalresult));
                regfile.GPRS[r1address]=finalresult;
                if (from8bitstonum(finalresult)==0)
                    Z=1;
                else Z=0;

                if (finalresult.charAt(0)==1)
                    N=1;
                else N=0;
                break;
            case "0111":
                System.out.println("PC register was="+pc_value+"and became="+(pipelineregde_ex[4]+pipelineregde_ex[5]));
                regfile.PC=pipelineregde_ex[4]+pipelineregde_ex[5];
                pipelineregfe_de="1111000000000000";
                pipelineregde_ex[0]="1111";
                break;
            case "1000":
                finalresult=shiftleft(pipelineregde_ex[4],immediate);
                System.out.println("R"+r1address+"was="+from8bitstonum(regfile.GPRS[r1address])+"and got updated to"+from8bitstonum(finalresult));
                regfile.GPRS[r1address]=finalresult;
                if (from8bitstonum(finalresult)==0)
                    Z=1;
                else Z=0;
                if (finalresult.charAt(0)==1)
                    N=1;
                else N=0;

                break;
            case "1001":
                finalresult=shiftright(pipelineregde_ex[4],immediate);
                System.out.println("R"+r1address+"was="+from8bitstonum(regfile.GPRS[r1address])+"and got updated to"+from8bitstonum(finalresult));
                regfile.GPRS[r1address]=finalresult;
                if (from8bitstonum(finalresult)==0)
                    Z=1;
                else Z=0;
                if (finalresult.charAt(0)==1)
                    N=1;
                else N=0;
                break;
            case "1010":
                if (immediate<0)
                    return;
                System.out.println("R"+r1address+"was="+from8bitstonum(regfile.GPRS[r1address])+"and got updated to"+from8bitstonum(datamem.dm[immediate]));
                regfile.GPRS[r1address]=datamem.dm[immediate];
                break;
            case "1011":
                if (immediate<0)
                    return;
                System.out.println("Data at address="+immediate+"was="+from8bitstonum(datamem.dm[immediate])+"got updated to"+from8bitstonum(regfile.GPRS[r1address]));
                datamem.dm[immediate]=regfile.GPRS[r1address];
                break;
            default:
                break;
        }
        regfile.SREG="000"+C+V+N+S+Z;
    }

public static String fromnumto16bits(int number)
{
    String y=Integer.toBinaryString(number);
    int n=y.length();
    for (int i = 0; i <16-n ; i++) {
        y="0"+y;
    }
    return y;
}
public static String fromnumto16bits2(int number)
{
    String bitString = Integer.toBinaryString(number);
    String paddedBitString = String.format("%16s", bitString).replace(' ', '0');
    return bitString;
}
public static String fromnumto8bits(int number)
{
    String bitString = Integer.toBinaryString(number & 0xFF);
    String paddedBitString = String.format("%8s", bitString).replace(' ', '0');
    return paddedBitString;
}
public static int getvalueof6bits(String binaryString)
{
    int value;
    if (binaryString.charAt(0) == '1') {
        StringBuilder complement = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i++) {
            char bit = binaryString.charAt(i);
            complement.append(bit == '0' ? '1' : '0');
        }
        value = -(Integer.parseInt(complement.toString(), 2) + 1);
    } else {
        value = Integer.parseInt(binaryString, 2);
    }
    return value;
}
public static String numTosixbits(int number)
{
        int sixBitRepresentation = number & 0x3F;
        if (number < 0) {
            sixBitRepresentation |= 0x20;
        }

        String s=Integer.toBinaryString(sixBitRepresentation);
        int n=s.length();
        for (int i=0;i<6-n;i++)
            s="0"+s;
        return s;
    }


    public static String shiftright(String s,int n)
    {
        String x=s.charAt(0)+"";
        for (int i = 0; i <n ; i++) {
            s=x+s;
        }
        s=s.substring(0,8);
        return s;
    }
    public static String shiftleft(String s,int n)
    {
        for (int i = 0; i <n ; i++) {
            s=s+"0";
        }
        s=s.substring(n,s.length());
        return s;
    }
    public static int from16bitstonum(String s)
    {
        short signedShort = (short) Integer.parseInt(s, 2);
        int result = signedShort;
       return result;

    }
    public static int from8bitstonum(String s)
    {
        byte signedByte = (byte) Integer.parseInt(s, 2);
        int result = signedByte;
        return result;
    }
}
