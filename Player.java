
public class Player{

  public static int players;
  public static int p_num;
  public static int wait_num=0;

  static int stocks[][] = new int[100][3]; //�e�v���C���[�̏��������Ǘ��p�z��stocks
  static String name[] = new String[100];  //�v���C���[���Ǘ��p�z��name
  static int money[] = new int[100]; //�e�v���C���[�̏�����

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
