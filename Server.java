import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;


//グローバル変数の部分。(マルチスレッドではインスタンスの方がいいかも)
class Global{
    public static int price1=80;
    public static int price2=100;
    public static int price3=120;
    public static int fluc1;
    public static int fluc2;
    public static int fluc3;
    public static int players;
    public static int p_num;
    public static int i=0;
}

public class Server {
	//PORT番号は10007
    public static final int ECHO_PORT = 10007;

    

    public static void main(String[] args){
        ServerSocket serverSocket = null;
        
        try{
            serverSocket = new ServerSocket(ECHO_PORT);
            System.out.println("Serverが起動しました(port="+ serverSocket.getLocalPort() + ")");
            Scanner sc = new Scanner(System.in);
            System.out.println("プレイヤー人数を入力してください。");
			//プレイヤー人数を入力。(マルチスレッド用。現状1人プレイしか機能しない)
            int players = sc.nextInt();
            Global.p_num=0; //プレイヤー番号
            int stocks[][] = new int[players][3]; //各プレイヤーの所持株数管理用配列stocks
            String name[] = new String[players];  //プレイヤー名管理用配列name
            System.out.println("players="+players);
            System.out.println("クライアントを探しています。");

            for (int i=0;i<players;i++) {
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

            int players=2;
            Global.players=2;



            int stocks[][] = new int[players][3]; //各プレイヤーの所持株数管理用配列stocks
            String name[] = new String[players];  //プレイヤー名管理用配列name


            players=2;
			
			//stocksの初期化
            for(int i=0;i<Global.players;i++) for(int j=0;j<3;j++) stocks[i][j]=0;

			//プレイヤーが最初に指定した人数分揃うまでloginを待つ部分(場合によっては不必要)
            int i=0;
            //while(true){
               // if(Global.i>=Global.players) break;
                while((line = in.readLine())!=null){
                    System.out.println("プレイヤー"+(Global.i+1)+":"+line+"がログインしました。");
                    name[Global.i] =line;
                    Global.i++;
                    break;
                }
            //}

            int command;
            int turn=1; //ターン数
            int money[] = new int[players]; //各プレイヤーの所持金
            for(i=0;i<players;i++) money[i]=10000; //所持金の初期化。
            out.println("あなたはプレイヤー"+(Global.p_num+1)+"です。"); //プレイヤー番号を告知
            out.println(Global.p_num);						 //ついでにプレイヤー番号をクライアントに送る。
            Global.p_num++;
            out.println(players);
            out.flush();
            

			//とりあえず10ターン行うことにする。(初期設定で決めてもいいかもしれない)
            while (turn<=10){
                Price.changePrice(); //まず株価を変動させる。
				//ターン開始宣言と、現在の株価、持ち株数、持ち金をクライアントに送り、クライアント側で表示させる。
                System.out.println("Turn"+turn+" start.");
                out.println("Turn"+turn+" start.");
                if(Global.fluc1>=0)out.println("株1:"+Global.price1+"yen.(+"+Global.fluc1+")");
                else out.println("株1:"+Global.price1+"yen.("+Global.fluc1+")");
                if(Global.fluc2>=0)out.println("株2:"+Global.price2+"yen.(+"+Global.fluc2+")");
                else out.println("株2:"+Global.price2+"yen.("+Global.fluc2+")");
                if(Global.fluc3>=0)out.println("株3:"+Global.price3+"yen.(+"+Global.fluc3+")");
                else out.println("株3:"+Global.price3+"yen.("+Global.fluc3+")");

                for(i=0;i<players;i++)  out.println(name[i]+": "+money[i]+"yen.");

				//プレイヤー番号を受け取り、そのプレイヤーの持ち株を返す。
                temp1 = Integer.parseInt(in.readLine());
                out.println(stocks[temp1][0]);
                out.println(stocks[temp1][1]);
                out.println(stocks[temp1][2]);
                out.flush();
				//クライアントに現在の株価を教える部分。
                out.println(Global.price1);
                out.println(Global.price2);
                out.println(Global.price3);
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
                            stocks[temp2][0] +=temp1;
                            money[temp2] = temp3;
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
                            stocks[temp2][1] +=temp1;
                            money[temp2] = temp3;
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
                            stocks[temp2][2] +=temp1;
                            money[temp2] = temp3;
                            break;
                        }
                        else{/*System.out.println("NG3");*/}
                    }
					//[4]何もしない　を選んだ場合は即座に次に進む。
                    else if(command==4) break;
                    else {/*System.out.println("invalid.");*/}
                }

                //後半。株を売るフェイズ。
				//再度状況をクライアントに送信
                if(Global.fluc1>=0)out.println("株1:"+Global.price1+"yen.(+"+Global.fluc1+")");
                else out.println("株1:"+Global.price1+"yen.("+Global.fluc1+")");
                if(Global.fluc2>=0)out.println("株2:"+Global.price2+"yen.(+"+Global.fluc2+")");
                else out.println("株2:"+Global.price2+"yen.("+Global.fluc2+")");
                if(Global.fluc3>=0)out.println("株3:"+Global.price3+"yen.(+"+Global.fluc3+")");
                else out.println("株3:"+Global.price3+"yen.("+Global.fluc3+")");

                for(i=0;i<players;i++)  out.println(name[i]+": "+money[i]+"yen.");

                temp1 = Integer.parseInt(in.readLine());
                out.println(stocks[temp1][0]);
                out.println(stocks[temp1][1]);
                out.println(stocks[temp1][2]);
				
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
                                stocks[temp2][0] -=temp1;
                                money[temp2] = temp3;
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
                                stocks[temp2][1] -=temp1;
                                money[temp2] = temp3;
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
                                stocks[temp2][2] -=temp1;
                                money[temp2] = temp3;
                                break;
                            }
                            else{/*System.out.println("NG3");*/}
                        }
                        else if(command==4) break;
                        else {/*System.out.println("invalid.");*/}
                    }
                    catch(NumberFormatException nfe){
                        
                    }
                }
				//ここでターン数をカウントし次のターンへ。
                turn++;
            }
			//最終ターン終了後の処理
			//最終株価変動
            Price.changePrice();
			//最終株価を表示
            if(Global.fluc1>=0)out.println("株1:"+Global.price1+"yen.(+"+Global.fluc1+")");
            else out.println("株1:"+Global.price1+"yen.("+Global.fluc1+")");
            if(Global.fluc2>=0)out.println("株2:"+Global.price2+"yen.(+"+Global.fluc2+")");
            else out.println("株2:"+Global.price2+"yen.("+Global.fluc2+")");
            if(Global.fluc3>=0)out.println("株3:"+Global.price3+"yen.(+"+Global.fluc3+")");
            else out.println("株3:"+Global.price3+"yen.("+Global.fluc3+")");
            //最終株価を変数として送信
			out.println(Global.price1);
            out.println(Global.price2);
            out.println(Global.price3);
            out.flush();
			//ログアウトしたことをクライアントから受け取り表示する。
            line = in.readLine();
            System.out.println(line);
        }
        catch(IOException e){
            e.printStackTrace();
        }
		//サーバー終了時の処理(マルチスレッドの場合は終わってはいけないので要修正)
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
