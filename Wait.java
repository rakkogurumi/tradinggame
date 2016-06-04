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

}
