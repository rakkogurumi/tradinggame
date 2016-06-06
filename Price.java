import java.util.Random;

//test upload




public class Price{

    public static int price1=80;
    public static int price2=100;
    public static int price3=120;
    public static int fluc1;
    public static int fluc2;
    public static int fluc3;

	public static void changePrice(){
		//Œ»İ‚ğƒV[ƒh‚Æ‚µ‚Ä—”‚ğì¬B
        long seed = System.currentTimeMillis();
        Random r = new Random(seed);
        int ran1 = r.nextInt(21)-10;
        int ran2 = r.nextInt(41)-20;
        int ran3 = r.nextInt(61)-30;
        double rate1 = (double)1.0+(double)ran1/100; //Š”1‚Í}10%
        double rate2 = (double)1.0+(double)ran2/100; //Š”2‚Í}20%
        double rate3 = (double)1.0+(double)ran3/100; //Š”3‚Í}30%•Ï“®‚·‚éB
        fluc1=price1;//‘O‰ñ‚©‚ç‚Ì•Ï“®”’l‚ğ‹L˜^
        fluc2=price2;
        fluc3=price3;
        price1*=rate1;
        price2*=rate2; 
        price3*=rate3;
		//Š”‰¿Å’áŠz‚ğw’è(‚ ‚Ü‚è‚É‚àŠ”‰¿‚ª’á‚­‚È‚é‚±‚Æ‚ğ–h~)
        if(price1<20) price1=25;
        if(price2<30) price2=30;
        if(price3<40) price3=35;
        fluc1=price1-fluc1;
        fluc2=price2-fluc2;
        fluc3=price3-fluc3;
    }
    
}

