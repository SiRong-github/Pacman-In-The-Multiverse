# University Subject
This is a SWEN30006 Software Modelling and Design Project of the University of Melbourne created by a three-person team.

### Demo

https://github.com/SiRong-github/Pacman-In-The-Multiverse/assets/62817554/4125598f-6629-4f3e-9183-f2343e790786

# File Purpose
The program refactors a provided imitation of the Pacman game called Pacman in the Multiverse to better adhere to the GRASP principles and patterns.

# File-Level Documentation
This version of Pacman is similar to the original Pacman game, especially in terms of the gameplay. However, there are a few modifications:

1. Instead of Ghosts, there will be 2 kinds of Monsters.

    a. Troll

    Randomly turns left or right. If it hits the maze wall, it will turn back to the original direction and walk forward or turn the other side. Otherwise, it walks backward.

	b. T-X5
	
    Chases PacMan by determining the compass direction from itself to Pacman. If it hits a maze wall or heads back to a place it has visited, it will randomly walk like Troll. At the start of the game, it will wait for 5 seconds before moving. 

3. Powerups

	a. Pill

    Increases score by 1 point.

	b. Gold

	Increases score by 5 points

	c. Ice cube 

	No effect.

4. In addition to refactoring the provided code, the other task is to extend the game with the following features

	a. 3 Additional Monster Types

	i. Orion 

    Protects gold pieces by walking towards gold locations at random. It will start by visiting all the locations of the uneaten gold pieces and then those of the eaten ones. If the chosen gold piece has been eaten while Orion is on the way, it will continue to walk towards its destination. Once all gold pieces have been visited, it will start over.

	ii. Alien

    Chases PacMan by calculating the distance between PacMan and each of the neighbour locations (8 cells) that it can move (not the maze wall and not outside the game board) and then choosing the shortest distance. If there are more than one locations with the same shortest distance, it will choose one at random.

	iii. Wizard

    Walks through a 1-cell thick wall. It randomly selects one of its neighbour locations and:

    *  If the chosen location is not a maze wall, it will move to that location.

    * If the chosen location is a maze wall, it will check if the adjacent location in the same direction is a wall. If the adjacent location is not a wall, it will walk through the wall towards the adjacent location. Otherwise, it will randomly select another neighbour location.

	b. Power Ups

	i. Gold Piece

    In addition to the effects written above, all monsters enter the furious state and move faster. They determine the moving direction once based on their walking approach and head towards that direction for 2 cells if they can. Otherwise, they repeat the process until they are able to do so. There should be at least one direction in which the monster can move for 2 cells. The effect wears out in 3 seconds.

	ii. Ice Cube

    All monsters are frozen for 3 seconds and will go back to moving normally regardless of whether they were in a furious state. While they are frozen, PacMan can obtain gold pieces without making them furious.

# Provided Template and Driver Program by the Subject

1. lib folder

Contains the JGameGrid library

2. properties 

Contains the test cases. 

    a. test1.properties

    Configuration for the original behaviour (just testing the refactoring and not the extensions).

    b. test2.properties and test3.properties
		
    Auto-test mode for the original behaviour.

    c. test4.properties and test5.properties

    Tests extended version. test4 is playable while test5 is in Auto-Test mode.

    d. test6.properties

    Tests all aliens in playable mode.

3. sprites

Contains the gif images of the game objects.

4. src
   
    a. Driver

    Runs the project based on the given command line arguments which are based on the selected test case in the properties folder.

    b. Game
    Main logic of the game.
     
    c. Monster

    Contains all monster logic (Troll and TX5).

    d. MonsterType
	
    Enum for the monster types.

    e. PacActor

    PacMan logic.

    f. PacManGameGrid

    Game board.

5. utility 

    a. GameCallback

    Used for testing.

    b. PropertiesLoader

    Loads the property file onto the game.


# Testing

Steps prior to compiling and running:
1. Download (or clone) the project.
2. Head to the project directory by writing.

	cd *projectFileLocation*
 
 For example
 	
  	cd Desktop/Pacman-In-The-Multiverse

3. Head to the pacman directory in the project directory.
	cd pacman

## Compiling

	javac -cp lib/JGameGrid.jar -d out src/*.java src/*/*.java

## Running

	java -cp out:lib/JGameGrid.jar:properties:sprites src.Driver properties/*test file*

For example:

	java -cp out:lib/JGameGrid.jar:properties:sprites src.Driver properties/test6.properties
