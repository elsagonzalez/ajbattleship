// $Id: Board.java,v 1.1 2014/12/01 00:33:19 cheon Exp $

package battleship.model;

import java.util.*;
import java.util.stream.IntStream;

import battleship.model.Ship;
import battleship.model.Place;

/**
 * A game board consisting of <code>size</code> * <code>size</code> places
 * where battleships can be placed. A place of the board is denoted 
 * by a pair of 1-based indices (x, y), where x is the column index 
 * and y is the row index. A place of the board can be shot, resulting 
 * in either a hit (ship) or miss. The board recognizes ship sinking 
 * and game over, both of which along with the total number of shots can
 * be observed through the <code>BoardChangeListener</code>.
 *
 * @author cheon
 * @see Place
 * @see Ship
 */
public class Board {

    /** Dimension of this board. This board will have 
     *  <code>size</code> * <code>size</code> places. */
    private final int size;

    /** Number of shots that this board was hit. */
    private int numOfShots;

    /** Places of this board.
     * @see Place
     */
    private final List<Place> places;
    
    /** Create a default fleet of ships. */
    private static List<Ship> defaultShips() {
        return Arrays.asList(new Ship[] { 
                new Ship("Aircraft carrier", 5),
                new Ship("Battleship", 4),
                new Ship("Frigate", 3),
                new Ship("Submarine", 3),
                new Ship("Minesweeper", 2), });
    }
      
    /** Fleet of ships that can be placed in this board. 
     * @see Ship
     */
    private final List<Ship> ships;
    
    /** Listeners listening to board changes such as sliding of tiles.
     * @see BoardChangeListener
     */
    private final List<BoardChangeListener> listeners;
  
    /**
     * Create an empty board of the given dimension that can host the 
     * default feet of ships (<code>DEFAULT_SHIPS</code>). The board 
     * size must be equal to or bigger than the largest ship size.
     * 
     * @param size Width and height of the board to be created
     */
    public Board(int size) {
        this(size, defaultShips());
    }

    /**
     * Create an empty board of the given dimension that can host the given
     * fleet of ships. The board size must be at least equal to the largest
     * ship size.
     * 
     * @param size Width and height of the board to be created
     * @param ships Battleships to be placed in the board
     * @see Ship
     */
    public Board(int size, Iterable<Ship> ships) {
        this.size = size;
        numOfShots = 0;
        places = new ArrayList<Place>(size * size);
        for (int x = 1; x <= size; x++) {
            for (int y = 1; y <= size; y++) {
                places.add(new Place(x, y, this));
            }
        }
        this.ships = new ArrayList<>();
        ships.forEach(e -> this.ships.add(e));
        listeners = new ArrayList<BoardChangeListener>();
    }
    
    /**
     * Remove all the battleships placed on this board. This method should
     * be called before this board is reused for another play.
     */
    public void reset() {
        numOfShots = 0;
        places.stream().forEach(p -> p.reset());
    }
    
    /**
     * Place the given ship at the specified starting position horizontally 
     * or vertically. It is assumed the given ship is not already placed.
     * 
     * @param ship 
     *            battleship to be placed.
     * @param x
     *            1-based column index of the starting position
     * @param y
     *            1-based row index of the starting position
     * @param dir
     *            true for horizontal placement and false for vertical
     *            placement.
     * @return true if the ship was placed successfully; false otherwise
     * @see Ship
     */
    public boolean placeShip(Ship ship, int x, int y, boolean dir) {
    	int len = ship.size();
    	if (dir // horizontal? 
    	    && (x + len - 1 <= this.size)  // have places?
    	    && IntStream.range(x, x + len) // not occupied?
    		    .allMatch(i -> at(i, y).isEmpty())) {
    	    IntStream.range(x, x + len).forEach(i -> at(i,y).placeShip(ship));
    	    return true;
    	}
    	if (!dir // vertical?
    	    && (y + len - 1 <= this.size)
    	    && IntStream.range(y, y + len)
    		    .allMatch(j -> at(x, j).isEmpty())) {
    	    IntStream.range(y, y + len).forEach(j -> at(x,j).placeShip(ship));
    	    return true;
    	}
    	return false;
    }

    /**
     * Place the given ship at the specified starting place horizontally 
     * or vertically. It is assumed the given ship is not already placed.
     * 
     * @param ship 
     *            battleship to be placed.
     * @param place 
     *            Starting place.
     * @param dir
     *            true for horizontal placement and false for vertical
     *            placement.
     * @return true if the ship was placed successfully; false otherwise
     * 
     * @see Ship
     * @see Place
     */
    public boolean placeShip(Ship ship, Place place, boolean dir) {
    	return placeShip(ship, place.getX(), place.getY(), dir);
    }
    
    /** Return all the places of this board.
     *  @return Places of this board
     *  @see Place
     */
    public Iterable<Place> places() {
        return places;
    }
    
    /** Return all the ships that can be placed in this board.
     * 
     * @return Ships that can be placed in this board
     * @see Ship
     */
    public Iterable<Ship> ships() {
        return ships;
    }
    
    /**
     * Return the ship of the given name that can be placed in this board.
     * If there is no such a ship, return a null.
     * 
     * @param name Name of the ship to be returned
     * @return Ship of the given name or null
     * @see Ship
     */
    public Ship ship(String name) {
    	return ships.stream().filter(s -> s.name().equals(name))
    	        .findFirst().orElse(null);
    }

    /**
     * Return the place at the given indices. Return a null if indices are
     * invalid.
     * 
     * @param x 1-based column index
     * @param y 1-based row index
     * @return Place at the given indices or null
     */
    public Place at(int x, int y) {
        for (Place p: places) {
            if (p.getX() == x && p.getY() == y) {
                return p;
            }
        }
        return null; 
    }

    /** Return the dimension of this board.
     * 
     * @return Width and height of this board
     */
    public int size() {
        return size;
    }

    /** Return the number of shots made to this board. 
     * 
     * @return Number of shots made to this board
     */
    public int numOfShots() {
        return numOfShots;
    }
    
    /** Return true if all ships are sunk. 
     * 
     * @return True if all ships are sunk; false otherwise
     */
    public boolean isGameOver() {
    	return ships.stream().allMatch(s -> s.isSunk());
    }
    
    /** Record that the given place is hit. This method will call the
     * <code>hit</code> method on the given place if the place is
     * not already marked as hit. If hitting the place means a ship
     * hit, ship sinking, or game over, this method will notify this board
     * change to all registered observers.
     * 
     * @param place Place to shoot
     * @see Place
     */
	public void hit(Place place) {
        if (!place.isHit()) {
            place.hit();
            return; // Place.hit will call this method.
        }
        numOfShots++;
        notifyHit(place, numOfShots);

        if (!place.isEmpty()) {
            if (place.ship().isSunk()) {
                notifyShipSunk(place.ship());
                if (isGameOver()) {
                    notifyGameOver(numOfShots);
                }
            }
        }
	}

    /** Register the given listener to listen to board changes.
     * @param listener Listener to be added
     * @see BoardChangeListener
     */
    public void addBoardChangeListener(BoardChangeListener listener) {
    	if (!listeners.contains(listener)) {
    	    listeners.add(listener);
    	}
    }
    
    /** Unregister the given listener from listening to board changes. 
     * @param listener Listener to be removed
     * @see BoardChangeListener
     */
    public void removeBoardChangeListener(BoardChangeListener listener) {
        listeners.remove(listener);
    }
    
    /** Notify a place hit to the registered board change listeners. 
     * 
     * @param place Place that was shot
     * @param numOfShots Total number of shots
     * @see Place
     */
    private void notifyHit(Place place, int numOfShots) {
    	for (BoardChangeListener listener: listeners) {
    	    listener.hit(place, numOfShots);
    	}
    }
    
    /** Notify a game-over to the registered board change listeners.
     * @param numOfShots Total number of shots
     */
    private void notifyGameOver(int numOfShots) {
    	for (BoardChangeListener listener: listeners) {
    	    listener.gameOver(numOfShots);
    	}
    }
    
    /** Notify a ship sinking to the registered board change listeners.
     * @param ship Ship that was sunk
     * @see Ship
     */
    private void notifyShipSunk(Ship ship) {
    	for (BoardChangeListener listener: listeners) {
    	    listener.shipSunk(ship);
    	}
    }
    
    /** To listen to board changes such as hit. */
    public interface BoardChangeListener {
    	
        /**
         * Called when a place is hit. The place that is hit and the number
         * of shots are provided.
         * 
         * @param place Place that was shot
         * @param numOfShots Total number of shots
         * @see Place
         */
    	void hit(Place place, int numOfShots);
    	
        /** Called the game is over. I.e., all ships are sunk. 
         * @param numOfShots Total number of shots
         */
    	void gameOver(int numOfShots);
    	
        /** Called when a ship is sunk. 
         * @param ship Ship that was sunk
         * @see Ship
         */
    	void shipSunk(Ship ship);
    }

    /** Provide a no-op implementation for all observer methods. */
    public static class BoardChangeAdapter implements BoardChangeListener {
    	public void hit(Place place, int numOfShots) {}
    	public void gameOver(int numOfShots) {}
    	public void shipSunk(Ship ship) {}
    }
    
}
