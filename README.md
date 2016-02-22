# Battlecode 2016
This is a selection of **what_thesis**'s Battlecode 2016 submission. I'm making it available here so I can share it with others and give some coding examples for those who might be interested in what I've been up to.

Team members were Aaron Kuan, Stephen Fleming, Tamas Szalay, and Ryan Rollings, all hailing from Harvard Physics/Applied Physics.

## What is Battlecode?
Battlecode is a Java based 'hard core' AI coding competition done every year at MIT. Teams of up to four write an AI that controls a squad of simulated robots that battle head-to-head against an opponent's AI squad on terrain that's never been seen. Much more about the competition can be learned from MIT's host website [here](https://www.battlecode.org/contestants/about/). The game penalizes each robot for inneficient code by counting up the bytecode cost of each function called. This means that well known pathfinding algorithms must be completely rewritten in an efficient way and forces teams to implement their own pared down versions or develop their own hueristics. Even more critical is the game's limit on global knowledge; each simulated robot only knows about it's world within a very small radius and collective decisions must be handled through a costly and 'low bandwidth' messaging system.

The game is very similar to other real time strategy games like the popular video game **StarCraft**. This year mobile factories called Archons could produce robots of several types, each with their own abilities. Achons could build soldiers, guards, vipers, turrets and scouts, each with their own attributes and abilities. Details of this year's game can be found [here] (https://www.battlecode.org/info).

The entire three hour video of the finals, including the matches from our team can be found [on youtube] (https://www.youtube.com/watch?v=4ruUyCbhnWg)

There's also article on the Harvard Crimson with a photo of our team.
http://www.thecrimson.com/article/2016/2/18/mit-harvard-scrutiny/

## What is the code in Battlecode?
The java code written for Battlecode is meant to be run by a client designed by the Battlecode development team. Our job is to construct code written in RobotPlayer.java which we upload to the competition server. This year we created a new file for pretty much every class we used and tied them together as a package. This made it much easier to work as a team and cleaned things up.

For most of the competition we thought of each robot as a state machine with states like DEFENSIVE, OFFENSIVE, and RUNAWAY. Even though the robots can't see very much or communicate with other robots very well, they can easily remember their history, so knowing that you were heavily attacked last round that you were in the OFFENSIVE state was easy for the robot to remember. By using the state machine idea, the robot could switch states to RUNAWAY at the start of the next round and proceed to run away and fight when the odds were better. This formalism cleaned up our coding and it let us add useful state machine logic. For example, a robot that's getting heavily damaged can move from the DEFENSIVE or OFFENSIVE state to RUNAWAY, but once in RUNAWAY could only go back to the DEFENSIVE state. 

## What's in this repository
The folder team023 was contains our final submission bot. The other folders are bots that we developed during the tournament and used as test competitors.

## Note's on Ryan's state machine
For anyone interested in getting a feel for my coding style, I've dug into our Git and pulled out the  file RobotSoldier_ryanonly.java was an earlier version of the class that controlled our soldier was written almost entirely by me.

In it you'll find the method turn() that calls updateState() where the logic is done to decide what state to be in for this round. Within updateState() doOffensive() and doDefensive() are called to do the appropriate action. Note, there's no doRunAway() state because soldiers never ran away - actually they did, but later on in the game! Within each method you'll find calls to things like Mico.getNarbyHostiles which is calling a method from the class Micro (in the file MicroBase.java) that's doing the appropriate calculations to see if any bad guys are nearby.