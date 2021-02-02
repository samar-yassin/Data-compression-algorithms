import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


class node{
    double freq;
    char data;
    double lowRange;
    double highRange;
    node(char data , double freq){
        this.data=data;
        this.freq=freq;
    }


}


class myComparator implements Comparator<node> {

    @Override
    public int compare(node o1, node o2) {
        if (o1.data > o2.data)
            return 1;
        else if (o1.data < o2.data)
            return -1;
        return 0;

    }
}




public class binary {




        public static int getNoOfBits(ArrayList<node> arth){
            double range=1;
            for (node node : arth) {
                double temp= node.highRange-node.lowRange;
                if(temp<range) range=temp;
            }

            range=1/range;
            double power=0;
            int i=-1;
            while (range>power){
                i++;
                power=Math.pow(2,i);
            }

            return i;
        }

        public static double E1Scale(double num){
            return num*2;
        }

        public static double E2Scale(double num){
            return (num-.5)*2;
        }

        public static boolean needScaling(double lower ,double upper){
            return !(0.5 >= lower) || !(.5 <= upper);
        }

        public static int whichScale(double lower ,double upper){
            if(lower>.5 ) return 2;
            return 1;
        }

        public static void compress(File input) throws IOException {
            ArrayList<Character> chars =new ArrayList<>();

            ArrayList <Double> freq = new ArrayList<>();

            String word = "";
            int nextChar;
            int charIndex;
            int size=0;

            FileReader fr = new FileReader(input);
            BufferedReader br = new BufferedReader(fr);

            while ((nextChar = br.read()) != -1) {
                Character ch = (char)nextChar;
                word+=ch;
                size++;
                charIndex=chars.indexOf(ch);
                if(charIndex==-1){
                    chars.add(ch);
                    freq.add(1.);

                }
                else {
                    Double newFreq = freq.get(charIndex)+1;
                    freq.set(charIndex,newFreq);

                }




            }



            for(int i =0;i<freq.size();i++) {
                Double newFreq = freq.get(i)/size;
                freq.set(i,newFreq);
            }

            br.close();
            fr.close();



            compress(chars,freq,freq.size(),word);

        }


        public static void compress(ArrayList<Character> chars, ArrayList<Double> freq, int size, String data) throws IOException {
            PriorityQueue<node> nodes = new PriorityQueue<node>(size,new myComparator());

            for(int i=0 ; i<size ; i++){
                nodes.add(new node(chars.get(i), freq.get(i)));
            }

            double cumulativeFreq=0;

            ArrayList<node> Arithmetic=new ArrayList<>();

            while (nodes.size()!=0){

                node temp=nodes.peek();
                nodes.poll();

                node n = new node(temp.data,temp.freq);
                n.lowRange=cumulativeFreq;
                n.highRange=temp.freq+cumulativeFreq;

                cumulativeFreq=cumulativeFreq+temp.freq;

                Arithmetic.add(n);
            }
/*
           for (int i =0 ; i<Arithmetic.size();i++){
                System.out.println(Arithmetic.get(i).data);

                System.out.println(Arithmetic.get(i).lowRange);

                System.out.println(Arithmetic.get(i).highRange);
                System.out.println("--");
            }*/

            getBinaryCode(Arithmetic,data);



        }

        public static void getBinaryCode(ArrayList<node> arth,String word) throws IOException {
            double lower = 0 ,upper = 1, range = 1;
            String code="";
            for (int i =0 ; i<word.length();i++){
                char c =word.charAt(i);
                node temp = null;
                for (node node : arth) {
                    if (node.data == c) {
                        temp = node;
                        break;
                    }
                }

                upper = lower + (range * temp.highRange);
                lower = lower + (range * temp.lowRange);
                range = upper - lower;
            /*    System.out.println("data: "+temp.data);
                System.out.println("lower : " + lower);
                System.out.println("upper : " + upper);
                System.out.println("--");*/


                    while (needScaling(lower,upper)){
                    int scale = whichScale(lower,upper);
                    if(scale==1){
                        lower=E1Scale(lower);
                        upper=E1Scale(upper);
                        range = upper - lower;

                        code+="0";
                      /*  System.out.println("scale1");
                        System.out.println("data: "+temp.data);
                        System.out.println("lower : " + lower);
                        System.out.println("upper : " + upper);
                        System.out.println(code);
                        System.out.println("--");*/
                    }
                    else{
                        lower=E2Scale(lower);
                        upper=E2Scale(upper);
                        range = upper - lower;

                        code+="1";
                   /*     System.out.println("scale2");
                        System.out.println("data: "+temp.data);
                        System.out.println("lower : " + lower);
                        System.out.println("upper : " + upper);
                        System.out.println(code);
                        System.out.println("--");*/
                    }
                }
            }

            int nOfbits = getNoOfBits(arth);
            String Zeros="";
            while(nOfbits!=1){
                Zeros+="0";
                nOfbits--;
            }
            String  bCode=code+"1"+ Zeros;
            System.out.println(bCode);
           createCompressedFile(bCode,arth,word.length());
        }

        public static void createCompressedFile(String binaryCode,ArrayList<node> arth,int size) throws IOException {
            File compressed=new File("compressed.txt");
            FileWriter writer = new FileWriter(compressed);

            for(int i=0; i< arth.size(); i++) {
                writer.write(arth.get(i).data+ " - "+arth.get(i).lowRange+" - "+arth.get(i).highRange+"\n");
            }

            writer.write("BinaryCode - " +binaryCode);
            writer.write("\nnBits - "+getNoOfBits(arth));
            writer.write("\nSize - "+size);

            writer.close();
        }



    public static void decompress(File input) throws IOException {
        String line;
        String binaryCode = null;
        int nBits=0,size=0;
        ArrayList<node> Arithmetic=new ArrayList<>();

        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);

        while ((line = br.readLine()) != null) {
            String[] data = line.split(" - ");

            if(data[0].equals("nBits")){
                nBits= Integer.parseInt(data[1]);
                continue;
            }

            if(data[0].equals("BinaryCode")){
                binaryCode= data[1];
                continue;
            }

            if(data[0].equals("Size")){
                size= Integer.parseInt(data[1]);
                continue;
            }
            char c = data[0].charAt(0);
            node n = new node(c,0);
            n.lowRange= Double.parseDouble(data[1]);
            n.highRange= Double.parseDouble(data[2]);
            Arithmetic.add(n);
        }

        br.close();
        fr.close();


        getDecompressedData(Arithmetic,binaryCode,nBits,size);


    }

    public static void getDecompressedData(ArrayList<node> arth , String binaryCode,int nBits,int size){
        Double lower=0.0 ,upper=1.0,range=upper-lower;
        String word="";
        int shift=0;
        double prob=0;
        int bCode;

        bCode= Integer.parseInt(binaryCode.substring(shift,shift+nBits),2);
        prob=bCode/(Math.pow(2,nBits));

       for(int i=0;i<size;i++){
            node temp = null;
            for (node node : arth) {
                if (node.lowRange<= prob && node.highRange>= prob ) {
                    temp = node;
                    word+=temp.data;
                    break;
                }
            }

           upper = lower + (range * temp.highRange);
           lower = lower + (range * temp.lowRange);
           range = upper - lower;
           prob    =   (prob-lower)/range;



           while (needScaling(lower,upper)){
               int scale = whichScale(lower,upper);
               if(scale==1){

                   lower=E1Scale(lower);
                   upper=E1Scale(upper);
                   range = upper - lower;

                   shift++;
                   bCode= Integer.parseInt(binaryCode.substring(shift,shift+nBits),2);
                   prob=bCode/(Math.pow(2,nBits));
                   prob    =   (prob-lower)/range;




               }
               else{

                   lower=E2Scale(lower);
                   upper=E2Scale(upper);
                   range = upper - lower;

                   shift++;

                   bCode= Integer.parseInt(binaryCode.substring(shift,shift+nBits),2);
                   prob=bCode/(Math.pow(2,nBits));
                   prob    =   (prob-lower)/range;




               }
           }

        }

        System.out.println(word);
    }




        public static void main(String[] args) throws IOException {
            File input = new File("compressed.txt");

            decompress(input);
        }









}
