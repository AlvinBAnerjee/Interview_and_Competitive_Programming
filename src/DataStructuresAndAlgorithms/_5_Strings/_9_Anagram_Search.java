package _5_Strings;

public class _9_Anagram_Search {
    static final int CHAR=256;
    static boolean areSame(int CT[],int CP[]){
        for(int i=0;i<CHAR;i++){
            if(CT[i]!=CP[i])return false;
        }
        return true;
    }

    static boolean isPresent(String txt,String pat){
        int[] CT=new int[CHAR];int[] CP=new int[CHAR];
        for(int i=0;i<pat.length();i++){
            CT[txt.charAt(i)]++;
            CP[pat.charAt(i)]++;
        }
        for(int i=pat.length();i<txt.length();i++){
            if(areSame(CT,CP))return true;
            CT[txt.charAt(i)]++;
            CT[txt.charAt(i-pat.length())]--;
        }
        return false;
    }
}
