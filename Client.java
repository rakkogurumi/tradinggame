import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;


public class Client {
    //�|�[�g��10007
    public static final int ECHO_PORT = 10007;

    public static void main(String args[]) {
        Socket socket = null;
        //�����Ȃ���localhost�Ɏ����ڑ�����B
        //�T�[�o�[�����w�肷��ꍇ�́A"java Client [�T�[�o�[��]"
        try {
            if(args.length==0) socket = new Socket("localhost", ECHO_PORT);
            else socket = new Socket(args[0], ECHO_PORT);
            System.out.println("�ڑ����܂���"+ socket.getRemoteSocketAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
            Scanner sc = new Scanner(System.in);
            String input;
            String line;
            String name;
            int highscore=0;
            int turn=1; //�^�[����

            //�����̖��O����͂��ăT�[�o�[�ɑ��M
            System.out.println("�v���C���[������͂��Ă��������B");
            input = keyIn.readLine();
            name = input;
            //�T�[�o�[����̕ԐM���󂯎��B
            out.println(name);
            line = in.readLine();
            System.out.println(line);

            //�v���C���[�ԍ��ƃv���C���[�l�����󂯎��B
            int p_num = Integer.parseInt(in.readLine());
            int players = Integer.parseInt(in.readLine());
            int command;
            //p1,p2,p3�͊����B
            int p1;
            int p2;
            int p3;
            int temp;
            int mymoney=10000; //������
            int mys1=0,mys2=0,mys3=0; //������

            try{
                System.out.println("���݂̂��Ȃ��̃n�C�X�R�A");
                File file1 = new File("hs_name.txt");
                FileReader filereader1 = new FileReader(file1);
                int ch;
                while((ch = filereader1.read()) != -1){
                    System.out.print((char)ch);
                }
                System.out.print(":");
                filereader1.close();
                File file2 = new File("highscore.txt");
                FileReader filereader2 = new FileReader(file2);

                while((ch = filereader2.read()) != -1){
                    System.out.print((char)ch);
                    highscore = highscore*10+(((int)ch)-48);
                }
                System.out.println("yen");
                filereader2.close();
                System.out.println("------------------");
                System.out.println();
            }catch(FileNotFoundException e){
                System.out.println(e);
            }catch(IOException e){
                System.out.println(e);
            }

            //�Ƃ肠����10�^�[��
            while (turn<=10){
                System.out.println("---------------");
                System.out.println();
                line = in.readLine();
                System.out.println(line);
                System.out.println();
                System.out.println("�t�F�C�Y1:���w���t�F�C�Y");
                for(int i=0;i<3;i++){
                    line = in.readLine();
                    System.out.println(line);
                }

                System.out.println("---------------");
                System.out.println("�v���C���[��");
                for(int i=0;i<players;i++){
                    line = in.readLine();
                    System.out.println(line);
                }

                out.println(p_num);
                out.flush();
                System.out.printf("��1�̏�����:%4d ",Integer.parseInt(in.readLine()));
                System.out.printf("��2�̏�����:%4d ",Integer.parseInt(in.readLine()));
                System.out.println("��3�̏�����:"+Integer.parseInt(in.readLine()));   

                System.out.println("---------------");
                p1 = Integer.parseInt(in.readLine());
                p2 = Integer.parseInt(in.readLine());
                p3 = Integer.parseInt(in.readLine());

                //PHASE1 Buying stocks
                while(true){
                    System.out.println("�R�}���h����͂��Ă�������");
                    System.out.println("1:��1�𔃂� 2:��2�𔃂� 3:��3�𔃂� 4:����Ȃ�");
                    input = keyIn.readLine();
                    out.println(input);
                    out.flush();
                    try{
                        command = Integer.parseInt(input);
                    }
                    catch(NumberFormatException nfe){
                        command =0;
                    }

                    if(command==1){
                        System.out.println("�w�����銔1�̖�������͂��Ă��������B �w�����E��:"+(int)mymoney/p1);
                        System.out.println("�L�����Z������ꍇ��0����͂��Ă��������B");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mymoney/p1 && temp>0){
                            out.println("OK");
                            System.out.println("��1��"+temp+"���w�����܂����B");
                            System.out.println();
                            mymoney-=temp*p1;
                            mys1+=temp;
                            out.println(temp);
                            out.println(p_num);
                            out.println(mymoney);
                            out.flush();
                            break;
                        }
                        else{
                            out.println("NG");
                            System.out.println("�������𒆒f���܂����B");
                        }
                    }  
                    else if(command==2){
                        System.out.println("�w�����銔2�̖�������͂��Ă��������B �w�����E��:"+(int)mymoney/p2);
                        System.out.println("�L�����Z������ꍇ��0����͂��Ă��������B");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mymoney/p2 && temp>0){
                            out.println("OK");
                            System.out.println("��2��"+temp+"���w�����܂����B");
                            System.out.println();
                            mymoney-=temp*p2;
                            mys2+=temp;
                            out.println(temp);
                            out.println(p_num);
                            out.println(mymoney);
                            out.flush();
                            break;
                        }
                        else{
                            out.println("NG");
                            System.out.println("�������𒆒f���܂����B");
                        }
                    }
                    else if(command==3){
                        System.out.println("�w�����銔3�̖�������͂��Ă��������B �w�����E��:"+(int)mymoney/p3);
                        System.out.println("�L�����Z������ꍇ��0����͂��Ă��������B");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mymoney/p3 && temp>0){
                            out.println("OK");
                            System.out.println("��3��"+temp+"���w�����܂����B");
                            System.out.println();
                            mymoney-=temp*p3;
                            mys3+=temp;
                            out.println(temp);
                            out.println(p_num);
                            out.println(mymoney);
                            out.flush();
                            break;
                        }
                        else{
                            out.println("NG");
                            System.out.println("�������𒆒f���܂����B");
                        }
                    }
                    else if(command==4){
                        System.out.println("�����p�t�F�C�Y�Ɉڍs���܂��B");
                        break;
                    }
                    else    System.out.println(input+"�͖����ȓ��͂ł��B");
                }

                //PHASE2 Selling stocks
                System.out.println("---------------\n");
                System.out.println();
                System.out.println("�t�F�C�Y2�F�����p�t�F�C�Y");

                for(int i=0;i<3;i++){
                    line = in.readLine();
                    System.out.println(line);
                }

                System.out.println("---------------");
                System.out.println("�v���C���[�̏�");
                for(int i=0;i<players;i++){
                    line = in.readLine();
                    System.out.println(line);
                }

                out.println(p_num);
                System.out.printf("��1�̏�����:%4d ",Integer.parseInt(in.readLine()));
                System.out.printf("��2�̏�����:%4d ",Integer.parseInt(in.readLine()));
                System.out.println("��3�̏�����:"+Integer.parseInt(in.readLine()));   

                System.out.println("---------------");

                while(true){
                    System.out.println("�R�}���h����͂��Ă�������");
                    System.out.println("1:��1�𔄂� 2:��2�𔄂� 3:��3�𔄂� 4:����Ȃ�");
                    input = keyIn.readLine();
                    out.println(input);
                    try{
                        command = Integer.parseInt(input);
                    }
                    catch(NumberFormatException nfe){
                        command =0;
                    }
                    if(command==1){
                        System.out.println("���p���銔1�̖�������͂��Ă��������B�ő唄�p��:"+mys1);
                        System.out.println("�L�����Z������ꍇ��0����͂��Ă��������B");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mys1 && temp>0){
                            out.println("OK");
                            System.out.println("��1��"+temp+"�����p���܂����B");
                            System.out.println();
                            mymoney+=temp*p1;
                            mys1+=temp;
                            out.println(temp);
                            out.println(p_num);
                            out.println(mymoney);
                            out.flush();
                            break;
                        }
                        else{
                            out.println("NG");
                            System.out.println("�������𒆎~���܂����B");
                        }
                    }  
                    else if(command==2){
                        System.out.println("���p���銔2�̖�������͂��Ă��������B�ő唄�p��:"+mys2);
                        System.out.println("�L�����Z������ꍇ��0����͂��Ă��������B");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mys2 && temp>0){
                            out.println("OK");
                            System.out.println("��2��"+temp+"�����p���܂���.");
                            System.out.println();
                            mymoney+=temp*p2;
                            mys2-=temp;
                            out.println(temp);
                            out.println(p_num);
                            out.println(mymoney);
                            out.flush();
                            break;
                        }
                        else{
                            out.println("NG");
                            System.out.println("�������𒆎~���܂����B");
                        }
                    }
                    else if(command==3){
                        System.out.println("���p���銔3�̖�������͂��Ă��������B�ő唄�p��:"+mys3);
                        System.out.println("�L�����Z������ꍇ��0����͂��Ă��������B");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mys3 && temp>0){
                            out.println("OK");
                            System.out.println("��3��"+temp+"�����p���܂����B");
                            System.out.println();
                            mymoney+=temp*p3;
                            mys3-=temp;
                            out.println(temp);
                            out.println(p_num);
                            out.println(mymoney);
                            out.flush();
                            break;
                        }
                        else{
                            out.println("NG");
                            System.out.println("�������𒆎~���܂����B");
                        }
                    }
                    else if(command==4){
                        System.out.println("���̃^�[���Ɉڍs���܂��B");
                        break;
                    }
                    else    System.out.println(input+"�͖����ȓ��͂ł��B");
                }

                turn++;
            }
            //Game end.
            System.out.println("�ŏI�����ϓ�");
            System.out.println("---------------");
            for(int i=0;i<3;i++){
                line = in.readLine();
                System.out.println(line);
            }


            p1 = Integer.parseInt(in.readLine());
            p2 = Integer.parseInt(in.readLine());
            p3 = Integer.parseInt(in.readLine());
            int fortune = p1*mys1+p2*mys2+p3*mys3+mymoney;
            System.out.println("���Ȃ��̍ŏI�����Y�́A"+fortune+"yen �ł��B");

            System.out.println();
            System.out.println("--------------------");
            System.out.println("�ŏI�v���C���[��");
            for(int i=0;i<players;i++){
                input = in.readLine();
                System.out.println(input);
            }
            System.out.println("--------------------");
            System.out.println();

            int winner;
            winner = Integer.parseInt(in.readLine());
            if(p_num==winner){
                System.out.println("���Ȃ��̏����ł��I");
                input = in.readLine();
            }
            else{
                input = in.readLine();
                System.out.println("���Ȃ��̕����ł�...");
                System.out.println(input);
            }

            if(fortune>highscore){
                System.out.println("�n�C�X�R�A���X�V����܂����B");
                try{
                    File file1 = new File("hs_name.txt");
                    FileWriter filewriter1 = new FileWriter(file1);
                    filewriter1.write(name);
                    filewriter1.close();
                    File file2 = new File("highscore.txt");
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file2)));
                    pw.write(String.valueOf(fortune));
                    pw.close();
                }
                catch(IOException e){
                    System.out.println(e);
                }
            }
            System.out.println("�I��");
            out.println("�v���C���["+(p_num+1)+":"+name+"�̓��O�A�E�g���܂����B");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                if(socket != null)  socket.close();
            }
            catch (IOException e) {}
            System.out.println("�ؒf����܂��� " + socket.getRemoteSocketAddress());
        }
    }
}