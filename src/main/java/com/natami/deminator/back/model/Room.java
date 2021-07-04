package com.natami.deminator.back.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.natami.deminator.back.responses.EntityRoom;
import com.natami.deminator.back.util.InvalidSettingsException;

public class Room implements EntityRoom {
	public static final String VALID_ROOM_NUMBER_REGEXP = "^[0-9A-Za-z]{4,12}$";
	public static final int DEFAULT_SQUARE_SIZE = 16;
	public static final int DEFAULT_MINES_COUNT = 16;
	public static final int DEFAULT_TURN_DURATION = 30;
	public static final int START_TIME_DELAY_AFTER_LAUNCH = 5; // seconds

	private int width = DEFAULT_SQUARE_SIZE; // 0 ... x ... width-1
	private int height = DEFAULT_SQUARE_SIZE; // 0 ... y ... height -1
	private int minesCount = DEFAULT_MINES_COUNT;
	private String randomSeed = null;
	private Game game = null;
	private int turnDuration = DEFAULT_TURN_DURATION;
	private String gameStartTime = null;
	private final Map<Long, Player> players = new HashMap<>();

	public Room(String seed) {
		this.randomSeed = seed;
		start();
	}

	public String getSeed() {
		return this.randomSeed;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeigth(int height) {
		this.height = height;
	}
	public void setMinesCount(int minesCount) {
		this.minesCount = minesCount;
	}

	public void registerPlayer(long playerId) {
		if(!this.players.containsKey(playerId)) {
			// Create new player
			Player p = new Player(playerId);
			p.setReady(false);
			players.put(playerId, p);
		}
	}

	public void setTurnDuration(int turnDuration) {
		this.turnDuration = turnDuration;
	}

	public boolean start() {
		if(this.game != null) {
			return true;
		}
		try {
			// Set start date time
			Calendar startTime = Calendar.getInstance();
			startTime.add(Calendar.SECOND, START_TIME_DELAY_AFTER_LAUNCH);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			this.gameStartTime = formatter.format(startTime.getTime()); // e.g. 2000-12-31T23:59:59

			this.game = new Game(this);
			return true;
		} catch (InvalidSettingsException e) {}
		return false;
	}

	public void stop() {
		this.game = null;
	}

	public Player getPlayer(long clientID) {
		return this.players.get(clientID);
	}

	public void setPlayerReady(long uniqueSessionId, boolean ready) {
		getPlayer(uniqueSessionId).setReady(ready);
	}

	public Game getGame() {
		this.game.update();
		return this.game;
	}

	public boolean areAllPlayersReady() {
		// TODO
		return true;
	}

	// // // Interfaces // // //

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getMinesCount() {
		return this.minesCount;
	}

	@Override
	public int getTurnDuration() {
		return this.turnDuration;
	}

	@Override
	public String getGameStartTime() {
		return this.gameStartTime;
	}

	@Override
	public List<Player> getPlayerList() {
		return new ArrayList<>(this.players.values());
	}


	// // // STATIC // // //

	public static boolean isValidRoomNumber(String roomNumber) {
		return roomNumber != null && roomNumber.matches(VALID_ROOM_NUMBER_REGEXP);
	}

}
