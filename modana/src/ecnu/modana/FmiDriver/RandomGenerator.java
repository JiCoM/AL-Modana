package ecnu.modana.FmiDriver;

import java.util.Random;

public class RandomGenerator extends PlugInSlave{
    public boolean strategy_RoomA,strategy_RoomB;
    public boolean triggered;
    public Random random = null;
    Room room1 = null;
    @Override
    public void NewPath() {
    }
    public RandomGenerator(Long t){
        this.random = new Random(t);
        this.triggered = false;
        this.strategy_RoomA = false;
        this.strategy_RoomB = false;
    }
    public double predictStepSize(double curTime){
        return (1-curTime%1) <1e-5?  1e-5: (1-curTime%1);
    }
    @Override
    public double DoStep(double curTime, double stepSize) {
        double offset = 1e-2;
//        if ((curTime + stepSize) % 1 <= offset){
//                    triggered = true;
//                    this.strategy_Room1 = (random.nextInt(10) % 2 == 0);
//                    this.strategy_Room2 = !this.strategy_Room1;
//                    return stepSize;
//        }else
//            return stepSize;
        if((curTime)%1<=offset){
            if(!triggered ) {
                if ((curTime + stepSize) % 1 <= offset){
                    triggered = true;
                    this.strategy_RoomA = random.nextBoolean();
                    this.strategy_RoomB = !strategy_RoomA;
                    return stepSize;
                }else
                    return -1;
            }else{
                if ((curTime + stepSize) % 1 <= offset){
                    triggered = true;
                    return stepSize;
                }else {
                    triggered = false;
                    return stepSize;
                }
            }
        }else{
            if(triggered) {
                return -1;
            }else{
                if((curTime + stepSize) % 1 <= offset){
                    triggered = true;
                    this.strategy_RoomA = random.nextBoolean();
                    this.strategy_RoomB = !strategy_RoomA;
                    return stepSize;
                }else if(curTime%1+stepSize>=1)
                    return -1;
                else
                    return stepSize;
            }
        }
    }

    @Override
    public double GetValues(String varNames) {
        if(varNames.equals("strategy_Room1"))
            return (this.strategy_RoomA ? 1.0:0.0);
        return (strategy_RoomA? 1.0: 0.0);
    }

    @Override
    public boolean SetValues(String varNames, double values) {
        if(varNames.equals("strategy_Room1"))
            strategy_RoomA = true;
        return true;
    }

    @Override
    public void FreeSlave() {
        super.FreeSlave();
    }

    @Override
    public boolean RollBack(SlaveTrace slaveTrace, State state) {
        return super.RollBack(slaveTrace, state);
    }

    public void setValues(boolean t1,boolean t2,boolean triggered){
        this.strategy_RoomA = t1;
        this.strategy_RoomB = t2;
        this.triggered = triggered;
    }
}
