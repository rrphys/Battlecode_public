package space_bottity;

import battlecode.common.*;
import space_bottity.RoboArchon.ArchonState;

public class RoboSoldier extends RobotPlayer
{
	enum SoldierState
	{
		OFFENSIVE,
		DEFENSIVE
	}
	
	
	public static double lastRoundHealth = 60;//for soldier
	public static MapLocation closestAllyUnderAttackLocation = null;
	public static int roundsSinceAllyOrIWasAttacked = 100000;
	public static int roundsToWaitBeforeDefensive = 10;
	
	static SoldierState myState = SoldierState.OFFENSIVE;

	
	public static void init() throws GameActionException
	{
		BallMove.startBalling(RobotPlayer.myBuilderID);
	}
	
	public static void turn() throws GameActionException
	{
		updateState();
		
		switch (myState)
		{
		case OFFENSIVE:
			doOffense();
			break;
		case DEFENSIVE:
			doDefense();
			break;
		}
	}
	
	private static void updateState() throws GameActionException // this will probably need tweaking
	{
		RobotInfo[] ri = Micro.getNearbyHostiles();
		switch (myState)
		{
		case DEFENSIVE:
			//if I hear a buddies are under attack, or I see an enemy, or I was hit last round, switch to offensive
			Message.calcClosestAllyUnderAttack();
			MapLocation closestAllyUnderAttackLocation =  Message.closestAllyUnderAttackLocation;
			ri = Micro.getNearbyHostiles();
			if (closestAllyUnderAttackLocation != null || ri.length != 0 || wasDamagedLastRound())
			{
				myState = SoldierState.OFFENSIVE;
				roundsSinceAllyOrIWasAttacked = 0;
				Debug.setStringRR("State: Offensive");
			}
			else
			{
				roundsSinceAllyOrIWasAttacked++;
			}
			break;
		case OFFENSIVE:
			//if I've waited a few rounds since i heard a cry for help and don't see enemies, switch to defensive
			ri = Micro.getNearbyHostiles();
			if ((roundsSinceAllyOrIWasAttacked > roundsToWaitBeforeDefensive) && ri.length == 0);//start with 10rd wait
			{
				myState = SoldierState.DEFENSIVE;
				Debug.setStringRR("State: Defensive");
			}
			break;			
		}
	}
	
	public static void doOffense() throws GameActionException
	{
		double strengthRatio = nearbyAlliesFightingStrength()/nearbyHostilesFightingStrength();
		
		if (strengthRatio > 1)
		{
			Behavior.tryAttackSomeone();
		}// be skittish if we don't think we can win
		else if (strengthRatio < 1)
		{
			if (Micro.getRoundsUntilShootAndMove() < Micro.getRoundsUntilDanger()-1)
				Behavior.tryAttackSomeone();
			else
				Behavior.tryRetreatOrShootIfStuck();
		}
		

		if (rc.senseParts(here)==0)
			MapInfo.removeWaypoint(here);
		
		//Send ping to local allies to let them know I'm under attack
		if (wasDamagedLastRound())
			Message.sendSignal(RobotType.SOLDIER.sensorRadiusSquared*2);
				
		//read message and find closest ping, step towards it if you don't see enemies
		//there is a bug in bug nav I think?
		Message.calcClosestAllyUnderAttack();
		MapLocation closestAllyUnderAttackLocation =  Message.closestAllyUnderAttackLocation;
		if (closestAllyUnderAttackLocation != null)
		{
			DirectionSet canMove = Micro.getCanMoveDirs();
			if(Nav.tryGoTo(closestAllyUnderAttackLocation, canMove))
			{
				//Debug.setStringRR("I'm trying to go to " + closestAllyUnderAttackLocation + " and Nav succeeded");	
				
			}else
			{
				//Debug.setStringRR("I'm trying to go to " + closestAllyUnderAttackLocation + " and Nav failed");
			}
		}
	}
	
	public static void doDefense() throws GameActionException
	{
		if (rc.senseParts(here)==0)
			MapInfo.removeWaypoint(here);
		
		BallMove.tryUpdateTarget();
		BallMove.ballMove(2, 10);
		
		Direction dir = Direction.NONE;
		Behavior.tryClearRubble(dir);
	}
	
	public static boolean wasDamagedLastRound()
	{
		if (rc.getHealth() < lastRoundHealth)
		{
			lastRoundHealth = rc.getHealth();
			return true;
		}
		return false;
	}
	
	public static double nearbyAlliesFightingStrength()
	{
		//Estimate of whether allies can win a fight
		//Fighting strength is hp*attack summer over all units
		RobotInfo[] allies = Micro.getNearbyAllies();
		if (allies.length == 0)
			return 0;
		
		double allyStrength = 0;
		for (RobotInfo ri : allies)
		{
			allyStrength = allyStrength + ri.attackPower*ri.health;
		}
		return allyStrength;
	}
	
	public static double nearbyHostilesFightingStrength()
	{
		//Estimate of whether hostiles can win a fight
		//Fighting strength is hp*attack summer over all units
		RobotInfo[] hostiles = Micro.getNearbyHostiles();
		if (hostiles.length == 0)
			return 0;
		
		double hostileStrength = 0;
		for (RobotInfo ri : hostiles)
		{
			hostileStrength = hostileStrength + ri.attackPower*ri.health;
		}
		return hostileStrength;
	}
	
}