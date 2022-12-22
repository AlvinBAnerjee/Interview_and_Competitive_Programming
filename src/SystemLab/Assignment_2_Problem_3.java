package SystemLab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;


public class Assignment_2_Problem_3 {
    static class FileOperations{
        String filename;
        int inode;
        ArrayList<String> disk_blocks;

        FileOperations(String filename, int inode, ArrayList<String> disk_blocks){
            this.filename = filename;
            this.inode = inode;
            this.disk_blocks = new ArrayList<>(disk_blocks);
        }
    }

    static ArrayList<FileOperations> files;
    static final String root = "root/";
    static final int blockSize = 4;
    static void makeFile(String command) throws IOException {
        int commandLength = command.length();
        int cntSpaces = 0;
        String inst = "", filename = "", content = "";

        //Parsing the command
        for(int i=0; i<commandLength; ++i){
            char c = command.charAt(i);
            if(c == ' ') cntSpaces ++;
            else if(cntSpaces == 0 && c != ' ') inst += c;
            else if(cntSpaces == 1 && c != ' ') filename += c;
            else if(cntSpaces == 2) { content = command.substring(i+1, commandLength-1); break; }
        }
        int contentLength = content.length();

        //Checking if file with same name exists
        for(FileOperations f : files){
            if(f.filename.equals(filename)){
                System.out.println("File with same name already exists!");
                return;
            }
        }

        //Generating blocks
        ArrayList<String> disk_blocks = new ArrayList<>();
        FileOperations f;
        FileWriter fileWriter;
        Random random = new Random();
        for(int i=0; i<contentLength; ++i){
            int cnt = 0;
            String block = root + Integer.toString(random.nextInt(1000001)) + ".txt";
            disk_blocks.add(block);
            fileWriter = new FileWriter("src/SystemLab/"+block);
            while(cnt < blockSize && i < contentLength){
                fileWriter.write(content.charAt(i));
                i ++;
                cnt ++;
            }
            fileWriter.close();
            i --;
        }

        f = new FileOperations(filename, random.nextInt(1000001), disk_blocks);
        files.add(f);
        System.out.println("Files created successfully!");
    }

    static void printFile(String command) throws FileNotFoundException {
        int commandLength = command.length();
        int cntSpaces = 0;
        String inst = "", filename = "";

        //Parsing the command
        for(int i=0; i<commandLength; ++i){
            char c = command.charAt(i);
            if(c == ' ') cntSpaces ++;
            else if(cntSpaces == 0 && c != ' ') inst += c;
            else if(cntSpaces == 1) { filename = command.substring(i, commandLength); break; }
        }


        //Fetching the disk blocks
        ArrayList<String> disk_blocks=null;
        for(FileOperations f : files) {
//            System.out.println(f.filename);
            if (f.filename.equals(filename)) {
                disk_blocks = new ArrayList<>(f.disk_blocks);
                break;
            }
        }

        //Accumulating all the inodes to get the string
        String content = "";
        for(String s : disk_blocks){
            File text = new File("src/SystemLab/"+s);
            Scanner sc=new Scanner(text);
            while (sc.hasNext())
            content=content+sc.nextLine();

        }
        System.out.println(content);
    }

    static void deleteFile(String command){
        int commandLength = command.length();
        int cntSpaces = 0;
        String inst = "", filename = "";

        //Parsing the command
        for(int i=0; i<commandLength; ++i){
            char c = command.charAt(i);
            if(c == ' ') cntSpaces ++;
            else if(cntSpaces == 0 && c != ' ') inst += c;
            else if(cntSpaces == 1) { filename = command.substring(i, commandLength); break; }
        }

        //Fetching the disk blocks
        ArrayList<String> disk_blocks=null;
        for(FileOperations f : files){
            if(f.filename.equals(filename)){
                disk_blocks = new ArrayList<>(f.disk_blocks);
                break;
            }
        }

        //Deleting the blocks
        for(String block : disk_blocks){
            File file = new File("src/SystemLab/" + block);
            file.delete();
        }

        //Deleting from the datastructure
        int i=0;
        while (true){
            if (i==files.size())
                break;
            if(filename.equals(files.get(i).filename)) {
                files.remove(i);
                i--;
            }
            i++;
        }

        System.out.print("File deleted successfully!\n");
    }

    static void renameFile(String command){
        int commandLength = command.length();
        int cntSpaces = 0;
        String inst = "", filenameBefore = "", filenameAfter = "";

        //Parsing the command
        for(int i=0; i<commandLength; ++i){
            char c = command.charAt(i);
            if(c == ' ') cntSpaces ++;
            else if(cntSpaces == 0 && c != ' ') inst += c;
            else if(cntSpaces == 1 && c != ' ') filenameBefore += c;
            else if(cntSpaces == 2) { filenameAfter = command.substring(i, commandLength); break; }
        }

        for(int i=0; i<files.size(); ++i){
            if(files.get(i).filename.equals(filenameBefore))
                files.get(i).filename=filenameAfter;
        }
    }

    static void listFile(String command){
        for(FileOperations f : files){
            System.out.print(f.filename + " " + f.inode + "\n");
        }
    }

    public static void main(String args[]) throws IOException {
        files = new ArrayList<FileOperations>();
        Scanner scanner = new Scanner(System.in);

        char ch;
        while(true) {
            String command = scanner.nextLine();
            String inst = "";

            //Extracting instruction from the command
            for(int i=0; i<command.length(); ++i){
                char c = command.charAt(i);
                if(c != ' ') inst += c;
                else break;
            }

            if(inst.equals("mf")) makeFile(command);
            else if(inst.equals("pf")) printFile(command);
            else if(inst.equals("df")) deleteFile(command);
            else if(inst.equals("rf")) renameFile(command);
            else if(inst.equals("ls")) listFile(command);

            System.out.print("\nwanna try again[y/n]?");
            ch = scanner.next().charAt(0);
            if(ch == 'n' || ch == 'N') break;

            scanner.nextLine();
        }
    }
}
