// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.Items.*;
import src.Monster.*;
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

  public PacActor pacActor = new PacActor(this);

  private ArrayList<Monster> monsterList = new ArrayList<Monster>();
  private ArrayList<Pill> pillList = new ArrayList<Pill>();
  private ArrayList<Item> itemList = new ArrayList<Item>();
  private ArrayList<Gold> goldList = new ArrayList<Gold>();

  private GameCallback gameCallback;
  private Properties properties;
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
    setupActorLocations(bg, currentGameVersion);
    for (Monster monster: monsterList) {
      monster.setSeed(seed);
      monster.setSlowDown(3);
      if (monster.getType() == MonsterType.TX5) {
        monster.stopMoving(5);
      }
    }


    //Run the game
    doRun();
    show();
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
    gameFinished(bg, hasPacmanBeenHit, hasPacmanEatAllPills);
    doPause();
  }

  public int getNumHorzCells(){
    return this.nbHorzCells;
  }
  public int getNumVertCells(){
    return this.nbVertCells;
  }

  public GameCallback getGameCallback() {
    return gameCallback;
  }

  public ArrayList<Item> getItemList() {
    return itemList;
  }

  public ArrayList<Gold> getGoldList() {
    return goldList;
  }

  // read Character Location and stored in String[] --> add Actor into matched ArrayList<Actor> with its location & moving direction
  private void setupActorLocations(GGBackground bg, String version) {
    String[] charLocations;
    int initializeLength, charX, charY;
    MonsterFactory monsterFactory = new MonsterFactory(this);
    Location charLocation;

    if (version.equals(gameVersion[1])) {
      initializeLength = charKeyword.length;
    } else {
      initializeLength = 3;
    }

    for (int i = 0; i < initializeLength; i++) {
      charLocations = this.properties.getProperty(charKeyword[i]).split(",");
      charX = Integer.parseInt(charLocations[0]);
      charY = Integer.parseInt(charLocations[1]);
      charLocation = new Location(charX, charY);
      if (i == 0) {
        addActor(pacActor, charLocation);
      } else {
        Monster monster = monsterFactory.createMonster(i-1);
        Color c = bg.getColor(charLocation);
        if (!c.equals(Color.gray) && charX >= 0 && charX < nbHorzCells && charY >= 0 && charY < nbVertCells) {
          monsterList.add(monster);
          addActor(monster, charLocation, Location.NORTH);
        }
      }
    }
  }

  private int countPillsAndItems() {
    int pillsAndItemsCount = 0;
    for (Item item : itemList) {
      if (item.getItemType() != ItemType.ICE) {
        pillsAndItemsCount++;
      }
    }
    pillsAndItemsCount += pillList.size();
    return pillsAndItemsCount;
  }

  private void setupItem() {
    String itemLocationString;
    String[] itemLocations;
    ItemFactory itemFactory = new ItemFactory();
    int itemX, itemY;

    for (int i = 0; i < itemKeyword.length; i++) {
      itemLocationString = this.properties.getProperty(itemKeyword[i]);
      if (itemLocationString != null) {
        itemLocations = itemLocationString.split(";");
        for (String singleItemLocation: itemLocations) {
          itemX = Integer.parseInt(singleItemLocation.split(",")[0]);
          itemY = Integer.parseInt(singleItemLocation.split(",")[1]);
          if (i == 0) {
            pillList.add(new Pill(new Location(itemX, itemY)));
            propertyPillNumber++;
          } else {
            Item item = itemFactory.createItem(i, new Location(itemX, itemY));
            itemList.add(item);
            if (item.getItemType() == ItemType.GOLD) {
              propertyGoldNumber++;
            }
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
        Item item = null;
        int a = grid.getCell(location);
        if (a == 1 && propertyPillNumber == 0) {
          pillList.add(new Pill(location));
        }
        if (a == 3 &&  propertyGoldNumber == 0) {
          item = itemFactory.createItem(1, location);
          goldList.add(new Gold(location));
        }
        if (a == 4) {
          item = itemFactory.createItem(2, location);
        }
        if (item != null) {
          itemList.add(item);
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
    for (Pill pill : pillList) {
      putPill(bg, pill);
    }
  }

  private void putItem(GGBackground bg, Item item) {
    switch (item.getItemType()) {
      case GOLD:
        bg.setPaintColor(Color.yellow);
        bg.fillCircle(toPoint(item.getInitialLocation()), 5);
        break;
      case ICE:
        bg.setPaintColor(Color.blue);
        bg.fillCircle(toPoint(item.getInitialLocation()), 5);
        break;
    }
    addActor(item, item.getInitialLocation());
  }

  private void putPill(GGBackground bg, Pill pill) {
    bg.setPaintColor(Color.white);
    bg.fillCircle(toPoint(pill.getLocation()), 5);
  }

  public void removeItem(ItemType checkItemType, Location checkLocation) {
    for (Item item : itemList) {
      if (checkLocation.equals(item.getLocation())) {
        item.hideItem();
        if (currentGameVersion.equals(gameVersion[1])) {
          switch (checkItemType) {
            case GOLD:
              for (Monster monster : monsterList) {
                monster.enterFurious(3);
              }
              for (Gold gold : goldList) {
                if (checkLocation.equals(gold.getInitialLocation())) {
                  gold.setAvailable(false);
                }
              }
              break;
            case ICE:
              for (Monster monster : monsterList) {
                monster.stopMoving(3);
              }
              break;
          }
        }
      }
    }
  }

  public void gameFinished(GGBackground bg, boolean hasPacmanBeenHit, boolean hasPacmanEatAllPills) {
    // stop all character
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
  }
}
