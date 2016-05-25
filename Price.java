import java.util.Random;


public class Price{

    public static int price1=80;
    public static int price2=100;
    public static int price3=120;
    public static int fluc1;
    public static int fluc2;
    public static int fluc3;

  public static void changePrice(){ //現在時刻をシードとして乱数を作成。
        long seed = System.currentTimeMillis();
        Random r = new Random(seed);
        int ran1 = r.nextInt(21)-10;
        int ran2 = r.nextInt(41)-20;
        int ran3 = r.nextInt(61)-30;
        double rate1 = 1.0+(double)ran1/100; //株1は±10%
        double rate2 = 1.0+(double)ran2/100; //株2は±20%
        double rate3 = 1.0+(double)ran3/100; //株3は±30%変動する。
        fluc1=price1;//前回からの変動数値を記録
        fluc2=price2;
        fluc3=price3;
        price1*=rate1;
        price2*=rate2;
        price3*=rate3;
        //株価最低額を指定(あまりにも株価が低くなることを防止)
        if(price1<20) price1=25;
        if(price2<30) price2=30;
        if(price3<40) price3=35;
        fluc1=price1-fluc1;
        fluc2=price2-fluc2;
        fluc3=price3-fluc3;
    }

}
