import java.util.Random;

//test upload


public class Price{

	public static void changePrice(){
		//現在時刻をシードとして乱数を作成。
        long seed = System.currentTimeMillis();
        Random r = new Random(seed);
        int ran1 = r.nextInt(21)-10;
        int ran2 = r.nextInt(41)-20;
        int ran3 = r.nextInt(61)-30;
        double rate1 = (double)1.0+(double)ran1/100; //株1は±10%
        double rate2 = (double)1.0+(double)ran2/100; //株2は±20%
        double rate3 = (double)1.0+(double)ran3/100; //株3は±30%変動する。
        Global.fluc1=Global.price1;//前回からの変動数値を記録
        Global.fluc2=Global.price2;
        Global.fluc3=Global.price3;
        Global.price1*=rate1;
        Global.price2*=rate2; 
        Global.price3*=rate3;
		//株価最低額を指定(あまりにも株価が低くなることを防止)
        if(Global.price1<20) Global.price1=25;
        if(Global.price2<30) Global.price2=30;
        if(Global.price3<40) Global.price3=35;
        Global.fluc1=Global.price1-Global.fluc1;
        Global.fluc2=Global.price2-Global.fluc2;
        Global.fluc3=Global.price3-Global.fluc3;
    }
    
}
