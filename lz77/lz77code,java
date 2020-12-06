import java.io.*;

public class lz77 {
    StringBuffer searchWindow = new StringBuffer();

    public void compress(File input) throws IOException {
        String currentMatch="";
        String tag ;
        int matchingIndex=0,uniq = 0,tempIndex,nextChar,position,length;

        FileReader fr=new FileReader(input);
        BufferedReader br=new BufferedReader(fr);
        File compressed = new File("compressed.txt");
        FileWriter myWriter = new FileWriter("compressed.txt");

        while((nextChar=br.read())!=-1) {
                tempIndex=searchWindow.lastIndexOf(currentMatch+(char)nextChar);
                uniq=searchWindow.indexOf(String.valueOf((char) nextChar));

            if(tempIndex!=-1){
                currentMatch+=(char)nextChar;
                matchingIndex=tempIndex;
            }
            else{
                if(currentMatch.length()==0){
                    tag="0"+" - "+"0"+" - "+(char)nextChar;

                }
                else {
                     position = searchWindow.length() - matchingIndex;
                     length = currentMatch.length();
                    tag = String.valueOf(position);
                    tag+=" - "+length+" - "+(char)nextChar;
                }
                myWriter.write(tag);
                myWriter.write("\n");
                searchWindow.append(currentMatch+(char)nextChar);

                currentMatch="";
            }
        }

        if(matchingIndex!=0&& uniq!=-1) {
                 position = searchWindow.length() - matchingIndex;
                 length = currentMatch.length();
                tag = String.valueOf(position);
                tag += " - " + length + " - null";
            myWriter.write(tag);
        }
        myWriter.close();
        br.close();
        fr.close();
    }

    public StringBuffer decompress(File input) throws IOException {
        StringBuffer decompressed = new StringBuffer();
        FileReader file = new FileReader(input);
        BufferedReader br = new BufferedReader(file);
        String line, nextChar,word;
        int position, length, from ,to;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(" - ");
            position = Integer.parseInt(data[0]);
            length = Integer.parseInt(data[1]);
            nextChar = data[2];
            if(length==0){
                decompressed.append(nextChar);
            }
            else {
                if(nextChar.equals("null")) nextChar="";
                from=decompressed.length()-position;
                to=decompressed.length()-position+length;
                word=decompressed.substring(from,to)+nextChar;
                decompressed.append(word);
            }
        }
        return decompressed;
    }

    public static void main(String[] args) throws IOException {
        File i= new File("input.txt");
        File c= new File("compressed.txt");

        lz77 z = new lz77();
        z.compress(i);
        System.out.println(z.decompress(c));
    }
}
