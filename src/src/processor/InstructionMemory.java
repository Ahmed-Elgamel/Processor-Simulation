package src.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InstructionMemory {
    String[] im;
     int numoflines;
    public InstructionMemory()
    {
        ///Users/youssefalaa/Downloads/TestCases.txt
        im=new String[1024];
        fill();
        numoflines=0;
    }
    public void fill()
    {
        String s="0000000000000000";
        for (int i = 0; i <1024 ; i++)
            im[i]=s;
    }
    public  void read_textfile()
    {
        String filePath = "TestCases.txt";
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
                numoflines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0;i<numoflines;i++)
        {
            String concatenatedBits;
            String num1;
            String num2;
            String temp[]=lines.get(i).split(" ");
            String  opcode=getOpCode(temp[0]);
            if(temp[0].equals("ADD")||temp[0].equals("SUB")||temp[0].equals("MUL")||temp[0].equals("EOR")||temp[0].equals("BR"))
            {
                String numericValueString1 = temp[1].substring(1); // Extract substring starting from index 1
                int numericValue1 = Integer.parseInt(numericValueString1);
                String numericValueString2 = temp[2].substring(1); // Extract substring starting from index 1
                int numericValue2 = Integer.parseInt(numericValueString2);
                if (numericValue1>63||numericValue1<0||numericValue2>63||numericValue2<0)
                {
                    System.out.println("invalid instruction");
                    return;
                }
                 num1=numTosixbits(numericValue1);
                 num2=numTosixbits(numericValue2);
                concatenatedBits=opcode+num1+num2;




            }else
            {
                String numericValueString1 = temp[1].substring(1); // Extract substring starting from index 1
                int numericValue1 = Integer.parseInt(numericValueString1);
                int numericValue2=Integer.parseInt(temp[2]);
                if (numericValue1>63||numericValue1<0||numericValue2>31||numericValue2<-32)
                {
                    System.out.println("invalid instruction");
                    return;
                }
                num1=numTosixbits(numericValue1);
                num2=numTosixbits(numericValue2);
                concatenatedBits=opcode+num1+num2;


            }
            im[i]= concatenatedBits;
        }
    }
    public static String getOpCode(String s)
    {
        String r;
        switch (s)
        {
            case "ADD":r="0000";
                break;
            case "SUB":
                r="0001";break;
            case "MUL":r="0010";break;
            case "MOVI":r="0011";break;
            case "BEQZ":r="0100";break;
            case "ANDI": r="0101";break;
            case "EOR":r="0110";break;
            case "BR":r="0111";break;
            case "SAL":r="1000";break;
            case "SAR":r="1001";break;
            case "LDR":r="1010";break;
            case "STR":r="1011";break;
            default:r="1111";
        }
        return r;
    }


    public String numTosixbits(int number)
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






//    public static String ShortToBits(short value) {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 15; i >= 0; i--) {
//            int mask = 1 << i;
//            int bit = (value & mask) != 0 ? 1 : 0;
//            sb.append(bit);
//        }
//
//        return sb.toString();
//    }
//    //    public static void main(String[] args) {
////     //   read_textfile();
////
////        short concatenatedBits = (short) ((10 << 12) | (1 << 6) | 2);
////
////     //   String bits = ShortToBits(concatenatedBits);
////        //   System.out.println("Bits: " + bits);
////     //   System.out.println("Concatenated bits: " + Integer.toBinaryString(concatenatedBits));
////
////
////    }

   
    




}
