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
		//���ݎ������V�[�h�Ƃ��ė������쐬�B
        long seed = System.currentTimeMillis();
        Random r = new Random(seed);
        int ran1 = r.nextInt(21)-10;
        int ran2 = r.nextInt(41)-20;
        int ran3 = r.nextInt(61)-30;
        double rate1 = (double)1.0+(double)ran1/100; //��1�́}10%
        double rate2 = (double)1.0+(double)ran2/100; //��2�́}20%
        double rate3 = (double)1.0+(double)ran3/100; //��3�́}30%�ϓ�����B
        fluc1=price1;//�O�񂩂�̕ϓ����l���L�^
        fluc2=price2;
        fluc3=price3;
        price1*=rate1;
        price2*=rate2; 
        price3*=rate3;
		//�����Œ�z���w��(���܂�ɂ��������Ⴍ�Ȃ邱�Ƃ�h�~)
        if(price1<20) price1=25;
        if(price2<30) price2=30;
        if(price3<40) price3=35;
        fluc1=price1-fluc1;
        fluc2=price2-fluc2;
        fluc3=price3-fluc3;
    }
    
}

