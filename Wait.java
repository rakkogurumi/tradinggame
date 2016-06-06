public class Wait{

    public synchronized void waitLogin(){
      try{
          while(Player.p_num<Player.players){
            wait(800);
          }
          notifyAll();
      }catch(InterruptedException e){
      }
    }

    public synchronized void waitPlayer(){
      try{
          while(Player.wait_num%Player.players!=0){
            wait(800);
          }
          notifyAll();
      }catch(InterruptedException e){
      }
    }

}