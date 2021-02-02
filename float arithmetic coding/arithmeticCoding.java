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


public class arithmeticCoding {


        public static void compress(File input) throws IOException {
            ArrayList<Character> chars =new ArrayList<>();

            ArrayList <Double> freq = new ArrayList<>();

            String word = "";
            int nextChar;
            int charIndex;
            int size=0;

            FileReader fr = new FileReader(input);
            BufferedReader br = new BufferedReader(fr);
            File compressed = new File("compressed.txt");

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



            compress(chars,freq,size,word);

        }


        public static void compress(ArrayList<Character> chars, ArrayList<Double> freq, int size, String data) throws IOException {
            PriorityQueue<node> nodes = new PriorityQueue<node>(chars.size(),new myComparator());

            for(int i=0 ; i<chars.size() ; i++){
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

     /*      for (int i =0 ; i<Arithmetic.size();i++){
                System.out.println(Arithmetic.get(i).data);

                System.out.println(Arithmetic.get(i).lowRange);

                System.out.println(Arithmetic.get(i).highRange);
                System.out.println("--");
            }
*/
            getBinaryCode(Arithmetic,data,size);


        }

        public static void getBinaryCode(ArrayList<node> arth,String word,int size) throws IOException {
            double lower = 0 ,upper = 1, range = 1;
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

            }

            double bCode=(upper+lower)/2;
            createCompressedFile(bCode,arth,size);
        }

    public static void createCompressedFile(double binaryCode,ArrayList<node> arth,int size) throws IOException {
        File compressed=new File("compressed.txt");
        FileWriter writer = new FileWriter(compressed);

        for(int i=0; i< arth.size(); i++) {
            writer.write(arth.get(i).data+ " - "+arth.get(i).lowRange+" - "+arth.get(i).highRange+"\n");
        }

        writer.write("Size - " +size);
        writer.write("\nBinaryCode - " +binaryCode);
        writer.close();
    }


    public static void decompress(File input) throws IOException {
        String line;
        double binaryCode = 0;
        int size=0;
        ArrayList<node> Arithmetic=new ArrayList<>();

        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);

        while ((line = br.readLine()) != null) {
            String[] data = line.split(" - ");

            if(data[0].equals("Size")){
                size= Integer.parseInt(data[1]);
                continue;
            }

            if(data[0].equals("BinaryCode")){
                binaryCode= Double.parseDouble(data[1]);
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


        getDecompressedData(Arithmetic,binaryCode,size);


    }

    public static void getDecompressedData(ArrayList<node> arth , Double binaryCode,int size){
        Double lower,upper,range;
        String word="";
       for(int i=0;i<size;i++){
            node temp = null;
            for (node node : arth) {
                if (node.lowRange<= binaryCode && node.highRange>= binaryCode ) {
                    temp = node;
                    word+=temp.data;
                    break;
                }
            }
            if (temp==null)break;

            lower=temp.lowRange;
            upper=temp.highRange;
            range = upper - lower;
            binaryCode=(binaryCode-lower)/range;

        }

        System.out.println(word);
    }





    public static void main(String[] args) throws IOException {
            File input = new File("compressed.txt");

            decompress(input);
        }







}
