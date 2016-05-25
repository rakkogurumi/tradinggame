public class Player{

  public static int players;

  static int stocks[][] = new int[100][3]; //各プレイヤーの所持株数管理用配列stocks
  static String name[] = new String[100];  //プレイヤー名管理用配列name
  static int money[] = new int[100]; //各プレイヤーの所持金

  public static int getstocks(int p,int s){
    return stocks[p][s];
  }

  public static void putstocks(int p,int s,int x){
    stocks[p][s]=x;
  }

  public static String getname(int p){
    return name[p];
  }

  public static void putname(int p,String n){
    name[p]=n;
  }

  public static int getmoney(int p){
    return money[p];
  }

  public static void putmoney(int p,int m){
    money[p]=m;
  }

}
