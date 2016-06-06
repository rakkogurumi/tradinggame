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

//グローバル変数の部分。(マルチスレッドではインスタンスの方がいいかも)
class Global{
    public static int i=0;
}

public class Server {
    //PORT番号は10007
    public static final int ECHO_PORT = 10007;


    public static void main(String[] args){
        ServerSocket serverSocket = null;
        BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));

        

        try{
            serverSocket = new ServerSocket(ECHO_PORT);
            System.out.println("Serverが起動しました(port="+ serverSocket.getLocalPort() + ")");
            Scanner sc = new Scanner(System.in);
            
            int chk=0;
            while(chk==0){
                try{
                    System.out.println("プレイヤー人数を入力してください。");
                    Player.players = Integer.parseInt(keyIn.readLine());
                    chk++;
                }
                catch(NumberFormatException nfe){
                    System.out.println("数字を入力してください。");
                }
            }
            Player.p_num=0; //プレイヤー番号
            System.out.println("クライアントを探しています。");

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
        System.out.println("接続されました "
                + socket.getRemoteSocketAddress());
    }

     public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String line;
            int temp1,temp2,temp3;

            //stocksの初期化
            for(int i=0;i<Player.players;i++) for(int j=0;j<3;j++) Player.putstocks(i,j,0);

            //プレイヤーが最初に指定した人数分揃うまでloginを待つ部分(場合によっては不必要)
            int i=0;
            //while(true){
               // if(Global.i>=Player.players) break;
                while((line = in.readLine())!=null){
                    System.out.println("プレイヤー"+(Global.i+1)+":"+line+"がログインしました。");
                    Player.putname(Global.i,line);
                    Global.i++;
                    break;
                }
            //}

            int command;
            int turn=1; //ターン数
            for(i=0;i<Player.players;i++) Player.putmoney(i,10000); //所持金の初期化。
            out.println("あなたはプレイヤー"+(Player.p_num+1)+"です。"); //プレイヤー番号を告知
            out.println(Player.p_num);                       //ついでにプレイヤー番号をクライアントに送る。
            Player.p_num++;
            out.println(Player.players);
            out.flush();

            wt.waitLogin();




            //とりあえず10ターン行うことにする。(初期設定で決めてもいいかもしれない)
            while (turn<=10){
                //Price.changePrice(); //まず株価を変動させる。
                //ターン開始宣言と、現在の株価、持ち株数、持ち金をクライアントに送り、クライアント側で表示させる。
                System.out.println(Thread.currentThread()+"Turn"+turn+" start.");
                out.println("Turn"+turn+" start.");
                if(Price.fluc1>=0)out.println("株1:"+Price.price1+"yen.(+"+Price.fluc1+")");
                else out.println("株1:"+Price.price1+"yen.("+Price.fluc1+")");
                if(Price.fluc2>=0)out.println("株2:"+Price.price2+"yen.(+"+Price.fluc2+")");
                else out.println("株2:"+Price.price2+"yen.("+Price.fluc2+")");
                if(Price.fluc3>=0)out.println("株3:"+Price.price3+"yen.(+"+Price.fluc3+")");
                else out.println("株3:"+Price.price3+"yen.("+Price.fluc3+")");

                int score;
                for(i=0;i<Player.players;i++){
                    score = (Player.getmoney(i)+Price.price1*Player.getstocks(i,0)+Price.price2*Player.getstocks(i,1)+Price.price3*Player.getstocks(i,2));
                    out.println(Player.getname(i)+": "+score+"yen.");
                }

                //プレイヤー番号を受け取り、そのプレイヤーの持ち株を返す。
                temp1 = Integer.parseInt(in.readLine());
                out.println(Player.getstocks(temp1,0));
                out.println(Player.getstocks(temp1,1));
                out.println(Player.getstocks(temp1,2));
                out.flush();
                //クライアントに現在の株価を教える部分。
                out.println(Price.price1);
                out.println(Price.price2);
                out.println(Price.price3);
                out.flush();

                //前半の株を買うフェイズ
                while(true){
                    //クライアントの選択を受け取る。
                    line = in.readLine();
                    //数字以外を入力しても強制終了しないようにtry-catchする。
                    try{
                        command = Integer.parseInt(line);
                    }
                    catch(NumberFormatException nfe){
                        command = 0;
                    }
                    //[1]株1を買う場合
                    if(command==1){
                        //クライアントが適切な数値を入力した場合のみ、以降の処理に移る。
                        //回りくどいが、これをしないと受信数≠送信数となり、結果フリーズする。
                        line = in.readLine();
                        if(line.equals("OK")){
                            //System.out.println("OK1");
                            //3つの変数(プレイヤー番号,購入数,購入後の金額)をクライアントから受け取る。
                            temp1 = Integer.parseInt(in.readLine());
                            temp2 = Integer.parseInt(in.readLine());
                            temp3 = Integer.parseInt(in.readLine());
                            //3つの変数をもとに、データ更新
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
                    //[4]何もしない　を選んだ場合は即座に次に進む。
                    else if(command==4) {
                                Player.wait_num++;
                                wt.waitPlayer();
                      break;
                    }
                    else {/*System.out.println("invalid.");*/}
                }

                //後半。株を売るフェイズ。
                //再度状況をクライアントに送信
                if(Price.fluc1>=0)out.println("株1:"+Price.price1+"yen.(+"+Price.fluc1+")");
                else out.println("株1:"+Price.price1+"yen.("+Price.fluc1+")");
                if(Price.fluc2>=0)out.println("株2:"+Price.price2+"yen.(+"+Price.fluc2+")");
                else out.println("株2:"+Price.price2+"yen.("+Price.fluc2+")");
                if(Price.fluc3>=0)out.println("株3:"+Price.price3+"yen.(+"+Price.fluc3+")");
                else out.println("株3:"+Price.price3+"yen.("+Price.fluc3+")");

                for(i=0;i<Player.players;i++){
                    score = (Player.getmoney(i)+Price.price1*Player.getstocks(i,0)+Price.price2*Player.getstocks(i,1)+Price.price3*Player.getstocks(i,2));
                    out.println(Player.getname(i)+": "+score+"yen.");
                }

                temp1 = Integer.parseInt(in.readLine());
                out.println(Player.getstocks(temp1,0));
                out.println(Player.getstocks(temp1,1));
                out.println(Player.getstocks(temp1,2));

                //基本的には買う場合と同様の処理を行う。
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
                                if(Player.wait_num%Player.players==0) Price.changePrice(); //まず株価を変動させる。
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
                //ここでターン数をカウントし次のターンへ。
                turn++;
            }
            //最終ターン終了後の処理
            //最終株価を表示
            if(Price.fluc1>=0)out.println("株1:"+Price.price1+"yen.(+"+Price.fluc1+")");
            else out.println("株1:"+Price.price1+"yen.("+Price.fluc1+")");
            if(Price.fluc2>=0)out.println("株2:"+Price.price2+"yen.(+"+Price.fluc2+")");
            else out.println("株2:"+Price.price2+"yen.("+Price.fluc2+")");
            if(Price.fluc3>=0)out.println("株3:"+Price.price3+"yen.(+"+Price.fluc3+")");
            else out.println("株3:"+Price.price3+"yen.("+Price.fluc3+")");
            //最終株価を変数として送信
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
            out.println("優勝は"+Player.name[topNum]+"さんです。所持金: "+topScore+" yen.");

            //ログアウトしたことをクライアントから受け取り表示する。
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