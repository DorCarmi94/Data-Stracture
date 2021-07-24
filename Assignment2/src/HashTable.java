import java.io.File;
import java.util.Scanner;

class HashTable {

    private final HashList [] hashLists;
    private final int m2;

    public HashTable(String m2){
        try {
            this.hashLists = new HashList[Integer.parseInt(m2)];
            for (int i = 0; i < hashLists.length; i++) {
                hashLists[i] = new HashList();
            }
            this.m2 = Integer.parseInt(m2);
        }
        catch (Exception e){
            throw new IllegalArgumentException("the input is not a number: +" +m2);
        }
    }

    public void updateTable(String path)
    {

        try{
            File file=new File(path);
            Scanner cs1=new Scanner(file);
            while(cs1.hasNextLine()){
                String newPass=cs1.nextLine();
                int hashForNewPass=(int)(this.hashFunction(newPass))%Integer.MAX_VALUE;
                this.hashLists[hashForNewPass].Add(newPass);
            }

        }
        catch(Exception e){
            System.out.println("can't find the file");
        }
    }

    private long hashFunction(String s){
        long sInt= BloomFilter.evaluateHorner(s);
        return sInt%m2;
    }

    public boolean isPassExist(String pass) {
        int passkey=(int)(hashFunction(pass))% Integer.MAX_VALUE;
        return(hashLists[passkey].isExist(pass));
    }

    public String getSearchTime(String s) {
        try {
            File file = new File(s);
            Scanner cs = new Scanner(file);
            long start= System.nanoTime();
            while (cs.hasNextLine()){
                String check=cs.nextLine();
                this.isPassExist(check);
            }
            long end=System.nanoTime();
            Long diff=end-start;
            double diffInDouble= diff.doubleValue();
            diffInDouble=diffInDouble/1000000;
            return String.format("%.4f",diffInDouble);
        }
        catch (Exception e){
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement st:stackTrace) {
                System.out.println(st.toString());
            }
            return null;
        }
    }
}
