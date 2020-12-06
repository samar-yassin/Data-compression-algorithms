import java.io.*;
import java.util.ArrayList;

public class lz78 {
    ArrayList<String> dict = new ArrayList<String>();

        public void compress(File input) throws IOException {
            String currentMatch="",tag;
            int matchingIndex=0,tempIndex,nextChar,uniq = 0;
            dict.add("");

            FileReader fr=new FileReader(input);
            BufferedReader br=new BufferedReader(fr);
            File compressed = new File("compressed.txt");
            FileWriter myWriter = new FileWriter("compressed.txt");

            while((nextChar=br.read())!=-1) {
                tempIndex=dict.indexOf(currentMatch+(char)nextChar);
                uniq=dict.indexOf(String.valueOf((char) nextChar));

                if(tempIndex!=-1){
                    currentMatch+=(char)nextChar;
                    matchingIndex=tempIndex;
                }
                else{
                    if(currentMatch.length()==0){
                        tag="0"+" - "+(char)nextChar;

                    }
                    else {
                        tag=matchingIndex+" - "+(char)nextChar;
                    }
                    myWriter.write(tag);
                    myWriter.write("\n");
                    dict.add(currentMatch+(char)nextChar);
                    currentMatch="";
                }
            }
            if(matchingIndex!=0&& uniq!=-1) {
                tag=matchingIndex+" - null";
                myWriter.write(tag);
            }
            myWriter.close();
            br.close();
            fr.close();
        }

        public StringBuffer decompress(File input) throws IOException {
            StringBuffer decompressed = new StringBuffer();
            dict = new ArrayList<String>();
            dict.add("");
            FileReader file = new FileReader(input);
            BufferedReader br = new BufferedReader(file);
            String line, nextChar,word;
            int index;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" - ");
                index = Integer.parseInt(data[0]);
                nextChar = data[1];
                if(index==0){
                    decompressed.append(nextChar);
                    dict.add(nextChar);
                }
                else {
                    if(nextChar.equals("null")) nextChar="";
                    word=dict.get(index)+nextChar;
                    decompressed.append(word);
                    dict.add(word);
                }
            }
            return decompressed;
        }

        public static void main(String[] args) throws IOException {
            File i= new File("input.txt");
          File c= new File("compressed.txt");
            lz78 z= new lz78();
            z.compress(i);
            System.out.println(z.decompress(c));
        }
}
