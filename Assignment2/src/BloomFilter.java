import java.io.File;

import java.util.Arrays;
import java.util.Scanner;

class BloomFilter {
    

    private HashFunctions [] hashFunctions;
    private final boolean[] bits;

    public BloomFilter(String m1String, String path){
        try{
            File file=new File(path);
            Scanner cs1=new Scanner(file);
            int counter=0;
            while (cs1.hasNextLine()){
                counter++; cs1.nextLine();
            }
            Scanner cs2=new Scanner(file);
            HashFunctions [] functions=new HashFunctions[counter];
            int i=0; while(cs2.hasNextLine()){
                String str=cs2.nextLine();
                String []line =str.split("_");
                HashFunctions function=new HashFunctions(line[0],line[1],m1String);
                functions[i]=function;
                i++;
            }
            this.hashFunctions=functions;
        }
        catch(Exception e){

        }
        this.bits=new boolean[Integer.parseInt(m1String)];
    }

    public void updateTable(String path) {
        //path for bad passwords file
        try {
            File file = new File(path);
            Scanner cs1 = new Scanner(file);
            while (cs1.hasNextLine()) {
                String badPass = cs1.nextLine();
                long badPassInteger = evaluateHorner(badPass);
                for (HashFunctions func : this.hashFunctions) {
                    long hi_l = func.activateFunction(badPassInteger);
                    int hi = (int) hi_l;
                    bits[hi] = true;
                }
            }
        }

        catch(Exception e){
            System.out.println("can't find the file");
        }
    }


    public static long evaluateHorner(String s)
    {
        int p= 15486907;
        long output=0;
        for(int i=0;i<s.length();i++){
            output=output*256%p+s.charAt(i)%p;
        }
        return output;
    }

    public String getFalsePositivePercentage(HashTable hashTable, String path) {
        try {
            File file = new File(path);
            Scanner sc=new Scanner(file);
            int sumAll=0;
            int sumBloom=0;
            int sumTable=0;
            while(sc.hasNextLine()){
                sumAll++;
                String pass=sc.nextLine();
                if(checkPassBloom(pass)) {sumBloom++;
                }
                if(hashTable.isPassExist(pass)) {sumTable++;
                }
            }
            double percentage=((double)(sumBloom-sumTable))/(double)(sumAll-sumTable);
            return String.valueOf(percentage);
        }
        catch(Exception e){
            return ("can't find the file in the path");
        }

    }

    public String getRejectedPasswordsAmount(String path) {
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);
            int sumBloom = 0;
            while(sc.hasNextLine()) {
                String pass = sc.nextLine();
                if (checkPassBloom(pass)) {
                    sumBloom++;
                }
            }
            return String.valueOf(sumBloom);
        }
        catch (Exception e) {
            return ("can't find the file in the path");
        }
    }

    private boolean checkPassBloom(String pass){
        long passInt=evaluateHorner(pass);
        boolean areEquals = true;
        for (HashFunctions func : this.hashFunctions ) {
            long hi_l=func.activateFunction(passInt);
            int hi =(int) hi_l;

            if(!bits[hi])
                areEquals=false;
        }
        return areEquals;
    }
}
