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
    //ポートは10007
    public static final int ECHO_PORT = 10007;

    public static void main(String args[]) {
        Socket socket = null;
        //引数なしでlocalhostに自動接続する。
        //サーバー名を指定する場合は、"java Client [サーバー名]"
        try {
            if(args.length==0) socket = new Socket("localhost", ECHO_PORT);
            else socket = new Socket(args[0], ECHO_PORT);
            System.out.println("接続しました"+ socket.getRemoteSocketAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
            Scanner sc = new Scanner(System.in);
            String input;
            String line;
            String name;
            int highscore=0;
            int turn=1; //ターン数

            //自分の名前を入力してサーバーに送信
            System.out.println("プレイヤー名を入力してください。");
            input = keyIn.readLine();
            name = input;
            //サーバーからの返信を受け取る。
            out.println(name);
            line = in.readLine();
            System.out.println(line);

            //プレイヤー番号とプレイヤー人数を受け取る。
            int p_num = Integer.parseInt(in.readLine());
            int players = Integer.parseInt(in.readLine());
            int command;
            //p1,p2,p3は株価。
            int p1;
            int p2;
            int p3;
            int temp;
            int mymoney=10000; //所持金
            int mys1=0,mys2=0,mys3=0; //所持株

            try{
                System.out.println("歴代ハイスコア");
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

            //とりあえず10ターン
            while (turn<=3){
                System.out.println("---------------");
                System.out.println();
                line = in.readLine();
                System.out.println(line);
                System.out.println();
                System.out.println("フェイズ1:株購入フェイズ");

                System.out.println("*..Enter Keyを押してください..*");
                while((input = keyIn.readLine())!=null){
                    if(input.equals("")) break;
                    System.out.println("*..Enter Keyを押してください..*");
                }

                for(int i=0;i<3;i++){
                    line = in.readLine();
                    System.out.println(line);
                }


                System.out.println("---------------");
                System.out.println("プレイヤー状況");
                for(int i=0;i<players;i++){
                    line = in.readLine();
                    System.out.println(line);
                }

                out.println(p_num);
                out.flush();
                System.out.printf("株1の所持数:%4d ",Integer.parseInt(in.readLine()));
                System.out.printf("株2の所持数:%4d ",Integer.parseInt(in.readLine()));
                System.out.println("株3の所持数:"+Integer.parseInt(in.readLine()));   

                System.out.println("---------------");
                p1 = Integer.parseInt(in.readLine());
                p2 = Integer.parseInt(in.readLine());
                p3 = Integer.parseInt(in.readLine());

                System.out.println("*..もう１度Enter Keyを押してください..*");
                while((input = keyIn.readLine())!=null){
                    if(input.equals("")) break;
                    System.out.println("*..もう１度Enter Keyを押してください..*");
                }

                //PHASE1 Buying stocks
                while(true){
					System.out.println("現在の所持金："+mymoney+" yen.");
                    System.out.println("コマンドを入力してください");
                    System.out.println("1:株1を買う 2:株2を買う 3:株3を買う 4:買わない");
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
                        System.out.println("購入する株1の枚数を入力してください。 購入限界数:"+(int)mymoney/p1);
                        System.out.println("キャンセルする場合は0を入力してください。");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mymoney/p1 && temp>0){
                            out.println("OK");
                            System.out.println("株1を"+temp+"枚購入しました。");
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
                            System.out.println("取り引きを中断しました。");
                        }
                    }  
                    else if(command==2){
                        System.out.println("購入する株2の枚数を入力してください。 購入限界数:"+(int)mymoney/p2);
                        System.out.println("キャンセルする場合は0を入力してください。");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mymoney/p2 && temp>0){
                            out.println("OK");
                            System.out.println("株2を"+temp+"枚購入しました。");
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
                            System.out.println("取り引きを中断しました。");
                        }
                    }
                    else if(command==3){
                        System.out.println("購入する株3の枚数を入力してください。 購入限界数:"+(int)mymoney/p3);
                        System.out.println("キャンセルする場合は0を入力してください。");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mymoney/p3 && temp>0){
                            out.println("OK");
                            System.out.println("株3を"+temp+"枚購入しました。");
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
                            System.out.println("取り引きを中断しました。");
                        }
                    }
                    else if(command==4){
                        System.out.println("株売却フェイズに移行します。");
                        break;
                    }
                    else    System.out.println(input+"は無効な入力です。");
                }

                //PHASE2 Selling stocks
                System.out.println("---------------¥n");
                System.out.println();
                System.out.println("フェイズ2：株売却フェイズ");


                System.out.println("*..Enter Keyを押してください..*");
                while((input = keyIn.readLine())!=null){
                    if(input.equals("")) break;
                    System.out.println("*..Enter Keyを押してください..*");
                }


                for(int i=0;i<3;i++){
                    line = in.readLine();
                    System.out.println(line);
                }

                System.out.println("---------------");
                System.out.println("プレイヤーの状況");
                for(int i=0;i<players;i++){
                    line = in.readLine();
                    System.out.println(line);
                }

                out.println(p_num);
                System.out.printf("株1の所持数:%4d ",Integer.parseInt(in.readLine()));
                System.out.printf("株2の所持数:%4d ",Integer.parseInt(in.readLine()));
                System.out.println("株3の所持数:"+Integer.parseInt(in.readLine()));   

                System.out.println("---------------");

                System.out.println("*..もう１度Enter Keyを押してください..*");
                while((input = keyIn.readLine())!=null){
                  if(input.equals("")) break;
                  System.out.println("*..もう１度Enter Keyを押してください..*");
                }

                while(true){
					System.out.println("現在の所持金："+mymoney+" yen.");
                    System.out.println("コマンドを入力してください");
                    System.out.println("1:株1を売る 2:株2を売る 3:株3を売る 4:売らない");
                    input = keyIn.readLine();
                    out.println(input);
                    try{
                        command = Integer.parseInt(input);
                    }
                    catch(NumberFormatException nfe){
                        command =0;
                    }
                    if(command==1){
                        System.out.println("売却する株1の枚数を入力してください。最大売却数:"+mys1);
                        System.out.println("キャンセルする場合は0を入力してください。");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mys1 && temp>0){
                            out.println("OK");
                            System.out.println("株1を"+temp+"枚売却しました。");
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
                            System.out.println("取り引きを中止しました。");
                        }
                    }  
                    else if(command==2){
                        System.out.println("売却する株2の枚数を入力してください。最大売却数:"+mys2);
                        System.out.println("キャンセルする場合は0を入力してください。");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mys2 && temp>0){
                            out.println("OK");
                            System.out.println("株2を"+temp+"枚売却しました.");
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
                            System.out.println("取り引きを中止しました。");
                        }
                    }
                    else if(command==3){
                        System.out.println("売却する株3の枚数を入力してください。最大売却数:"+mys3);
                        System.out.println("キャンセルする場合は0を入力してください。");
                        try{
                            temp = Integer.parseInt(keyIn.readLine());
                        }
                        catch(NumberFormatException nfe){
                            temp =0;
                        }
                        if(temp<=mys3 && temp>0){
                            out.println("OK");
                            System.out.println("株3を"+temp+"枚売却しました。");
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
                            System.out.println("取り引きを中止しました。");
                        }
                    }
                    else if(command==4){
                        System.out.println("次のターンに移行します。");
                        break;
                    }
                    else    System.out.println(input+"は無効な入力です。");
                }

                turn++;
            }
            //Game end.
            System.out.println("最終株価変動");
            System.out.println("---------------");
            for(int i=0;i<3;i++){
                line = in.readLine();
                System.out.println(line);
            }


            p1 = Integer.parseInt(in.readLine());
            p2 = Integer.parseInt(in.readLine());
            p3 = Integer.parseInt(in.readLine());

			int fortune[] = new int[100];

			for(int i=0;i<players;i++){
				fortune[i] = Integer.parseInt(in.readLine());
				System.out.println(fortune[i]);
			}

            System.out.println();
            System.out.println("--------------------");
            System.out.println("最終プレイヤー状況");
            for(int i=0;i<players;i++){
                input = in.readLine();
                System.out.println(input);
            }
            System.out.println("--------------------");
            System.out.println();

            int winner;
            winner = Integer.parseInt(in.readLine());
            if(p_num==winner){
                System.out.println("あなたの勝ちです！");
                input = in.readLine();
            }
            else{
                input = in.readLine();
                System.out.println("あなたの負けです...");
                System.out.println(input);
            }

            if(fortune[winner]>highscore){
                System.out.println("ハイスコアが更新されました。");
                try{
                    File file1 = new File("hs_name.txt");
                    FileWriter filewriter1 = new FileWriter(file1);
                    filewriter1.write(name);
                    filewriter1.close();
                    File file2 = new File("highscore.txt");
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file2)));
                    pw.write(String.valueOf(fortune[winner]));
                    pw.close();
                }
                catch(IOException e){
                    System.out.println(e);
                }
            }
            System.out.println("終了");
            out.println("プレイヤー"+(p_num+1)+":"+name+"はログアウトしました。");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                if(socket != null)  socket.close();
            }
            catch (IOException e) {}
            System.out.println("切断されました " + socket.getRemoteSocketAddress());
        }
    }
}

