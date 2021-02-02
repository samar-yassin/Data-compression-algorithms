import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Vector;

class node{
    double freq;
    char data;
    node left, right;
    node(char data , double freq){
        this.data=data;
        this.freq=freq;
        left= null;
        right = null;
    }


}


class myComparator implements Comparator<node>{

    @Override
    public int compare(node o1, node o2) {
        if (o1.freq > o2.freq)
            return 1;
        else if (o1.freq < o2.freq)
            return -1;
        return 0;

    }
}


public class huffman {
    public static ArrayList<Character> chr=new ArrayList<>();
    public static ArrayList<String> codes=new ArrayList<>();
    public static String compressedData="";
    public static String deCompressedData="";





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

        size=chars.size();

        for(int i =0;i<size;i++) {
            Double newFreq = freq.get(i)/size;
            freq.set(i,newFreq);
        }

        br.close();
        fr.close();


        compress(chars,freq,size,word);

    }


    public static void compress(ArrayList<Character> chars, ArrayList<Double> freq, int size, String data) throws IOException {
        PriorityQueue<node> huffmanTree = new PriorityQueue<node>(size,new myComparator());

        for(int i=0 ; i<size ; i++){
            huffmanTree.add(new node(chars.get(i), freq.get(i)));
        }

        while (huffmanTree.size()!=1){
            node left=huffmanTree.peek();
            huffmanTree.poll();
           node right=huffmanTree.peek();
            huffmanTree.poll();


            node n = new node('#',left.freq+right.freq);
            n.left=left;
            n.right=right;

            huffmanTree.add(n);
        }
        getCodes(huffmanTree.peek(),"");
        System.out.println();

        printCompressedData(data);
        System.out.println(compressedData);

        createCompressedFile();

    }

    public static void getCodes(node root , String code) throws IOException {
        if (root.left == null && root.right == null && Character.isLetter(root.data)) {
            chr.add(root.data);
            codes.add(code);
            System.out.println(root.data+" : "+code);

            return;
        }






        getCodes(root.left,code+"0");
        getCodes(root.right,code+"1");
    }


    public static void printCompressedData(String word){
        compressedData="";
        for(int i= 0 ;i<word.length();i++){
                    compressedData += codes.get(chr.indexOf(word.charAt(i)));
        }
    }

    public static void createCompressedFile() throws IOException {
        File compressed=new File("compressed.txt");
        FileWriter writer = new FileWriter(compressed);

        for(int i=0; i< chr.size(); i++) {
            writer.write(chr.get(i)+ " - "+codes.get(i)+"\n");
        }

        writer.write("Data - " +compressedData);
        writer.close();
    }





    public static void deCompress(File input) throws IOException {
        chr.clear();
        codes.clear();
        compressedData="";

        String line;

        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);
        File compressed = new File("compressed.txt");

        while ((line = br.readLine()) != null) {
            String[] data = line.split(" - ");

            if(data[0].equals("Data")){
                compressedData= data[1];
                break;
            }

            String temp= data[0];
           chr.add(temp.charAt(0));
           codes.add(data[1]);

        }

        br.close();
        fr.close();


        calculateDeCompressedData();

    }

    public static void calculateDeCompressedData() throws IOException {
        String code = "";
        for(int i= 0 ;i<compressedData.length();i++){
            code+=compressedData.charAt(i);
            if(codes.indexOf(code)!=-1){
                deCompressedData +=chr.get(codes.indexOf(code));
                code="";
            }

        }
        createDeCompressedFile();
        System.out.println("\n"+deCompressedData);
    }

    public static void createDeCompressedFile() throws IOException {
        File compressed=new File("decompressed.txt");
        FileWriter writer = new FileWriter(compressed);

        writer.write(deCompressedData);
        writer.close();
    }




    public static void main(String[] args) throws IOException {
        File input = new File("decompressed.txt");

        compress(input);
    }





}
