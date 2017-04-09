package battleship.model;

import java.util.ArrayList;
import java.util.List;

import battleship.model.Place;

/**
 * A ship that can be placed in a game board. A battle ship has 
 * a name and a size representing the number of sections of the ship.
 * 
 * @author cheon
 */
public class Ship {

    /** Name of this ship. */
    private String name;
    
    /** Number of sections of this ship. */
    private int size;

    /**
     * Places in the board where this ship is placed on.
     * 
     * <pre>
     * INV: places.size() == 0 || places.size() == size
     * </pre>
     */
    private List<Place> places;

    /**
     * Create a new ship of the given name and size. Initially, the 
     * created ship is not placed in a board.
     * 
     * @param name Name of the ship to be created
     * @param size Size of the ship to be created
     */
    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        places = new ArrayList<Place>(size);
    }
    
    /** Return the name of this ship. 
     * @return Name of this ship
     */
    public String name() {
    	return name;
    }
    
    /** Return the size of this ship. I.e., the number of sections.
     * 
     *  @return Size of this ship
     */
    public int size() {
    	return size;
    }
    
    /**
     * Return the starting place of this ship. This method is supposed 
     * to be called when this ship is placed in a board.
     * 
     * @return Starting place of this ship
     */
    public Place head() {
    	return places.get(0);
    }
    
    /**
     * Return the ending place of this ship. Must be called when this ship 
     * is placed in a board.
     * 
     * @return Ending place of this ship
     */
    public Place tail() {
    	return places.get(places.size() - 1);
    }
    
    /**
     * Is this ship placed horizontally? Must be called when this ship
     * is placed in a board.
     * 
     * @return True if this ship is placed horizontally; false otherwise
     */
    public boolean isHorizontal() {
    	return head().getY() == tail().getY();
    }
    
    /**
     * Is this ship placed vertically? Must be called when this ship is 
     * placed in a board.
     * 
     * @return True if this ship is placed vertically; false otherwise
     */
    public boolean isVertical() {
    	return head().getX() == tail().getX();
    }
    
    /**
     * Return the places on which this ship is placed. Return an empty 
     * Iterable if this ship is not placed in a board.
     * 
     * @return Places of this ship
     * @see Place
     */
    public Iterable<Place> places() {
        return places;
    }
    
    /**
     * Is this ship sunk? A ship is sunk if all its sections are hit.
     * If ship is not placed, this method returns false.
     * 
     * @return True if this ship is sunk; false otherwise
     */
    public boolean isSunk() {
        return size == places.size()
                && places.stream().allMatch(p -> p.isHit());
    }

    /**
     * Add the given place to the set of places on which this ship is 
     * placed.
     * 
     * @param place Place to be added
     * @see Place
     */
    public void addPlace(Place place) {
        if (!places.contains(place)) {
            places.add(place);
        }
        if (place.ship() != this) {
            place.placeShip(this);
        }
    }

    /**
     * Remove the given place from the set of places on which this ship is
     * placed. This method should be used only from a place to break a mutual
     * recursive link between a place and a ship; see the reset method of the
     * Place class.
     * 
     * @param place Place to be removed
     * @see Place
     */
    public void removePlace(Place place) {
        places.remove(place);
    }
	
    /** Return true if this ship is placed in a board. 
     * 
     * @return True if this ship is placed in a board; false otherwise
     */
    public boolean isDeployed() {
        return !places.isEmpty();
    }
	
    /** Clear the places of this ship. This method makes this ship
     *  be unplaced in a board. */
    public void removePlaces() {
        List<Place> copies = new ArrayList<>(places);
        for (Place p : copies) {
            p.reset(); // will call back this (removePlace) method.
        }
    }
    
}
