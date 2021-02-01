package wargame.model;

import java.util.ArrayList;

;

public class Machine extends Player
{

    private final static String MACHINE = "Machine";

    public Machine()
    {

    }

    @Override
    public String getName()
    {
	return MACHINE;
    }public ArrayList<Card> getStartingHand() {
		return startingHand;
	}
   

}
