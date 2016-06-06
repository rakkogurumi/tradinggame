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

//�O���[�o���ϐ��̕����B(�}���`�X���b�h�ł̓C���X�^���X�̕�����������)
class Global{
    public static int i=0;
}

public class Server {
    //PORT�ԍ���10007
    public static final int ECHO_PORT = 10007;


    public static void main(String[] args){
        ServerSocket serverSocket = null;
        BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));

        

        try{
            serverSocket = new ServerSocket(ECHO_PORT);
            System.out.println("Server���N�����܂���(port="+ serverSocket.getLocalPort() + ")");
            Scanner sc = new Scanner(System.in);
            
            int chk=0;
            while(chk==0){
                try{
                    System.out.println("�v���C���[�l������͂��Ă��������B");
                    Player.players = Integer.parseInt(keyIn.readLine());
                    chk++;
                }
                catch(NumberFormatException nfe){
                    System.out.println("��������͂��Ă��������B");
                }
            }
            Player.p_num=0; //�v���C���[�ԍ�
            System.out.println("�N���C�A���g��T���Ă��܂��B");

            for (int i=0;i<Player.players;i++) {
                Socket socket = serverSocket.accept();
                new EchoThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {}
        }
    }
}

class EchoThread extends Thread {
    ServerSocket serverSocket = null;
    Wait wt = new Wait();

    private Socket socket;

    public EchoThread(Socket socket) {
        this.socket = socket;
        System.out.println("�ڑ�����܂��� "
                + socket.getRemoteSocketAddress());
    }

     public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String line;
            int temp1,temp2,temp3;

            //stocks�̏�����
            for(int i=0;i<Player.players;i++) for(int j=0;j<3;j++) Player.putstocks(i,j,0);

            //�v���C���[���ŏ��Ɏw�肵���l���������܂�login��҂���(�ꍇ�ɂ���Ă͕s�K�v)
            int i=0;
            //while(true){
               // if(Global.i>=Player.players) break;
                while((line = in.readLine())!=null){
                    System.out.println("�v���C���["+(Global.i+1)+":"+line+"�����O�C�����܂����B");
                    Player.putname(Global.i,line);
                    Global.i++;
                    break;
                }
            //}

            int command;
            int turn=1; //�^�[����
            for(i=0;i<Player.players;i++) Player.putmoney(i,10000); //�������̏������B
            out.println("���Ȃ��̓v���C���["+(Player.p_num+1)+"�ł��B"); //�v���C���[�ԍ������m
            out.println(Player.p_num);                       //���łɃv���C���[�ԍ����N���C�A���g�ɑ���B
            Player.p_num++;
            out.println(Player.players);
            out.flush();

            wt.waitLogin();




            //�Ƃ肠����10�^�[���s�����Ƃɂ���B(�����ݒ�Ō��߂Ă�������������Ȃ�)
            while (turn<=10){
                //Price.changePrice(); //�܂�������ϓ�������B
                //�^�[���J�n�錾�ƁA���݂̊����A���������A���������N���C�A���g�ɑ���A�N���C�A���g���ŕ\��������B
                System.out.println(Thread.currentThread()+"Turn"+turn+" start.");
                out.println("Turn"+turn+" start.");
                if(Price.fluc1>=0)out.println("��1:"+Price.price1+"yen.(+"+Price.fluc1+")");
                else out.println("��1:"+Price.price1+"yen.("+Price.fluc1+")");
                if(Price.fluc2>=0)out.println("��2:"+Price.price2+"yen.(+"+Price.fluc2+")");
                else out.println("��2:"+Price.price2+"yen.("+Price.fluc2+")");
                if(Price.fluc3>=0)out.println("��3:"+Price.price3+"yen.(+"+Price.fluc3+")");
                else out.println("��3:"+Price.price3+"yen.("+Price.fluc3+")");

                int score;
                for(i=0;i<Player.players;i++){
                    score = (Player.getmoney(i)+Price.price1*Player.getstocks(i,0)+Price.price2*Player.getstocks(i,1)+Price.price3*Player.getstocks(i,2));
                    out.println(Player.getname(i)+": "+score+"yen.");
                }

                //�v���C���[�ԍ����󂯎��A���̃v���C���[�̎�������Ԃ��B
                temp1 = Integer.parseInt(in.readLine());
                out.println(Player.getstocks(temp1,0));
                out.println(Player.getstocks(temp1,1));
                out.println(Player.getstocks(temp1,2));
                out.flush();
                //�N���C�A���g�Ɍ��݂̊����������镔���B
                out.println(Price.price1);
                out.println(Price.price2);
                out.println(Price.price3);
                out.flush();

                //�O���̊��𔃂��t�F�C�Y
                while(true){
                    //�N���C�A���g�̑I�����󂯎��B
                    line = in.readLine();
                    //�����ȊO����͂��Ă������I�����Ȃ��悤��try-catch����B
                    try{
                        command = Integer.parseInt(line);
                    }
                    catch(NumberFormatException nfe){
                        command = 0;
                    }
                    //[1]��1�𔃂��ꍇ
                    if(command==1){
                        //�N���C�A���g���K�؂Ȑ��l����͂����ꍇ�̂݁A�ȍ~�̏����Ɉڂ�B
                        //��肭�ǂ����A��������Ȃ��Ǝ�M�������M���ƂȂ�A���ʃt���[�Y����B
                        line = in.readLine();
                        if(line.equals("OK")){
                            //System.out.println("OK1");
                            //3�̕ϐ�(�v���C���[�ԍ�,�w����,�w����̋��z)���N���C�A���g����󂯎��B
                            temp1 = Integer.parseInt(in.readLine());
                            temp2 = Integer.parseInt(in.readLine());
                            temp3 = Integer.parseInt(in.readLine());
                            //3�̕ϐ������ƂɁA�f�[�^�X�V
                            Player.putstocks(temp2,0,Player.getstocks(temp2,0)+temp1);
                            Player.putmoney(temp2,temp3);
                                Player.wait_num++;
                                wt.waitPlayer();
                            break;
                        }
                        else{/*System.out.println("NG1");*/}
                    }
                    else if(command==2){
                        line = in.readLine();
                        if(line.equals("OK")){
                            //System.out.println("OK2");
                            temp1 = Integer.parseInt(in.readLine());
                            temp2 = Integer.parseInt(in.readLine());
                            temp3 = Integer.parseInt(in.readLine());
                            Player.putstocks(temp2,1,Player.getstocks(temp2,1)+temp1);
                            Player.putmoney(temp2,temp3);
                                Player.wait_num++;
                                wt.waitPlayer();
                            break;
                        }
                        else{/*System.out.println("NG2");*/}
                    }
                    else if(command==3){
                        line = in.readLine();
                        if(line.equals("OK")){
                            //System.out.println("OK3");
                            temp1 = Integer.parseInt(in.readLine());
                            temp2 = Integer.parseInt(in.readLine());
                            temp3 = Integer.parseInt(in.readLine());
                            Player.putstocks(temp2,2,Player.getstocks(temp2,2)+temp1);
                            Player.putmoney(temp2,temp3);
                                Player.wait_num++;
                                wt.waitPlayer();
                            break;
                        }
                        else{/*System.out.println("NG3");*/}
                    }
                    //[4]�������Ȃ��@��I�񂾏ꍇ�͑����Ɏ��ɐi�ށB
                    else if(command==4) {
                                Player.wait_num++;
                                wt.waitPlayer();
                      break;
                    }
                    else {/*System.out.println("invalid.");*/}
                }

                //�㔼�B���𔄂�t�F�C�Y�B
                //�ēx�󋵂��N���C�A���g�ɑ��M
                if(Price.fluc1>=0)out.println("��1:"+Price.price1+"yen.(+"+Price.fluc1+")");
                else out.println("��1:"+Price.price1+"yen.("+Price.fluc1+")");
                if(Price.fluc2>=0)out.println("��2:"+Price.price2+"yen.(+"+Price.fluc2+")");
                else out.println("��2:"+Price.price2+"yen.("+Price.fluc2+")");
                if(Price.fluc3>=0)out.println("��3:"+Price.price3+"yen.(+"+Price.fluc3+")");
                else out.println("��3:"+Price.price3+"yen.("+Price.fluc3+")");

                for(i=0;i<Player.players;i++){
                    score = (Player.getmoney(i)+Price.price1*Player.getstocks(i,0)+Price.price2*Player.getstocks(i,1)+Price.price3*Player.getstocks(i,2));
                    out.println(Player.getname(i)+": "+score+"yen.");
                }

                temp1 = Integer.parseInt(in.readLine());
                out.println(Player.getstocks(temp1,0));
                out.println(Player.getstocks(temp1,1));
                out.println(Player.getstocks(temp1,2));

                //��{�I�ɂ͔����ꍇ�Ɠ��l�̏������s���B
                while(true){
                    line = in.readLine();
                    try{
                        command = Integer.parseInt(line);
                    }
                    catch(NumberFormatException nfe){
                        command = 0;
                    }
                    try{
                        if(command==1){
                            line = in.readLine();
                            if(line.equals("OK")){
                                //System.out.println("OK1");
                                temp1 = Integer.parseInt(in.readLine());
                                temp2 = Integer.parseInt(in.readLine());
                                temp3 = Integer.parseInt(in.readLine());
                                Player.putstocks(temp2,0,Player.getstocks(temp2,0)-temp1);
                                Player.putmoney(temp2,temp3);
                                Player.wait_num++;
                                if(Player.wait_num%Player.players==0) Price.changePrice();
                                wt.waitPlayer();
                                break;
                            }
                            else{/*System.out.println("NG1");*/}
                        }
                        else if(command==2){
                            line = in.readLine();
                            if(line.equals("OK")){
                                //System.out.println("OK2");
                                temp1 = Integer.parseInt(in.readLine());
                                temp2 = Integer.parseInt(in.readLine());
                                temp3 = Integer.parseInt(in.readLine());
                                Player.putstocks(temp2,1,Player.getstocks(temp2,1)-temp1);
                                Player.putmoney(temp2,temp3);
                                Player.wait_num++;
                                if(Player.wait_num%Player.players==0) Price.changePrice();
                                wt.waitPlayer();
                                break;
                            }
                            else{/*System.out.println("NG2");*/}
                        }
                        else if(command==3){
                            line = in.readLine();
                            if(line.equals("OK")){
                                //System.out.println("OK3");
                                temp1 = Integer.parseInt(in.readLine());
                                temp2 = Integer.parseInt(in.readLine());
                                temp3 = Integer.parseInt(in.readLine());
                                Player.putstocks(temp2,2,Player.getstocks(temp2,2)-temp1);
                                Player.putmoney(temp2,temp3);
                                Player.wait_num++;
                                if(Player.wait_num%Player.players==0) Price.changePrice(); //�܂�������ϓ�������B
                                wt.waitPlayer();
                                break;
                            }
                            else{/*System.out.println("NG3");*/}
                        }
                        else if(command==4) {
                                Player.wait_num++;
                                if(Player.wait_num%Player.players==0) Price.changePrice();
                                wt.waitPlayer();
                          break;
                        }
                        else {/*System.out.println("invalid.");*/}
                    }
                    catch(NumberFormatException nfe){

                    }
                }
                //�����Ń^�[�������J�E���g�����̃^�[���ցB
                turn++;
            }
            //�ŏI�^�[���I����̏���
            //�ŏI������\��
            if(Price.fluc1>=0)out.println("��1:"+Price.price1+"yen.(+"+Price.fluc1+")");
            else out.println("��1:"+Price.price1+"yen.("+Price.fluc1+")");
            if(Price.fluc2>=0)out.println("��2:"+Price.price2+"yen.(+"+Price.fluc2+")");
            else out.println("��2:"+Price.price2+"yen.("+Price.fluc2+")");
            if(Price.fluc3>=0)out.println("��3:"+Price.price3+"yen.(+"+Price.fluc3+")");
            else out.println("��3:"+Price.price3+"yen.("+Price.fluc3+")");
            //�ŏI������ϐ��Ƃ��đ��M
            out.println(Price.price1);
            out.println(Price.price2);
            out.println(Price.price3);
            out.flush();

            int fscore[] = new int[100];
            for(Global.i=0;Global.i<Player.players;Global.i++){
                fscore[Global.i] = (Player.getmoney(Global.i)
                    +Price.price1*Player.getstocks(Global.i,0)
                    +Price.price2*Player.getstocks(Global.i,1)
                    +Price.price3*Player.getstocks(Global.i,2));
                System.out.println("Player"+Global.i+":"+fscore[Global.i]+"yen.");
            }
            int topNum=0,topScore=fscore[0];
            for(Global.i=1;Global.i<Player.players;Global.i++){
                if(fscore[Global.i]>fscore[topNum]){
                    topScore = fscore[Global.i];
                    topNum = Global.i;
                }
            }
            for(Global.i=0;Global.i<Player.players;Global.i++){
                out.println(Player.name[Global.i]+": "+fscore[Global.i]+" yen.");
            }

            out.println(topNum);
            out.println("�D����"+Player.name[topNum]+"����ł��B������: "+topScore+" yen.");

            //���O�A�E�g�������Ƃ��N���C�A���g����󂯎��\������B
            line = in.readLine();
            System.out.println(line);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        finally{
            try{
                if(socket != null)  socket.close();
            }
            catch(IOException e){}
            try{
                if(serverSocket!=null)  serverSocket.close();
            }
            catch(IOException e){}
        }
    }
}