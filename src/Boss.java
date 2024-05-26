public class Boss extends Enemy {
    private int phase;
    private boolean phaseOneBeat;
    private boolean phaseTwoBeat;
    private boolean phaseThreeBeat;
    private boolean win;

    public Boss(){
        super("src/assets/background.png",400,200);
        phase = 1;
        phaseOneBeat = false;
        phaseTwoBeat = false;
        phaseThreeBeat = false;
        win = false;
    }
    public void phaseOne(){
        if (!phaseOneBeat) {
            phaseOneBeat = true;
            phase = 2;
        }else{
            phaseTwo();
        }
    }
    public void phaseTwo(){
        if (!phaseTwoBeat) {
            phaseTwoBeat = true;
            phase = 3;
        }else{
            phaseThree();
        }
    }
    public void phaseThree(){
        if (!phaseThreeBeat){
            win = true;
        }
    }
}
