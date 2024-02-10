// Monster.java
// Used for PacMan
package src.Monster;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import src.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Monster extends Actor
{
  private Game game;
  private MonsterType monsterType;
  private ArrayList<Location> visitedList = new ArrayList<Location>();
  private int listLength = 10;
  private boolean stopMoving = false;
  private boolean furiousState = false;
  private int seed = 0;
  private Random randomiser = new Random(0);

  public Monster(Game game, MonsterType monsterType, String monsterImage)
  {
    // construct unrotatable Actor
    super(monsterImage);
    this.game = game;
    this.monsterType = monsterType;
  }

  public Monster(Game game, MonsterType monsterType, String monsterImage, int listLength)
  {
    // construct unrotatable Actor
    super(monsterImage);
    this.game = game;
    this.monsterType = monsterType;
    this.listLength = listLength;
  }

  public MonsterType getType() {
    return monsterType;
  }

  public Game getGame() {
    return game;
  }

  public Random getRandomiser() {
    return randomiser;
  }

  public boolean isFuriousState() {
    return furiousState;
  }

  public ArrayList<Location> getVisitedList() {
    return visitedList;
  }

  public void setSeed(int seed) {
    this.seed = seed;
    randomiser.setSeed(seed);
  }

  public void setStopMoving(boolean stopMoving) {
    this.stopMoving = stopMoving;
  }

  public void stopMoving(int seconds) {
    this.stopMoving = true;
    Timer timer = new Timer(); // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    final Monster monster = this;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.stopMoving = false;
      }
    }, seconds * SECOND_TO_MILLISECONDS);
  }

  public void enterFurious(int seconds) {
    this.furiousState = true;
    Timer timer = new Timer(); // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    final Monster monster = this;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.furiousState = false;
      }
    }, seconds * SECOND_TO_MILLISECONDS);
  }

  public void act()
  {
    if (stopMoving) {
      return;
    }
    walkApproach();
    if (getDirection() > 150 && getDirection() < 210)
      setHorzMirror(false);
    else
      setHorzMirror(true);
  }

  protected abstract void walkApproach();

  protected Location randomWalk(double oldDirection) {
    // revised RandomWalk()
    int sign = getRandomiser().nextDouble() < 0.5 ? 1 : -1;
    double currentDirection;
    Location next = null;
    int moveTry = 0;
    // loop to check each direction of randomWalk
    while(moveTry <= 3) {
      setDirection(oldDirection);
      switch (moveTry) {
        case 0:
          // Try to turn left/right
          turn(sign * 90);
          break;
        case 1:
          // Try to move forward (in the same direction)
          break;
        case 2:
          // Try to turn right/left
          turn(-sign * 90);
          break;
        case 3:
          // Turn backward
          turn(180);
          break;
      }
      currentDirection = getDirection();
      next = getLocation().getNeighbourLocation(currentDirection);
      if (canMove(next)) {
        if (isFuriousState() == true) {
          // 2 block move is it valid or not, if YES move to that location & exit, otherwise find new direction
          next = next.getNeighbourLocation(currentDirection);
          if (canMove(next)) {
            setLocation(next);
            break;
          }
        } else {
          setLocation(next);
          break;
        }
      }
      moveTry++;
    }
    return next;
  }

  protected void addVisitedList(Location location)
  {
    visitedList.add(location);
    if (visitedList.size() == listLength)
      visitedList.remove(0);
  }

  protected boolean isVisited(Location location)
  {
    for (Location loc : visitedList)
      if (loc.equals(location)) {
        return true;
      }
    return false;
  }

  protected boolean canMove(Location location)
  {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.gray) || location.getX() >= game.getNumHorzCells()
            || location.getX() < 0 || location.getY() >= game.getNumVertCells() || location.getY() < 0)
      return false;
    else
      return true;
  }
}
