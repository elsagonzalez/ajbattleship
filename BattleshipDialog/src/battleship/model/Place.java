// $Id: Place.java,v 1.1 2014/12/01 00:33:19 cheon Exp $

package battleship.model;

import battleship.model.Board;

/**
 * A place in a game board. Each place has a pair of 1-based indices---
 * <code>x</code> for column and <code>y</code> for row---that uniquely
 * identify it in the board. A place can be placed on by a battleship.
 * 
 * @author cheon
 * @see Board
 */
public class Place {
	
    /** 1-based column index of this place. */
    public final int x;

    /** 1-based row index of this place. */
    public final int y;

    /** Is this place hit? */
    private boolean isHit;

    /** Battleship placed on this place. */
    private Ship ship;

    /** Board which this place belongs to. */
    private Board battleBoard;
    
    /**
     * Create a new place with the given indices belonging to the given
     * board.
     * 
     * @param x 1-based column index of the place to be created
     * @param y 1-based row index of the place to be created
     * @param battleBoard board to which the created place belongs
     */
    public Place(int x, int y, Board battleBoard) {
        this.x = x;
        this.y = y;
        this.battleBoard = battleBoard;
    }
    
    /** Return the 1-based column index of this place. 
     * @return Column index of this place.
     */
    public int getX() {
    	return x;
    }
    
    /** Return the 1-based row index of this place. 
     * @return Row index of this place.
     */
    public int getY() {
    	return y;
    }
    
    /** Is this place hit? 
     * @return True if this place was shot; false otherwise
     */
    public boolean isHit() {
    	return isHit;
    }
    
    /** Is this place hit and does it have a ship? 
     * @return True if this place has a ship and was shot; false otherwise
     */
    public boolean isHitShip() {
    	return isHit && !isEmpty();
    }
    
    /** Hit this place. */
    public void hit() {
    	isHit = true;
    	battleBoard.hit(this);
    }
    
    /** Does this place have a ship? 
     * 
     * @return True if a ship is placed on this place; false otherwise
     */
    public boolean hasShip() {
    	return ship != null;
    }
    
    /** Is this place empty? I.e., no ship placed on it? 
     * 
     * @return True if no ship is placed on this place; false otherwise
     */
    public boolean isEmpty() {
    	return ship == null;
    }
    
    /** Place the given ship on this place.
     * 
     * @param ship Ship to be placed on this place
     * @see Ship
     */
    public void placeShip(Ship ship) {
    	this.ship = ship;
    	ship.addPlace(this);
    }
    
    /**
     * Return the ship placed on this place. Return null if no ship is 
     * placed on this place.
     * 
     * @return Battleship placed on this place or null
     */
    public Ship ship() {
    	return ship;
    }

    /**
     * Reset this place. This method clears the shot made on this place 
     * and remove the ship placed on it.
     */
    public void reset() {
        isHit = false;
        if (ship != null) {
            ship.removePlace(this);
            ship = null;
        }
    }
}
