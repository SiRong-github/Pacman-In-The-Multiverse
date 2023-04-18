// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.utility.GameCallback;

import java.awt.*;
import java.util.ArrayList;
import java.util.Properties;

public class Game extends GameGrid
{
  // game window size
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;
  protected PacManGameGrid grid = new PacManGameGrid(nbHorzCells, nbVertCells);

  protected PacActor pacActor = new PacActor(this);

  private ArrayList<Monster> monsterList = new ArrayList<Monster>();

  private ArrayList<Item> itemList = new ArrayList<Item>();

  private GameCallback gameCallback;
  private Properties properties;

  // initiate random seed
  private int seed = 30006;

  private int propertyPillNumber = 0;
  private int propertyGoldNumber = 0;
  private String currentGameVersion;

  private static String[] gameVersion = {"simple", "multiverse"};

  private static String[] charKeyword = {"PacMan.location", "Troll.location", "TX5.location", "Orion.location",
  "Alien.location", "Wizard.location"};

  private static String[] itemKeyword = {"Pills.location", "Gold.location", "Ice.location"};
  public Game(GameCallback gameCallback, Properties properties)
  {
    //Setup game
    super(nbHorzCells, nbVertCells, 20, false);
    this.gameCallback = gameCallback;
    this.properties = properties;
    this.currentGameVersion = properties.getProperty("version");
    initializeMonster(currentGameVersion);
    // set period of simulation loop (millisec) == each loop is 1 sec
    setSimulationPeriod(100);
    setTitle("[PacMan in the Multiverse]");

    //Setup for auto test
    pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
    pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));
    setupItem();

    GGBackground bg = getBg();
    drawGrid(bg);

    //Setup Random seeds
    seed = Integer.parseInt(properties.getProperty("seed"));
    pacActor.setSeed(seed);
    pacActor.setSlowDown(3);
    addKeyRepeatListener(pacActor);
    setKeyRepeatPeriod(150);
    for (Monster monster: monsterList) {
      monster.setSeed(seed);
      monster.setSlowDown(3);
      if (monster.getType() == MonsterType.TX5) {
        monster.stopMoving(5);
      }
    }
    setupActorLocations(currentGameVersion);


    //Run the game
    doRun();
    // show the gameGrid window after initialization
    show();

    // Loop to look for collision in the application thread
    // This makes it improbable that we miss a hit
    boolean hasPacmanBeenHit = false;
    boolean hasPacmanEatAllPills;
    int maxPillsAndItems = countPillsAndItems();

    // loop to check whether has been hit by monster / eat all pills or not
    do {
      for (Monster monster: monsterList) {
        if (monster.getLocation().equals(pacActor.getLocation())) {
          hasPacmanBeenHit = true;
        }
      }
      hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;
      delay(10);
    } while(!hasPacmanBeenHit && !hasPacmanEatAllPills);
    delay(120);

    // if YES, stop the game
    Location loc = pacActor.getLocation();
    for (Monster monster: monsterList) {
      monster.setStopMoving(true);
    }
    pacActor.removeSelf();

    // render WIN/LOSE game & stop running the game
    String title = "";
    if (hasPacmanBeenHit) {
      bg.setPaintColor(Color.red);
      title = "GAME OVER";
      addActor(new Actor("sprites/explosion3.gif"), loc);
    } else if (hasPacmanEatAllPills) {
      bg.setPaintColor(Color.yellow);
      title = "YOU WIN";
    }
    setTitle(title);
    gameCallback.endOfGame(title);

    doPause();
  }

  public GameCallback getGameCallback() {
    return gameCallback;
  }

  public String getCurrentGameVersion() {
    return currentGameVersion;
  }

  public static String[] getGameVersion() {
    return gameVersion;
  }

  private void initializeMonster(String version) {
    monsterList.add(new Troll(this));
    monsterList.add(new TX5(this));
    if (version.equals(gameVersion[1])) {
//      monsterList.add(new Orion(this));
//      monsterList.add(new Alien(this));
//      monsterList.add(new Wizard(this));
    }
  }

  // read Character Location and stored in String[] --> add Actor into matched ArrayList<Actor> with its location & moving direction
  private void setupActorLocations(String version) {
    String[] charLocations;
    int initializeLength, charX, charY;

    if (version.equals(gameVersion[1])) {
      initializeLength = charKeyword.length;
    } else {
      initializeLength = 3;
    }

    for (int i = 0; i < initializeLength; i++) {
      charLocations = this.properties.getProperty(charKeyword[i]).split(",");
      charX = Integer.parseInt(charLocations[0]);
      charY = Integer.parseInt(charLocations[1]);
      switch (i) {
        case 0:
          addActor(pacActor, new Location(charX, charY));
          break;
        case 1:
          addActor(monsterList.get(0), new Location(charX, charY), Location.NORTH);
          break;
        case 2:
          addActor(monsterList.get(1), new Location(charX, charY), Location.NORTH);
          break;
//        case 3:
//          addActor(new Orion(this), new Location(charX, charY), Location.NORTH);
//          break;
//        case 4:
//          addActor(new Alien(this), new Location(charX, charY), Location.NORTH);
//          break;
//        case 5:
//          addActor(new Wizard(this), new Location(charX, charY), Location.NORTH);
//          break;
      }
    }
  }

  private int countPillsAndItems() {
    int pillsAndItemsCount = 0;
    for (Item item : itemList) {
      if (item.getItemType() != ItemType.ICE && item.getItemType() != null) {
        pillsAndItemsCount++;
      }
    }
    return pillsAndItemsCount;
  }

  public ArrayList<Item> getItemList() {
    return itemList;
  }

  private void setupItem() {
    String itemLocationString;
    String[] itemLocations;
    int itemX, itemY;

    for (int i = 0; i < itemKeyword.length; i++) {
      itemLocationString = this.properties.getProperty(itemKeyword[i]);
      if (itemLocationString != null) {
        itemLocations = itemLocationString.split(";");
        for (String singleItemLocation: itemLocations) {
          itemX = Integer.parseInt(singleItemLocation.split(",")[0]);
          itemY = Integer.parseInt(singleItemLocation.split(",")[1]);
          switch (i) {
            case 0:
              itemList.add(new Pill(new Location(itemX, itemY)));
              propertyPillNumber++;
              break;
            case 1:
              itemList.add(new Gold(new Location(itemX, itemY)));
              propertyGoldNumber++;
              break;
            case 2:
              itemList.add(new Ice(new Location(itemX, itemY)));
              break;
          }
        }
      }
    }

    // add remaining item
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && propertyPillNumber == 0) {
          itemList.add(new Pill(location));
        }
        if (a == 3 &&  propertyGoldNumber == 0) {
          itemList.add(new Gold(location));
        }
        if (a == 4) {
          itemList.add(new Ice(location));
        }
      }
    }

  }

  private void drawGrid(GGBackground bg)
  {
    // set background of the grid (for insert pills, golds, ices)
    bg.clear(Color.gray);
    bg.setPaintColor(Color.white);
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        bg.setPaintColor(Color.white);
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a > 0)
          bg.fillCell(location, Color.lightGray);
      }
    }

    for (Item item : itemList) {
      putItem(bg, item);
    }
  }

  private void putItem(GGBackground bg, Item item) {
    switch (item.getItemType()) {
      case PILL:
        bg.setPaintColor(Color.white);
        bg.fillCircle(toPoint(item.getLocation()), 5);
        break;
      case GOLD:
        bg.setPaintColor(Color.yellow);
        bg.fillCircle(toPoint(item.getLocation()), 5);
        addActor(item, item.getLocation());
        break;
      case ICE:
        bg.setPaintColor(Color.blue);
        bg.fillCircle(toPoint(item.getLocation()), 5);
        addActor(item, item.getLocation());
        break;
    }
  }

  public void removeItem(ItemType checkItemType, Location location){
    for (Item item : itemList){
      if (item.getItemType() == checkItemType && location.equals(item.getLocation())) {
        item.hide();
        if (item.getItemType() == ItemType.ICE && currentGameVersion.equals(gameVersion[1])) {
          for (Monster monster : monsterList) {
            monster.stopMoving(3);
          }
        } else if (item.getItemType() == ItemType.GOLD && currentGameVersion.equals(gameVersion[1])) {
          for (Monster monster : monsterList) {
            monster.enterFurious(3);
          }
        }
      }
    }
  }

  public int getNumHorzCells(){
    return this.nbHorzCells;
  }
  public int getNumVertCells(){
    return this.nbVertCells;
  }
}
