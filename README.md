# Battlecode 2016
This is Ryan Rollings' summary of team **what_thesis**'s Battlecode 2016 submission. I'm making it available here so I can share it with others and give some coding examples for those who might be interested in what I've been up to. There's a lot in the repository, so I've picked out the best summary of my coding style in  RobSoldier_ryanonly.java, details are at the bottom of the readme.

Team members were Aaron Kuan, Stephen Fleming, Tamas Szalay, and me, all hailing from Harvard Physics/Applied Physics. We qualified for the invitation only top 16 final tournament, making 7th place overall.

## What is Battlecode?
Battlecode is a Java based 'hard core' AI coding competition done every year at MIT since 2000. Teams of up to four write an AI that controls a squad of simulated robots that battle head-to-head against an opponent's AI squad on terrain that's never been seen. Much more about the competition can be learned from MIT's host website [here](https://www.battlecode.org/contestants/about/). The game penalizes each robot for inefficient code that's a function of the number of bytecodes run by the algorithm. This means that means decision making and well known pathfinding algorithms must be simplified and rewritten for bytecode efficiency. Even more critical is the game's limit on global knowledge as each simulated robot only knows about it's world within a very small radius and collective decisions must be handled through a costly and 'low bandwidth' messaging system.

The game is very similar to other real time strategy games like the popular video game StarCraft. This year mobile factories called Archons could produce robots of several types, each with their own abilities. Achons could build soldiers, guards, vipers, turrets and scouts, each with their own attributes and abilities. This year, extremely powerful zombie teams that ran a really simple AI were added, making for some pretty terrifying end-of-the-world situations. Details of this year's game can be found on youtube [here] (https://www.youtube.com/watch?v=4ruUyCbhnWg#1m50).

There's also article on the Harvard Crimson with a photo of our team. We're on the bottom left (I'm third from the left) in the finals going up against the team that eventually won the tournament.
![what_thesis Harvard Crimson](http://thumbnails.thecrimson.com.s3.amazonaws.com/photos/2016/02/16/191740_1312808.jpg.800x533_q95_crop-smart_upscale.jpg)

## What is the code in Battlecode?
The java code written for Battlecode is run by a client designed by the Battlecode development team. Our job is to construct code written in RobotPlayer.java which we upload to the competition server. This year we created a new file for pretty much every class we used and tied them together under RobotPlayer as a package. This made it much easier to work as a team with Git and cleaned things up.

For most of the competition we thought of each robot as a state machine with states like DEFENSIVE, OFFENSIVE, and RUNAWAY. Even though the robots can't see very much or communicate with other robots very well, they can easily remember their history, so knowing that you were heavily attacked last round that you were in the OFFENSIVE state was easy for the robot to remember. By using the state machine idea, the robot could switch states to RUNAWAY at the start of the next round and proceed to run away and fight when the odds were better. This formalism cleaned up our coding and it let us add useful state machine logic. For example, a robot that's getting heavily damaged can move from the DEFENSIVE or OFFENSIVE state to RUNAWAY, but once in RUNAWAY could only go back to the DEFENSIVE state. 

## What's in this repository
The folder team023 was contains our final submission bot. The other folders are bots that we developed during the tournament and used as test competitors.

## Note's on Ryan's state machine
For anyone interested in getting a feel for my coding style, I've dug into our Git and pulled out the  file RobotSoldier_ryanonly.java was an earlier version of the class that controlled our soldiers and was written entirely by me.

In it you'll find the method turn() that calls updateState() where the logic is done to decide what state to be in for this round. Within updateState() doOffensive() and doDefensive() are called to do the appropriate action. Later we added states like RUNAWAY and RUSH. Within each method you'll find calls to things like Mico.getNearbyHostiles which is calling a method from the class Micro (in the file MicroBase.java) that's doing the appropriate calculations to see if any bad guys are nearby.